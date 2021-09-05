package pt.ist.socialsoftware.edition.virtual.domain;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import pt.ist.socialsoftware.edition.notification.dtos.text.ScholarInterDto;
import pt.ist.socialsoftware.edition.notification.dtos.user.UserDto;
import pt.ist.socialsoftware.edition.notification.event.Event;
import pt.ist.socialsoftware.edition.notification.event.EventVirtualEditionUpdate;


import pt.ist.socialsoftware.edition.notification.utils.LdoDDuplicateAcronymException;
import pt.ist.socialsoftware.edition.notification.utils.LdoDException;
import pt.ist.socialsoftware.edition.notification.utils.PropertiesManager;
import pt.ist.socialsoftware.edition.virtual.api.VirtualEventPublisher;
import pt.ist.socialsoftware.edition.virtual.api.VirtualRequiresInterface;
import pt.ist.socialsoftware.edition.virtual.config.BeanUtil;


import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class VirtualEdition extends VirtualEdition_Base {
    private static final Logger logger = LoggerFactory.getLogger(VirtualEdition.class);

    public static final String ARCHIVE_EDITION_ACRONYM = "LdoD-Arquivo";
    public static final String ARCHIVE_EDITION_NAME = "Arquivo LdoD";
    public static String ACRONYM_PREFIX = "LdoD-";

    @Override
    public String getTitle() {
        return StringEscapeUtils.escapeHtml(super.getTitle());
    }

    @Override
    public String getSynopsis() {
        return StringEscapeUtils.escapeHtml(super.getSynopsis());
    }

    @Override
    public void setAcronym(String acronym) {

        if (getAcronym() != null && !getAcronym().toUpperCase().equals(acronym.toUpperCase()) || getAcronym() == null) {

            if (!acronym.matches("^[A-Za-z0-9\\-]+$")) {
                throw new LdoDException("acronym");
            }

            // cannot change acronym of the archive edition
            if (getAcronym() == null || !getAcronym().equals(VirtualEdition.ARCHIVE_EDITION_ACRONYM)) {

           //     TextProvidesInterface textProvidesInterface = new TextProvidesInterface();

                if (VirtualRequiresInterface.getInstance().acronymExists(acronym)) {
                    throw new LdoDDuplicateAcronymException();
                }

                for (VirtualEdition edition : VirtualModule.getInstance().getVirtualEditionsSet()) {
                    if (edition.getAcronym() != null && acronym.toUpperCase().equals(edition.getAcronym().toUpperCase())) {
                        throw new LdoDDuplicateAcronymException();
                    }
                }

//                EventInterface.getInstance().publish(new EventVirtualEditionUpdate(this.getAcronym(), acronym));
                VirtualEventPublisher virtualEventPublisher = BeanUtil.getBean(VirtualEventPublisher.class);
                virtualEventPublisher.publishEvent(new EventVirtualEditionUpdate(this.getAcronym(), acronym));

                super.setAcronym(acronym);
            }
        }
    }

    @Override
    public String getXmlId() {
        return "ED.VIRT." + getAcronym();
    }

    public VirtualEdition(VirtualModule virtualModule, String participant, String acronym, String title, LocalDate date, Boolean pub,
                          String acronymOfUsed) {
        setVirtualModule4Virtual(virtualModule);
        new Member(this, participant, Member.MemberRole.ADMIN, true);
        setXmlId("ED.VIRT." + acronym);
        setAcronym(acronym);
        setTitle(title);
        setDate(date);
        setPub(pub);
        setTaxonomy(new Taxonomy());
        createSection(Section.DEFAULT, 0);
        if (acronymOfUsed != null && !acronymOfUsed.equals("")) {
            VirtualEdition virtualEdition = VirtualModule.getInstance().getVirtualEdition(acronymOfUsed);
            if (virtualEdition != null) {
                for (VirtualEditionInter inter : virtualEdition.getIntersSet()) {
                    createVirtualEditionInter(inter, inter.getNumber());
                }
            } else {
               // TextProvidesInterface textProvidesInterface = new TextProvidesInterface();
                List<ScholarInterDto> scholarInterDtos = VirtualRequiresInterface.getInstance().getExpertEditionScholarInterDtoList(acronymOfUsed);
                for (ScholarInterDto scholarInterDto : scholarInterDtos) {
                    createVirtualEditionInter(scholarInterDto, scholarInterDto.getNumber());
                }
            }
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void remove() {
        String acronym = this.getAcronym();

        // delete directory and all its files if it exists
        String path = PropertiesManager.getProperties().getProperty("corpus.dir");
        File directory = new File(path + getExternalId());
        if (directory.exists()) {
            try {
                FileUtils.deleteDirectory(directory);
            } catch (IOException e) {
                // Unable to delete directory
                e.printStackTrace();
            }
        }

        setVirtualModule4Virtual(null);

        getTaxonomy().remove();

        getMemberSet().stream().forEach(m -> m.remove());

        getCriteriaSet().stream().forEach(c -> c.remove());

        for (SelectedBy selectedBy : getSelectedBySet()) {
            selectedBy.remove();
        }

        for (Section section : getSectionsSet()) {
            section.remove();
        }

        for (VirtualEditionInter inter : getAllDepthVirtualEditionInters()) {
            inter.remove();
        }

        deleteDomainObject();
//        EventInterface.getInstance().publish(new Event(Event.EventType.VIRTUAL_EDITION_REMOVE, acronym));
        VirtualEventPublisher virtualEventPublisher = BeanUtil.getBean(VirtualEventPublisher.class);
        virtualEventPublisher.publishEvent(new Event(Event.EventType.VIRTUAL_EDITION_REMOVE, acronym));
    }

    @Override
    public void setPub(Boolean pub) {
        if (!pub) {
            Set<String> participants = getParticipantSet();
            for (SelectedBy user : getSelectedBySet()) {
                if (!participants.contains(user.getUser())) {
                    this.removeSelectedBy(user);
                }
            }
        }
        super.setPub(pub);
    }

    public boolean isVirtualEdition() {
        return true;
    }


//    public List<VirtualEditionInter> getSortedInter4Frag(Fragment fragment) {
//        return getAllDepthVirtualEditionInters().stream().filter(i -> i.getFragmentXmlId().equals(fragment.getXmlId())).sorted().collect(Collectors.toList());
//    }

    public boolean canAddFragInter(VirtualEditionInter virtualEditionInter) {
        return canAddFragInter(virtualEditionInter.getLastUsed());
    }

    public boolean canAddFragInter(String xmlId) {
        VirtualEditionInter virtualEditionInter = VirtualModule.getInstance().getVirtualEditionInterByXmlId(xmlId);
        if (virtualEditionInter != null) {
            return canAddFragInter(virtualEditionInter);
        }
        return canAddFragInter(VirtualRequiresInterface.getInstance().getScholarInterByXmlId(xmlId));
    }

    // determines if the fragment can have more interpretations for this virtual
    // edition, deals with the the case of a fragment having two interpretations
    // for the same expert edition
    public boolean canAddFragInter(ScholarInterDto addInter) {
        String fragmentXmlId = addInter.getFragmentXmlId();

        for (VirtualEditionInter inter : getVirtualEditionInterSetForFragment(fragmentXmlId)) {
            ScholarInterDto usedInter = inter.getLastUsed();
            if (isSameInterpretation(addInter, usedInter)) {
                return false;
            }

            if (atLeastOneIsSourceInterpretation(addInter, usedInter)) {
                return false;
            }

            if (belongToDifferentExpertEditions(addInter, usedInter)) {
                return false;
            }
        }
        return true;
    }

    private boolean belongToDifferentExpertEditions(ScholarInterDto usedAddInter, ScholarInterDto usedInter) {

        return !usedAddInter.getExpertEditionAcronym().
                equals(usedInter.getExpertEditionAcronym());
    }

    private boolean atLeastOneIsSourceInterpretation(ScholarInterDto usedAddInter, ScholarInterDto usedInter) {
        return usedInter.isSourceInter() || usedAddInter.isSourceInter();
    }

    private boolean isSameInterpretation(ScholarInterDto usedAddInter, ScholarInterDto usedInter) {
        return usedAddInter.getXmlId().equals(usedInter.getXmlId());
    }

    public int getMaxFragNumber() {
        int max = 0;
        for (VirtualEditionInter inter : getAllDepthVirtualEditionInters()) {

            max = inter.getNumber() > max ? inter.getNumber() : max;
        }

        return max;
    }

    public Set<VirtualEditionInter> getIntersSet() {
        return new HashSet<>(getAllDepthVirtualEditionInters());
    }

    public Set<VirtualEditionInter> getVirtualEditionInterSetForFragment(String fragmentXmlId) {
        return getAllDepthVirtualEditionInters().stream().filter(virtualEditionInter -> virtualEditionInter.getFragmentXmlId().equals(fragmentXmlId))
                .collect(Collectors.toSet());
    }

    public List<VirtualEditionInter> getSortedInterps() {
        return getIntersSet().stream().map(VirtualEditionInter.class::cast).sorted().collect(Collectors.toList());
    }

    public VirtualEditionInter getFragInterByUrlId(String urlId) {
        return getIntersSet().stream().filter(i -> i.getUrlId().equals(urlId)).findFirst().orElse(null);
    }

    public VirtualEditionInter getFragInterByXmlId(String xmlId) {
        return getIntersSet().stream().filter(i -> i.getXmlId().equals(xmlId)).findFirst().orElse(null);
    }

    public String getReference() {
        return getAcronym();
    }

    public String getShortAcronym() {
        return getAcronym().substring(ACRONYM_PREFIX.length());
    }

    // TODO corrigir o caso dos parâmetros vazios e também o new e o remove
    @Atomic(mode = TxMode.WRITE)
    public void edit(String acronym, String title, String synopsis, boolean pub, boolean openManagement,
                     boolean openVocabulary, boolean openAnnotation, String mediaSource, String beginDate, String endDate,
                     String geoLocation, String frequency) {
        setPub(pub);
        setTitle(title);
        if (synopsis.length() > 1500) {
            setSynopsis(synopsis.substring(0, 1499));
        } else {
            setSynopsis(synopsis);
        }
        setAcronym(acronym);
        getTaxonomy().edit(openManagement, openVocabulary, openAnnotation);

        MediaSource medSource = this.getMediaSource();
        // creates
        if (medSource == null) {
            if (mediaSource.equals("noMediaSource")) {
                // do nothing
            } else if (mediaSource.equals("Twitter")) {
                new MediaSource(this, mediaSource);
            }
        }
        // removes or edits
        else {
            if (mediaSource.equals("noMediaSource")) {
                medSource.remove();
            } else if (mediaSource.equals("Twitter")) {
                medSource.edit(mediaSource);
            }
        }

        TimeWindow timeWindow = this.getTimeWindow();
        LocalDate bDate = null;
        LocalDate eDate = null;
        // creates
        if (timeWindow == null) {
            if (!beginDate.equals("") || !endDate.equals("")) {
                if (!beginDate.equals("")) {
                    bDate = new LocalDate(beginDate);
                }
                if (!endDate.equals("")) {
                    eDate = new LocalDate(endDate);
                }
                new TimeWindow(this, bDate, eDate);
            }
        }
        // removes or edits
        else {
            if (beginDate.equals("") && endDate.equals("")) {
                timeWindow.remove();
            } else {
                if (!beginDate.equals("")) {
                    bDate = new LocalDate(beginDate);
                }
                if (!endDate.equals("")) {
                    eDate = new LocalDate(endDate);
                }
                timeWindow.edit(bDate, eDate);
            }
        }

        // remover o noCountry!! fazer a comparaçao com a String vazia
        GeographicLocation geographicLocation = this.getGeographicLocation();
        // creates
        if (geographicLocation == null) {
            if (geoLocation.equals("")) {
                // do nothing
            } else {
                new GeographicLocation(this, geoLocation);
            }
        }
        // removes or edits
        else {
            if (geoLocation.equals("")) {
                geographicLocation.remove();
            } else {
                geographicLocation.edit(geoLocation);
            }
        }

        Frequency freq = this.getFrequency();
        // creates
        if (freq == null) {
            if (frequency.equals("") || frequency.equals("0")) {
                // do nothing
            } else {
                new Frequency(this, Integer.parseInt(frequency));
            }
        }
        // removes or edits
        else {
            if (frequency.equals("") || frequency.equals("0")) {
                freq.remove();
            } else {
                freq.edit(Integer.parseInt(frequency));
            }
        }

        geographicLocation = this.getGeographicLocation();
        if (geographicLocation != null) {
            logger.debug(geographicLocation.getCountry());
            String[] split = geographicLocation.getCountry().split(",");
            logger.debug("size: " + split.length);
            for (String s : split) {
                logger.debug(s);
            }
        }

    }

    @Atomic(mode = TxMode.WRITE)
    public void updateVirtualEditionInters(List<String> fragIntersXmlIds) {
        // create list of current used xmlIds
        List<String> currentUsedXmlIds = new ArrayList<>();
        for (VirtualEditionInter inter : getAllDepthVirtualEditionInters()) {
            currentUsedXmlIds.add(inter.getUsesXmlId());
        }

        // remove fragments that are not in the list
        for (VirtualEditionInter inter : getAllDepthVirtualEditionInters()) {
            if (!fragIntersXmlIds.contains(inter.getUsesXmlId())) {
                inter.remove();
            }
        }

        // add new virtual edition interpretations
        int number = 0;
        for (String interXmlId : fragIntersXmlIds) {
            if (!currentUsedXmlIds.contains(interXmlId)) {
                ScholarInterDto scholarInterDto = VirtualRequiresInterface.getInstance().getScholarInterByXmlId(interXmlId);
                if (scholarInterDto != null) {
                    createVirtualEditionInter(scholarInterDto, number + 1);
                } else {
                    VirtualEditionInter virtualEditionInter = VirtualModule.getInstance().getVirtualEditionInterByXmlId(interXmlId);
                    if (virtualEditionInter != null) {
                        createVirtualEditionInter(virtualEditionInter, number + 1);
                    } else {
                        throw new LdoDException("the xmlId does not have an associated interpretation in manual virtual edition reordering for xmlId " + interXmlId);
                    }
                }
            }
            number++;
        }

        // redefine the global order
        for (VirtualEditionInter inter : getAllDepthVirtualEditionInters()) {
            String usedXmlId = inter.getUsesXmlId();
            if (fragIntersXmlIds.contains(usedXmlId)) {
                number = fragIntersXmlIds.indexOf(usedXmlId) + 1;
                inter.setNumber(number);
            } else {
                throw new LdoDException("error in manual virtual edition reordering for xmlId " + usedXmlId);
            }

        }
    }

    // Default section
    @Atomic(mode = TxMode.WRITE)
    public VirtualEditionInter createVirtualEditionInter(VirtualEditionInter inter, int number) {
        // logger.debug("createVirtualEditionInter inter:{}, number:{}", inter, number);
        VirtualEditionInter virtualInter = null;

        if (canAddFragInter(inter.getLastUsed())) {
            if (getSectionsSet().isEmpty()) {
                Section section = new Section(this, Section.DEFAULT, 0);
                virtualInter = new VirtualEditionInter(section, inter, number);
                section.addVirtualEditionInter(virtualInter);
                addSections(section);
            } else {
                Section section = getSectionsSet().iterator().next();
                virtualInter = new VirtualEditionInter(section, inter, number);
            }
        }

        return virtualInter;
    }

    // Default section
    @Atomic(mode = TxMode.WRITE)
    public VirtualEditionInter createVirtualEditionInter(ScholarInterDto scholarInterDto, int number) {
        // logger.debug("createVirtualEditionInter inter:{}, number:{}", inter, number);
        VirtualEditionInter virtualInter = null;

        if (canAddFragInter(scholarInterDto)) {
            if (getSectionsSet().isEmpty()) {
                Section section = new Section(this, Section.DEFAULT, 0);
                virtualInter = new VirtualEditionInter(section, scholarInterDto, number);
                section.addVirtualEditionInter(virtualInter);
                addSections(section);
            } else {
                Section section = getSectionsSet().iterator().next();
                virtualInter = new VirtualEditionInter(section, scholarInterDto, number);
            }
        }
        return virtualInter;
    }

    public boolean isPublicOrIsParticipant(String username) {
        return getPub() || getParticipantSet().contains(username);
    }

    public List<VirtualEditionInter> getAllDepthVirtualEditionInters() {
        List<VirtualEditionInter> inters = new ArrayList<>();
        for (Section section : getSectionsSet()) {
            inters.addAll(section.getAllDepthVirtualEditionInterSet());
        }
        Collections.sort(inters);
        return inters;
    }

    @Atomic(mode = TxMode.WRITE)
    public Section createSection(String title, int number) {
        Section section = new Section(this, title, number);
        return section;
    }

    public boolean hasMultipleSections() {
        return getSectionsSet().size() > 1;
    }

    public Section getSection(String title) {
        for (Section section : getSectionsSet()) {
            if (section.getTitle().equals(title)) {
                return section;
            }
        }
        return null;
    }

    public Section createSection(String title) {
        int number = getSectionsSet().size();
        return createSection(title, number);
    }

    public int getSectionDepth() {
        int max = 0;
        for (Section section : getSectionsSet()) {
            int depth = section.getDepth();
            if (max < depth) {
                max = depth;
            }
        }
        return max;
    }

    public List<Section> getSortedSections() {
        List<Section> sortedList = new ArrayList<>(getSectionsSet());
        Collections.sort(sortedList);
        return sortedList;
    }

    @Atomic(mode = TxMode.WRITE)
    public void clearEmptySections() {
        for (Section section : getSectionsSet()) {
            section.clearEmptySections();
        }

        for (Section section : getSectionsSet()) {
            if (section.getAllDepthVirtualEditionInterSet().size() == 0) {
                section.remove();
            }
        }
    }

    public boolean isSelectedBy(String user) {
        return getSelectedBySet().stream().anyMatch(selectedBy -> selectedBy.getUser().equals(user));
    }

    @Atomic(mode = TxMode.WRITE)
    public void addSelectedByUser(String user) {
        if (!isSelectedBy(user)) {
            new SelectedBy(this, user);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void removeSelectedByUser(String user) {
        getSelectedBySet().stream().filter(selectedBy -> selectedBy.equals(user)).forEach(selectedBy -> selectedBy.remove());
    }

    @Atomic(mode = TxMode.WRITE)
    public void removeMember(String user) {
        getMemberSet().stream().filter(m -> m.getUser().equals(user)).forEach(m -> m.remove());
        removeSelectedByUser(user);
    }

    public Member getMember(String user) {
        return getMemberSet().stream().filter(m -> m.getUser().equals(user)).findFirst().orElse(null);
    }

    @Atomic(mode = TxMode.WRITE)
    public void addMember(String user, Member.MemberRole role, boolean active) {
        if (!getMemberSet().stream().filter(m -> m.getUser().equals(user)).findAny().isPresent()) {
            new Member(this, user, role, active);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void cancelParticipationSubmission(String user) {
        Member member = getMemberSet().stream().filter(m -> !m.getActive() && m.getUser().equals(user)).findAny()
                .orElse(null);
        if (member != null) {
            member.remove();
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void addApprove(String user) {
        Member member = getMemberSet().stream().filter(m -> !m.getActive() && m.getUser().equals(user)).findAny()
                .orElse(null);
        if (member != null) {
            member.setActive(true);
        }
    }

    @Atomic(mode = TxMode.WRITE)
    public void switchRole(String user) {
        Member member = getMemberSet().stream().filter(m -> m.getUser().equals(user)).findFirst().orElse(null);
        if (member != null) {
            if (member.getRole().equals(Member.MemberRole.ADMIN)) {
                member.setRole(Member.MemberRole.MEMBER);
            } else {
                member.setRole(Member.MemberRole.ADMIN);
            }
        }
    }

    public Set<String> getParticipantSet() {
        return getMemberSet().stream().filter(m -> m.getActive()).map(m -> m.getUser()).collect(Collectors.toSet());
    }

    public List<UserDto> getParticipantList() {
        return getParticipantSet().stream().map(participant -> new UserDto(participant)).sorted(Comparator.comparing(UserDto::getFirstName))
                .collect(Collectors.toList());
    }

    public Set<Member> getActiveMemberSet() {
        return getMemberSet().stream().filter(m -> m.getActive()).collect(Collectors.toSet());
    }

    public Set<String> getAdminSet() {
        return getMemberSet().stream()
                .filter(member -> member.getRole().equals(Member.MemberRole.ADMIN) && member.getActive())
                .map(member -> member.getUser())
                .collect(Collectors.toSet());
    }

    public Set<Member> getAdminMemberSet() {
        return getMemberSet().stream().filter(m -> m.getRole().equals(Member.MemberRole.ADMIN) && m.getActive())
                .collect(Collectors.toSet());
    }

    public Set<UserDto> getPendingSet() {
        return getMemberSet().stream().filter(m -> !m.getActive()).map(m -> new UserDto(m.getUser())).collect(Collectors.toSet());
    }

    public Set<Member> getPendingMemberSet() {
        return getMemberSet().stream().filter(m -> !m.getActive()).collect(Collectors.toSet());
    }

    public boolean canRemoveMember(String actor, String user) {
        Member.MemberRole roleActor = getMemberSet().stream().filter(m -> m.getUser().equals(actor)).map(m -> m.getRole())
                .findFirst().get();

        if (roleActor.equals(Member.MemberRole.ADMIN) && getAdminMemberSet().size() > 1) {
            return true;
        }

        if (roleActor.equals(Member.MemberRole.ADMIN) && getAdminMemberSet().size() == 1 && !actor.equals(user)) {
            return true;
        }

        if (roleActor.equals(Member.MemberRole.MEMBER) && actor.equals(user)) {
            return true;
        }

        return false;
    }

    public boolean canSwitchRole(String actor, String user) {
        Member.MemberRole roleActor = getMemberSet().stream().filter(m -> m.getUser().equals(actor)).map(m -> m.getRole())
                .findAny().get();

        if (roleActor.equals(Member.MemberRole.ADMIN) && getAdminMemberSet().size() > 1) {
            return true;
        }

        if (roleActor.equals(Member.MemberRole.ADMIN) && getAdminMemberSet().size() == 1 && !actor.equals(user)) {
            return true;
        }

        return false;
    }

    public Set<Category> getAllDepthCategories() {
        Set<Category> result = new HashSet<>(getTaxonomy().getCategoriesSet());

        VirtualEdition usedEdition = getUses();
        while (usedEdition != null) {
            result.addAll(usedEdition.getTaxonomy().getCategoriesSet());
            usedEdition = usedEdition.getUses();
        }

        return result;
    }

    public List<Category> getAllDepthSortedCategories() {
        return getAllDepthCategories().stream().sorted(Comparator.comparing(Category::getName))
                .collect(Collectors.toList());
    }

    // Foi alterado por causa das human annotations
    public List<HumanAnnotation> getAnnotationList() {
        return getAllDepthVirtualEditionInters().stream().flatMap(i -> i.getAnnotationSet().stream())
                .filter(HumanAnnotation.class::isInstance).map(HumanAnnotation.class::cast)
                .collect(Collectors.toList());
    }

    public List<String> getAnnotationTextList() {
        return getAnnotationList().stream().filter(a -> a.getText() != null && !a.getText().isEmpty())
                .map(a -> a.getText()).sorted().collect(Collectors.toList());
    }

    public boolean isSAVE() {
        // old version
        // if (!this.getCriteriaSet().isEmpty()) {
        // return true;
        // }
        // return false;

        // new version
        if (this.getMediaSource() != null && !this.getMediaSource().getName().equals("noMediaSource")) {
            return true;
        }
        return false;
    }

    public MediaSource getMediaSource() {
        for (SocialMediaCriteria criteria : this.getCriteriaSet()) {
            if (criteria instanceof MediaSource) {
                return (MediaSource) criteria;
            }
        }
        return null;
    }

    public TimeWindow getTimeWindow() {
        for (SocialMediaCriteria criteria : this.getCriteriaSet()) {
            if (criteria instanceof TimeWindow) {
                return (TimeWindow) criteria;
            }
        }
        return null;
    }

    public GeographicLocation getGeographicLocation() {
        for (SocialMediaCriteria criteria : this.getCriteriaSet()) {
            if (criteria instanceof GeographicLocation) {
                return (GeographicLocation) criteria;
            }
        }
        return null;
    }

    public Frequency getFrequency() {
        for (SocialMediaCriteria criteria : this.getCriteriaSet()) {
            if (criteria instanceof Frequency) {
                return (Frequency) criteria;
            }
        }
        return null;
    }

    public boolean isLdoDEdition() {
        return getAcronym().equals(VirtualEdition.ARCHIVE_EDITION_ACRONYM);
    }


    @Atomic(mode = TxMode.WRITE)
    public void save(List<VirtualEditionInter> virtualEditionInters) {
        Section section = createSection(Section.DEFAULT);
        int i = 0;
        for (VirtualEditionInter virtualEditionInter : virtualEditionInters) {
            section.addVirtualEditionInter(virtualEditionInter, ++i);
        }
        clearEmptySections();
    }
}