package pt.ist.socialsoftware.edition.ldod.bff.virtual.service;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Service;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.backend.jvstmojb.pstm.AbstractDomainObject;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.EditorialInterDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.bff.text.dtos.inter.SourceInterDto;
import pt.ist.socialsoftware.edition.ldod.bff.virtual.dtos.*;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.recommendation.properties.Property;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateAcronymException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.Message;
import pt.ist.socialsoftware.edition.ldod.social.aware.AwareAnnotationFactory;
import pt.ist.socialsoftware.edition.ldod.validator.ClassificationGameValidator;
import pt.ist.socialsoftware.edition.ldod.validator.VirtualEditionValidator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class VirtualService {


    public VirtualEditionsDto getVirtualEditionsList() {
        List<VirtualEditionDto> ves = getVirtualEditions()
                .stream()
                .map(this::getVirtualEditionDto)
                .collect(Collectors.toList());
        return new VirtualEditionsDto(ves, getUser());


    }

    private VirtualEditionDto getVirtualEditionDto(VirtualEdition ve) {
        return VirtualEditionDto
                .VirtualEditionDtoBuilder
                .aVirtualEditionDto(ve)
                .activeMembers(ve.getParticipantSet().stream().map(LdoDUser_Base::getUsername).collect(Collectors.toList()))
                .pendingMembers(ve.getPendingSet().stream().map(LdoDUser_Base::getUsername).collect(Collectors.toList()))
                .member(getUser(), ve, getUser())
                .build();
    }

    private LdoDUser getUser() {
        return LdoDUser.getAuthenticatedUser();
    }

    public VirtualEditionsDto createVirtualEdition(VirtualEditionDto veDto) {
        LdoDUser user = checkUserNotNull();
        String acronym = veDto.getAcronym().trim();
        String title = veDto.getTitle().trim();
        VirtualEditionValidator.validateInputs(acronym, title);
        try {
            LdoD.getInstance().createVirtualEdition(user, VirtualEdition.ACRONYM_PREFIX + acronym, title, new LocalDate(), veDto.isPub(), LdoD.getInstance().getEdition(veDto.getUseEdition()));
        } catch (LdoDDuplicateAcronymException ex) {
            throw LdoDException
                    .LdoDExceptionBuilder
                    .aLdoDException()
                    .message(String.format(Message.DUPLICATE_ACRONYM.getLabel(), acronym))
                    .build();
        }
        return getVirtualEditionsList();
    }

    public void deleteVirtualEdition(String externalId) {
        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        if (virtualEdition == null)
            throw new LdoDException(String.format(Message.VIRTUAL_EDITION_INVALID.getLabel(), externalId));
        virtualEdition.remove();
    }


    private List<VirtualEdition> getVirtualEditions() {
        return LdoD.getInstance().getVirtualEditionsSet()
                .stream()
                .filter(VirtualEdition::checkAccess)
                .collect(Collectors.toList());
    }


    @Atomic(mode = Atomic.TxMode.WRITE)
    public VirtualEditionDto toggleSelectedVE(String externalId, boolean selected) {
        LdoDUser user = checkUserNotNull();
        VirtualEdition edition = checkVENotNull(externalId);
        if (selected) user.addSelectedVirtualEditions(edition);
        else user.removeSelectedVirtualEditions(edition);
        return getVirtualEditionDto(edition);

    }

    public VirtualEditionDto submitParticipant(String externalId) {
        LdoDUser user = checkUserNotNull();
        VirtualEdition edition = checkVENotNull(externalId);
        edition.addMember(user, Member.MemberRole.MEMBER, false);
        return getVirtualEditionDto(edition);
    }

    public VirtualEditionDto cancelParticipation(String externalId) {
        LdoDUser user = checkUserNotNull();
        VirtualEdition edition = checkVENotNull(externalId);
        edition.cancelParticipationSubmission(user);
        return getVirtualEditionDto(edition);
    }

    protected LdoDUser checkUserNotNull() {
        if (getUser() == null) throw new LdoDException(Message.USER_NOT_FOUND.getLabel());
        return getUser();
    }

    protected LdoDUser checkUserNotNull(String username) {
        LdoDUser user = LdoD.getInstance().getUser(username);
        if (user == null) throw new LdoDException(String.format(Message.USERNAME_NOT_FOUND.getLabel(), username));
        return user;
    }

    protected VirtualEdition checkVENotNull(String externalId) {
        VirtualEdition edition = FenixFramework.getDomainObject(externalId);
        if (edition == null)
            throw new LdoDException(String.format(Message.VIRTUAL_EDITION_NOT_FOUND.getLabel(), externalId));
        return edition;
    }

    protected VirtualEdition checkVeByAcronymNotNull(String acronym) {
        VirtualEdition edition = LdoD.getInstance().getVirtualEdition(acronym);
        if (edition == null)
            throw new LdoDException(String.format(Message.VIRTUAL_EDITION_NOT_FOUND.getLabel(), acronym));
        return edition;
    }

    private ClassificationGame checkCgNotNull(String gameId) {
        ClassificationGame game = FenixFramework.getDomainObject(gameId);
        if (game == null)
            throw new LdoDException(String.format(Message.GAME_NOT_FOUND.getLabel(), gameId));
        return game;
    }


    public VirtualEditionDto approveParticipation(String externalId, String username) {
        VirtualEdition ve = checkVENotNull(externalId);
        LdoDUser participant = checkUserNotNull(username);
        ve.addApprove(participant);
        return getVeDtoWithParticipants(ve);
    }

    public VirtualEditionDto removeParticipant(String externalId, String username) {

        VirtualEdition ve = checkVENotNull(externalId);
        LdoDUser participant = checkUserNotNull(username);
        if (!ve.canRemoveMember(LdoDUser.getAuthenticatedUser(), participant))
            throw new LdoDException(Message.OPERATION_NOT_AUTHORIZED.getLabel());
        ve.removeMember(participant);
        return getVeDtoWithParticipants(ve);
    }


    public VirtualEditionDto switchRole(String externalId, String username) {
        VirtualEdition ve = checkVENotNull(externalId);
        LdoDUser participant = checkUserNotNull(username);
        if (!ve.canSwitchRole(LdoDUser.getAuthenticatedUser(), participant))
            throw new LdoDException(Message.OPERATION_NOT_AUTHORIZED.getLabel());
        ve.switchRole(participant);
        return getVeDtoWithParticipants(ve);
    }


    public VirtualEditionDto addParticipant(String externalId, String username) {
        VirtualEdition ve = checkVENotNull(externalId);
        LdoDUser participant = checkUserNotNull(username);
        ve.addMember(participant, Member.MemberRole.MEMBER, true);
        return getVeDtoWithParticipants(ve);
    }


    public VirtualEditionDto getVirtualEdition(String externalId) {
        return getVeDtoWithParticipants(checkVENotNull(externalId));
    }

    private VirtualEditionDto getVeDtoWithParticipants(VirtualEdition ve) {
        VirtualEditionDto veDto = getVirtualEditionDto(ve);
        veDto.setParticipants(Stream
                .concat(ve.getParticipantList().stream(), ve.getPendingSet().stream())
                .map(u -> new VeUserDto(u, ve, getUser()))
                .collect(Collectors.toList()));
        return veDto;
    }

    public GameDto getClassificationGames(String externalId) {
        VirtualEdition ve = checkVENotNull(externalId);
        return new GameDto(ve.getTaxonomy().getOpenAnnotation(), getVeInters(ve), ve.getClassificationGameSet()
                .stream()
                .sorted(Comparator.comparing(ClassificationGame_Base::getDateTime))
                .map(game -> new ClassGameDto(game, getUser()))
                .collect(Collectors.toList()));
    }

    public GameDto createClassGame(String externalId, ClassGameDto gameDto) {
        VirtualEdition ve = checkVENotNull(externalId);
        VirtualEditionInter inter = FenixFramework.getDomainObject(gameDto.getInterExternalId());
        ClassificationGameValidator.validateInputs(gameDto.getDescription(), gameDto.getInterExternalId());
        ve.createClassificationGame(gameDto.getDescription(),
                DateTime.parse(gameDto.getDate(), DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm")), inter,
                LdoDUser.getAuthenticatedUser());

        return getClassificationGames(externalId);

    }

    private List<VirtualEditionInterDto> getVeInters(VirtualEdition ve) {
        return ve.getAllDepthVirtualEditionInters()
                .stream()
                .map(VirtualEditionInterDto::new)
                .collect(Collectors.toList());
    }

    public GameDto removeClassGame(String externalId, String gameExternalId) {
        VirtualEdition ve = checkVENotNull(externalId);
        ClassificationGame game = FenixFramework.getDomainObject(gameExternalId);
        if (game == null || !game.canBeRemoved()) throw new LdoDException(Message.GAME_CAN_NOT_BE_REMOVED.getLabel());
        game.remove();
        return getClassificationGames(externalId);
    }

    public VirtualEditionDto editVirtualEdition(String externalId, VirtualEditionDto veDto) {
        VirtualEdition ve = checkVENotNull(externalId);
        String acronym = veDto.getAcronym().trim();
        String title = veDto.getTitle().trim();
        VirtualEditionValidator.validateInputs(acronym, title);

        System.out.println(veDto.getSynopsis());

        ve.edit(VirtualEdition.ACRONYM_PREFIX + acronym, title,
                veDto.getSynopsis(),
                veDto.isPub(),
                veDto.isTaxonomyOpenManagement(),
                veDto.isTaxonomyOpenVocab(),
                veDto.isTaxonomyOpenAnnotation(),
                veDto.getMediaSource(),
                veDto.getBeginDate(),
                veDto.getEndDate(),
                veDto.getCountries(),
                veDto.getMinFrequency().toString());

        AwareAnnotationFactory awareFactory = new AwareAnnotationFactory();
        if (ve.isSAVE())
            awareFactory.searchForAwareAnnotations(ve);

        else {
            ve.getAllDepthVirtualEditionInters().forEach(awareFactory::removeAllAwareAnnotationsFromVEInter);
        }
        return getVeDtoWithParticipants(ve);
    }

    public VirtualEditionAssistedSortDto getEditionWithRecommendation(String externalId) {
        VirtualEdition virtualEdition = checkVENotNull(externalId);
        LdoDUser user = checkUserNotNull();
        RecommendationWeights recommendationWeights = user.getRecommendationWeights(virtualEdition);
        recommendationWeights.setWeightsZero();
        List<VirtualEditionInter> recommendedEdition = new ArrayList<>();
        String selected = "";
        if (!virtualEdition.getAllDepthVirtualEditionInters().isEmpty()) {
            VirtualEditionInter inter = virtualEdition.getAllDepthVirtualEditionInters().get(0);
            selected = inter.getExternalId();
            recommendedEdition = virtualEdition.generateRecommendation(inter, recommendationWeights);
        }
        return getVirtualEditionAssistedSortDto(recommendedEdition, selected, null);
    }

    private VirtualEditionAssistedSortDto getVirtualEditionAssistedSortDto(List<VirtualEditionInter> recommendedEdition,
                                                                           String selected,
                                                                           List<Property> properties) {
        return new VirtualEditionAssistedSortDto(selected, getVirtualEditionInters(recommendedEdition), properties);
    }

    private List<VirtualEditionInterDto> getVirtualEditionInters(List<VirtualEditionInter> inters) {
        return inters
                .stream()
                .map(veInter -> VirtualEditionInterDto
                        .VirtualEditionInterDtoBuilder
                        .aVirtualEditionInterDto(veInter)
                        .usedList(veUsedList(veInter))
                        .build()).collect(Collectors.toList());
    }

    private List<FragInterDto> veUsedList(FragInter veInter) {
        return veInter.getListUsed()
                .stream()
                .map(inter -> inter.getSourceType().equals(Edition.EditionType.EDITORIAL)
                        ? EditorialInterDto.EditorialInterDtoBuilder.anEditorialInterDto(inter)
                        .title(inter.getTitle())
                        .shortName(inter.getShortName())
                        .build()
                        : SourceInterDto.SourceInterDtoBuilder.aSourceInterDto(inter)
                        .title(inter.getTitle())
                        .shortName(inter.getShortName())
                        .build())
                .collect(Collectors.toList());
    }

    public VirtualEditionAssistedSortDto setLinearVE(String externalId, VirtualEditionLinearBodyDto bodyDto) {
        VirtualEdition virtualEdition = checkVENotNull(externalId);
        LdoDUser user = checkUserNotNull();
        RecommendationWeights recommendationWeights = user.getRecommendationWeights(virtualEdition);
        recommendationWeights.setWeights(bodyDto.getProperties());

        List<VirtualEditionInter> recommendedEdition = new ArrayList<>();

        if (bodyDto.getSelected() != null && !bodyDto.getSelected().isEmpty()) {
            VirtualEditionInter inter = FenixFramework.getDomainObject(bodyDto.getSelected());
            recommendedEdition = virtualEdition.generateRecommendation(inter, recommendationWeights);
        }
        return getVirtualEditionAssistedSortDto(recommendedEdition, bodyDto.getSelected(), bodyDto.getProperties());
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public VirtualEditionAssistedSortDto saveLinearVE(String externalId, List<String> inters) {
        VirtualEdition virtualEdition = checkVENotNull(externalId);
        if (virtualEdition.getSourceType().equals(Edition.EditionType.VIRTUAL)) {
            Section section = virtualEdition.createSection(Section.DEFAULT);
            inters.forEach(id -> section.addVirtualEditionInter(FenixFramework.getDomainObject(id), inters.indexOf(id) + 1));
            virtualEdition.clearEmptySections();
        }
        return getEditionWithRecommendation(externalId);
    }

    public List<VirtualEditionInterDto> getEditionForManualSort(String externalId) {
        VirtualEdition virtualEdition = checkVENotNull(externalId);
        return virtualEdition.getSortedInterps()
                .stream()
                .map(inter -> VirtualEditionInterDto.VirtualEditionInterDtoBuilder.aVirtualEditionInterDto(inter)
                        .usedList(veUsedList(inter))
                        .build())
                .collect(Collectors.toList());

    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public List<VirtualEditionInterDto> mutateVirtualEditionInters(String externalId, List<String> inters) {

        VirtualEdition ve = checkVENotNull(externalId);
        ve.getAllDepthVirtualEditionInters()
                .stream()
                .filter(inter -> !inters.contains(inter.getExternalId()))
                .forEach(VirtualEditionInter::remove);

        inters.forEach(id -> {
            int index = inters.indexOf(id) + 1;
            if (ve.getAllDepthVirtualEditionInters().stream().map(AbstractDomainObject::getExternalId).noneMatch(extId -> extId.equals(id)))
                ve.createVirtualEditionInter(FenixFramework.getDomainObject(id), index);
            ve.getInterById(id).ifPresent(inter -> inter.setNumber(index));
        });

        return getEditionForManualSort(externalId);

    }

    public VirtualEditionDto getVirtualEditionByAcronym(String acronym) {
        VirtualEdition ve = checkVeByAcronymNotNull(acronym);
        VirtualEditionDto veDto = getVeDtoWithParticipants(ve);
        veDto.setVirtualEditionInters(ve.getAllDepthVirtualEditionInters()
                .stream()
                .map(inter -> VirtualEditionInterDto
                        .VirtualEditionInterDtoBuilder
                        .aVirtualEditionInterDto(inter)
                        .categories(inter.getAssignedCategories().stream().map(Category_Base::getName).collect(Collectors.toList()))
                        .usedList(veUsedList(inter))
                        .build())
                .collect(Collectors.toList()));
        return veDto;

    }

    public VeUserDto getUserVeData(String username) {
        return new VeUserDto(LdoD.getInstance().getUser(username));
    }

    public CategoryDto getVirtualEditionCategories(String acronym, String category) {
        Category cat = LdoD.getInstance().getVirtualEdition(acronym).getTaxonomy().getCategory(category);
        return CategoryDto
                .CategoryDtoBuilder
                .aCategoryDto(cat)
                .veInters(cat.getSortedInters()
                        .stream()
                        .map(inter -> VirtualEditionInterDto
                                .VirtualEditionInterDtoBuilder
                                .aVirtualEditionInterDto(inter)
                                .usedList(inter.getListUsed().stream().map(VirtualEditionInterDto::new).collect(Collectors.toList()))
                                .users(inter.getContributorSet(cat)
                                        .stream()
                                        .map(user -> VeUserDto.VeUserDtoBuilder.aVeUserDto()
                                                .username(user.getUsername())
                                                .firstname(user.getFirstName())
                                                .lastname(user.getLastName())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build())
                        .collect(Collectors.toList()))
                .build();

    }

    public TaxonomyDto getVirtualEditionTaxonomy(String acronym) {
        return new TaxonomyDto(LdoD.getInstance().getVirtualEdition(acronym).getTaxonomy());
    }

    public VeClassGameDto getVeClassGameDto(String gameId) {
        return new VeClassGameDto(checkCgNotNull(gameId));
    }
}


