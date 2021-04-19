package pt.ist.socialsoftware.edition.ldod.frontend.reactUi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.FenixFramework;

import pt.ist.socialsoftware.edition.ldod.frontend.domain.EditionModule;
import pt.ist.socialsoftware.edition.ldod.frontend.domain.Menu;
import pt.ist.socialsoftware.edition.ldod.frontend.domain.Option;
import pt.ist.socialsoftware.edition.ldod.frontend.text.FeTextRequiresInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.user.FeUserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.AnnotationDTO;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.AnnotationSearchJson;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.LdoDException;
import pt.ist.socialsoftware.edition.ldod.frontend.utils.enums.Source_Type;
import pt.ist.socialsoftware.edition.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.virtual.api.dto.*;

import pt.ist.socialsoftware.edition.virtual.api.textdto.*;
import pt.ist.socialsoftware.edition.virtual.utils.CategoryDTO;


import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;



@RestController
@CrossOrigin(methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.PUT})
@RequestMapping("/api/services/frontend")
public class ReactUiController {
    private static final Logger logger = LoggerFactory.getLogger(ReactUiController.class);

    private final FeTextRequiresInterface feTextRequiresInterface = new FeTextRequiresInterface();
    private final FeUserProvidesInterface feUserProvidesInterface = new FeUserProvidesInterface();
    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();
    private final VirtualProvidesInterface virtualProvidesInterface = new VirtualProvidesInterface();

    @Inject
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/module-info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getModuleInfo() {

        List<String> moduleNames = FenixFramework.getDomainRoot().getModuleSet()
                .stream().filter(editionModule -> !editionModule.getName().equals("Custom"))
                .map(EditionModule::getName).collect(Collectors.toList());

        return new ResponseEntity<>(moduleNames, HttpStatus.OK);
    }

    @GetMapping(value = "/topbar-config", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getTopBarConfig() {
        Set<EditionModule> moduleSet = FenixFramework.getDomainRoot().getModuleSet();

        List<Map<String, List<AbstractMap.SimpleEntry<String, String>>>> results = new ArrayList<>();

        EditionModule customModule = FenixFramework.getDomainRoot().getModuleSet().stream()
                .filter(editionModule -> editionModule.getName().equals("Custom")).findAny().orElse(null);

        if (customModule != null) {
            Map<String, List<AbstractMap.SimpleEntry<String, String>>> temp = new LinkedHashMap<>();
            for (Menu menu : customModule.getUiComponent().getMenuSet().stream().sorted(Comparator.comparingInt(Menu::getPosition)).collect(Collectors.toList())) {
                List<AbstractMap.SimpleEntry<String, String>> links = menu.getOptionSet().stream().sorted(Comparator.comparingInt(Option::getPosition))
                        .map(option -> new AbstractMap.SimpleEntry<>(option.getName(), option.getLink())).collect(Collectors.toList());
                temp.put(menu.getName(), links);
            }
            results.add(temp);
        } else {
            for (EditionModule module : moduleSet) {
                Map<String, List<AbstractMap.SimpleEntry<String, String>>> temp = new LinkedHashMap<>();
                for (Menu menu : module.getUiComponent().getMenuSet().stream().sorted(Comparator.comparingInt(Menu::getPosition)).collect(Collectors.toList())) {
                    List<AbstractMap.SimpleEntry<String, String>> links = menu.getOptionSet().stream().sorted(Comparator.comparingInt(Option::getPosition))
                            .map(option -> new AbstractMap.SimpleEntry<>(option.getName(), option.getLink())).collect(Collectors.toList());
                    temp.put(menu.getName(), links);
                }
                results.add(temp);
            }
        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping(value = "/frag-info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFragmentInfo(@RequestParam String xmlId) {

        FragmentDto fragment = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, String> fragInfo = new LinkedHashMap<>();

        fragInfo.put("xmlId", fragment.getXmlId());
        fragInfo.put("title", fragment.getTitle());

        return new ResponseEntity<>(fragInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/expert-inter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFragmentExpertInterInfo(@RequestParam String xmlId) {

        FragmentDto fragment = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<ScholarInterDto> expertEditionInters = fragment.getScholarInterDtoSet().stream()
                .filter(scholarInterDto -> !scholarInterDto.isSourceInter()).collect(Collectors.toList());

        Map<String, List<Map<String, String>>> interInfo = new LinkedHashMap<>();

        for (ScholarInterDto expertEditionInter : expertEditionInters) {
            Map<String, String> info = new LinkedHashMap<>();
            info.put("xmlId", expertEditionInter.getXmlId());
            info.put("title", expertEditionInter.getTitle());
            info.put("number", Integer.toString(expertEditionInter.getNumber()));
            info.put("urlId", expertEditionInter.getUrlId());
            info.put("externalId", expertEditionInter.getExternalId());
            info.put("nextXmlId", expertEditionInter.getNextScholarInter().getFragmentDto().getXmlId());
            info.put("nextUrlId", expertEditionInter.getNextScholarInter().getUrlId());
            info.put("prevXmlId", expertEditionInter.getPrevScholarInter().getFragmentDto().getXmlId());
            info.put("prevUrlId", expertEditionInter.getPrevScholarInter().getUrlId());

            if (interInfo.containsKey(expertEditionInter.getExpertEditionAcronym())) {
                interInfo.get(expertEditionInter.getExpertEditionAcronym()).add(info);
            } else {
                List<Map<String, String>> infoList = new ArrayList<>();
                infoList.add(info);
                interInfo.put(expertEditionInter.getExpertEditionAcronym(), infoList);
            }
        }

        return new ResponseEntity<>(interInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/source-inter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFragmentSourceInterInfo(@RequestParam String xmlId) {

        FragmentDto fragment = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Set<Map<String, String>> interInfo = new LinkedHashSet<>();

        Set<ScholarInterDto> sourceDtos = fragment.getScholarInterDtoSet().stream()
                .filter(ScholarInterDto::isSourceInter).collect(Collectors.toSet());


        for (ScholarInterDto sourceInter : sourceDtos) {
            Map<String, String> info = new LinkedHashMap<>();
            info.put("xmlId", sourceInter.getXmlId());
            info.put("shortName", sourceInter.getShortName());
            info.put("urlId", sourceInter.getUrlId());
            info.put("externalId", sourceInter.getExternalId());

            interInfo.add(info);
        }

        return new ResponseEntity<>(interInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/expert-edition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getExpertEditionInfo() {
        List<AbstractMap.SimpleEntry<String, String>> expertEditionInfo = this.feTextRequiresInterface.getSortedExpertEditionsDto().stream()
                .map(expertEdition -> new AbstractMap.SimpleEntry<>(expertEdition.getAcronym(), expertEdition.getEditor())).collect(Collectors.toList());

        return new ResponseEntity<>(expertEditionInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/inter-writer", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> getInterWriterResult(@RequestParam String xmlId, @RequestParam String urlId) {

        FragmentDto fragment = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Fragment not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ScholarInterDto scholarInter = fragment.getScholarInterDtoByUrlId(urlId);

        String result;

        if (scholarInter != null) {
            result = scholarInter.getTranscription();
        } else {
            VirtualEditionInterDto virtualEditionInter = this.virtualProvidesInterface.getVirtualEditionInterSet().stream()
                    .filter(dto -> dto.getUrlId().equals(urlId)).findAny().orElse(null);
            if (virtualEditionInter == null) {
                logger.debug("Inter not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            scholarInter = this.feTextRequiresInterface.getScholarInterByXmlId(virtualEditionInter.getUsesScholarInterId());
            result = scholarInter.getTranscription();
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/meta-info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getInterMetaInfo(@RequestParam String xmlId, @RequestParam String urlId) {

       // Fragment fragment = TextModule.getInstance().getFragmentByXmlId(xmlId);
        FragmentDto fragment = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        //ScholarInter scholarInter = fragment.getScholarInterByUrlId(urlId);
        ScholarInterDto scholarInter = fragment.getScholarInterByUrlId(urlId);

        if (scholarInter == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, Object> metaInfo = new LinkedHashMap<>();

        ScholarInterDto scholarInterDto = this.feTextRequiresInterface.getScholarInterByXmlId(scholarInter.getXmlId());
        SourceDto sourceDto = scholarInterDto.isSourceInter() ? scholarInterDto.getSourceDto() : null;

        boolean isEditorial = !scholarInterDto.isSourceInter();
        boolean isManuscript = scholarInterDto.isSourceInter()
                && sourceDto.getType().getDesc().equals(Source_Type.MANUSCRIPT.getDesc());
        boolean isPublication = scholarInterDto.isSourceInter()
                && sourceDto.getType().getDesc().equals(Source_Type.PRINTED.getDesc());

        if (isEditorial) {
            metaInfo.put("title", scholarInterDto.getTitle());
        } else if (isPublication) {
            metaInfo.put("title", sourceDto.getTitle());
        } else {
            metaInfo.put("title", "");
        }


        if (isManuscript) {
            metaInfo.put("idno", sourceDto.getIdno());
        }


        metaInfo.put("heteronym", scholarInterDto.getHeteronym().getName());

        if (isManuscript) {
            metaInfo.put("dimension", sourceDto.getFormattedDimensions());

            metaInfo.put("material", sourceDto.getMaterial());

            metaInfo.put("columns", sourceDto.getColumns());

            metaInfo.put("ldoDKey", sourceDto.getHasLdoDLabel());

            metaInfo.put("handNotes", sourceDto.getFormattedHandNote());

            metaInfo.put("typeNotes", sourceDto.getFormattedTypeNote());
        }

        if (isEditorial) {
            metaInfo.put("volume", scholarInterDto.getVolume());
        }

        if (isPublication) {
            metaInfo.put("journal", sourceDto.getJournal());
        }


        if (isEditorial) {
            metaInfo.put("number", scholarInterDto.getCompleteNumber());
        } else if (isPublication) {
            metaInfo.put("number", sourceDto.getIssue());
        } else {
            metaInfo.put("number", "");
        }

        if (isEditorial) {
            metaInfo.put("startPage", scholarInterDto.getStartPage());
            metaInfo.put("endPage", scholarInterDto.getEndPage());
        } else if (isPublication) {
            metaInfo.put("startPage", sourceDto.getStartPage());
            metaInfo.put("endPage", sourceDto.getEndPage());
        } else {
            metaInfo.put("startPage", 0);
        }

        if (isPublication) {
            metaInfo.put("pubPlace", sourceDto.getPubPlace());
        }

        if (isEditorial) {
            LdoDDateDto ldoDDate = scholarInterDto.getLdoDDate();
            metaInfo.put("date", (ldoDDate != null ? ldoDDate.print() : ""));
            if (ldoDDate != null && ldoDDate.getPrecision() != null) {
                metaInfo.put("datePrecision", ldoDDate.getPrecision().getDesc());
            }
        } else {
            LdoDDateDto ldoDDate = sourceDto.getLdoDDateDto();
            metaInfo.put("date", (ldoDDate != null ? ldoDDate.print() : ""));
            if (ldoDDate != null && ldoDDate.getPrecision() != null) {
                metaInfo.put("datePrecision", ldoDDate.getPrecision().getDesc());
            }
        }

        if (isEditorial) {
            metaInfo.put("notes", scholarInterDto.getNotes());
        } else if (isManuscript) {
            metaInfo.put("notes", sourceDto.getNotes());
        } else {
            metaInfo.put("notes", "");
        }

        List<String> notes = scholarInterDto.getSortedAnnexNote().stream().map(AnnexNoteDto::getText)
                .collect(Collectors.toList());
        metaInfo.put("annexNotes", notes);

        if (isManuscript || isPublication) {
            List<AbstractMap.SimpleEntry<String, String>> surfaces = sourceDto.getSurfaces().stream()
                    .map(surface -> new AbstractMap.SimpleEntry<>("/facs/" + surface.getGraphic(), sourceDto.getAltIdentifier())).collect(Collectors.toList());
            metaInfo.put("surfaces", surfaces);
        }

        return new ResponseEntity<>(metaInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/source-writer", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> getSourceWriterWithOptions(@RequestParam String xmlId, @RequestParam String urlId,
                                                        @RequestParam boolean diff,
                                                        @RequestParam boolean del, @RequestParam boolean ins,
                                                        @RequestParam boolean subst, @RequestParam boolean notes) {

        FragmentDto fragment = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could not find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ScholarInterDto inter = fragment.getScholarInterDtoByUrlId(urlId);

        if (inter == null || !inter.isSourceInter()) {
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String result = inter.getSourceTranscription(diff, del, ins, subst, notes);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/expert-writer", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> getExpertWriterWithOptions(@RequestParam String xmlId, @RequestParam String urlId,
                                                        @RequestParam boolean diff) {

        FragmentDto fragment = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could not find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ScholarInterDto inter = fragment.getScholarInterDtoByUrlId(urlId);

        if (inter == null || inter.isSourceInter()) {
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String result = inter.getExpertTranscription(diff);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/fac-urls", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFacsForSourceInter(@RequestParam String xmlId, @RequestParam String urlId) {

        FragmentDto fragment = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ScholarInterDto inter = fragment.getScholarInterDtoByUrlId(urlId);

        if (inter == null || !inter.isSourceInter()) {
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<String> urls = this.feTextRequiresInterface.getSourceInterFacUrls(inter.getXmlId());

        return new ResponseEntity<>(urls, HttpStatus.OK);
    }


    @GetMapping(value = "/virtual-inter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFragmentVirtualInterInfo(@RequestParam String xmlId) {

        FragmentDto fragment = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, List<Map<String, String>>> interInfo = new LinkedHashMap<>();


        //TODO : delete the following assignment and uncomment the following lines when
        // login capabilities are added to react frontend and a way to select virtual editions is possible
        Set<VirtualEditionDto> virtualEditions = this.virtualProvidesInterface.getVirtualEditions();

        /*Set<VirtualEdition> virtualEditions = new LinkedHashSet<>(FrontendSession.getFrontendSession().materializeVirtualEditions());

        virtualEditions.addAll((User.getAuthenticatedUser() != null ? User.getAuthenticatedUser().getSelectedVirtualEditionsSet() : new LinkedHashSet<>()));

        virtualEditions.add(VirtualModule.getInstance().getArchiveEdition());

        logger.debug(String.valueOf(virtualEditions.size()));*/

        for (VirtualEditionDto virtualEdition : virtualEditions) {

            Set<VirtualEditionInterDto> editionInters = virtualEdition.getVirtualEditionInterOfFragmentForVirtualEdition(fragment.getXmlId());

            List<Map<String, String>> data = new ArrayList<>();
            for (VirtualEditionInterDto vei : editionInters) {
                Map<String, String> info = new LinkedHashMap<>();
                info.put("xmlId", vei.getXmlId());
                info.put("title", vei.getTitle());
                info.put("number", Integer.toString(vei.getNumber()));
                info.put("urlId", vei.getUrlId());
                info.put("externalId", vei.getExternalId());
                info.put("nextXmlId", vei.getNextInter().getFragmentXmlId());
                info.put("nextUrlId", vei.getNextInter().getUrlId());
                info.put("prevXmlId", vei.getPrevInter().getFragmentXmlId());
                info.put("prevUrlId", vei.getPrevInter().getUrlId());
                data.add(info);
            }
            interInfo.put(virtualEdition.getAcronym(), data);
        }

        return new ResponseEntity<>(interInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/virtual-edition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFragmentVirtualInterInfo(@RequestParam String xmlId, @RequestParam String urlId) {

        FragmentDto fragment = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VirtualEditionInterDto inter = this.virtualProvidesInterface.getVirtualEditionInterSet().stream()
                .filter(virtualEditionInter -> virtualEditionInter.getUrlId().equals(urlId)).findFirst().orElse(null);

        if (inter == null) {
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, String> editionInfo = new LinkedHashMap<>();

        VirtualEditionDto virtualEdition = this.virtualProvidesInterface.getVirtualEditions().stream()
                .filter(virtualEditionDto -> this.virtualProvidesInterface.isInterInVirtualEdition(inter.getXmlId(), virtualEditionDto.getAcronym()))
                .findAny().orElse(null);

        editionInfo.put("editionTitle", virtualEdition.getTitle());
        editionInfo.put("externalId", inter.getExternalId());

        if (inter.getUsesInter() != null) {
            editionInfo.put("editionReference", virtualEdition.getReference());
            editionInfo.put("interReference", inter.getUsesInter().getReference());
        } else {
            ScholarInterDto scholarInter = this.feTextRequiresInterface.getScholarInterByXmlId(inter.getUsesScholarInterId());
            editionInfo.put("editionReference", scholarInter.getEditionReference());
            editionInfo.put("interReference", scholarInter.getReference());
        }

        editionInfo.put("openVocabulary", String.valueOf(virtualEdition.getTaxonomyVocabularyStatus()));

        return new ResponseEntity<>(editionInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/taxonomy", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getTaxonomyForInter(@RequestParam String xmlId, @RequestParam String urlId) {

        FragmentDto fragment = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VirtualEditionInterDto inter = this.virtualProvidesInterface.getVirtualEditionInterSet().stream()
                .filter(virtualEditionInter -> virtualEditionInter.getUrlId().equals(urlId)).findFirst().orElse(null);

        if (inter == null) {
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> categoryInfo = new ArrayList<>();

        // TODO: Nuno check if this is a correct interpretation of what is needed here
        for (CategoryDto category : inter.getCategoriesUsedInTags(this.feUserProvidesInterface.getAuthenticatedUser())) {
            Map<String, Object> infoMap = new LinkedHashMap<>();
            infoMap.put("interExternal", inter.getExternalId());
            infoMap.put("acronym", category.getAcronym());
            infoMap.put("urlId", category.getUrlId());
            infoMap.put("name", category.getNameInEdition());
            infoMap.put("categoryExternal", category.getExternalId());

            List<Map<String, String>> userList = new ArrayList<>();
            category.getUsers().forEach(userDto -> {
                Map<String, String> userInfo = new LinkedHashMap<>();
                userInfo.put("username", userDto.getUsername());
                userInfo.put("firstName", userDto.getFirstName());
                userInfo.put("lastName", userDto.getLastName());
                userList.add(userInfo);
            });
            infoMap.put("users", userList);

            categoryInfo.add(infoMap);
        }

        return new ResponseEntity<>(categoryInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getCategoriesForInter(@RequestParam String xmlId, @RequestParam String urlId) {

        FragmentDto fragment = this.feTextRequiresInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VirtualEditionInterDto inter = this.virtualProvidesInterface.getVirtualEditionInterSet().stream()
                .filter(virtualEditionInter -> virtualEditionInter.getUrlId().equals(urlId)).findFirst().orElse(null);

        if (inter == null) {
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, Object> catInfo = new LinkedHashMap<>();

        String username = this.feUserProvidesInterface.getAuthenticatedUser();

        if (this.userProvidesInterface.getUser(username) != null) {
            List<String> assignedInfo = new ArrayList<>();
            for (CategoryDto category : inter.getAllDepthCategoriesUsedByUserInTags(username)) {
                assignedInfo.add(category.getNameInEdition());
            }

            catInfo.put("assigned", assignedInfo);

            List<String> nonAssignedInfo = new ArrayList<>();
            for (CategoryDto category : inter.getAllDepthCategoriesNotUsedInTags(username)) {
                nonAssignedInfo.add(category.getNameInEdition());
            }

            catInfo.put("nonAssigned", nonAssignedInfo);
        }

        return new ResponseEntity<>(catInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/multiple-writer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getMultipleInterWriter(@RequestParam String interIds,
                                                    @RequestParam(required = false, defaultValue = "false") boolean lineByLine,
                                                    @RequestParam(required = false, defaultValue = "false") boolean showSpaces) {
        List<ScholarInterDto> interDtos = new ArrayList<>();

        for (String id : interIds.split("%2C")) {
            ScholarInterDto dto = this.feTextRequiresInterface.getScholarInterByExternalId(id);
            if (dto != null) {
                interDtos.add(dto);
            }
        }

        if (interDtos.size() == 1) {
            return new ResponseEntity<>(interDtos.get(0).getTranscription(), HttpStatus.OK);
        }

        if (interDtos.size() > 2) {
            lineByLine = true;
        }

        Map<String, Object> results = new LinkedHashMap<>();

        Map<String, String> transcriptions = this.feTextRequiresInterface.getMultipleInterTranscription(Arrays.asList(interIds.split("%2C")), lineByLine, showSpaces);

        if (!lineByLine) {
            for (ScholarInterDto inter : interDtos) {
                Map<String, String> interInfo = new LinkedHashMap<>();
                interInfo.put("transcription", transcriptions.get(inter.getExternalId()));
                interInfo.put("urlId", inter.getUrlId());
                interInfo.put("fragId", inter.getFragmentDto().getXmlId());
                results.put(inter.getExternalId(), interInfo);
            }
        } else {
            results.put("transcription", transcriptions.get("transcription"));
        }

        results.put("variations", this.feTextRequiresInterface.getMultipleInterVariations(Arrays.asList(interIds.split("%2C"))));
        results.put("title", interDtos.get(0).getTitle());
        results.put("lineByLine", lineByLine);
        results.put("showSpaces", showSpaces);

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping(value = "/multiple-virtual", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getMultipleVirtualInfo(@RequestParam String interIds) {

        List<VirtualEditionInterDto> inters = new ArrayList<>();

        for (String id : interIds.split("%2C")) {
            VirtualEditionInterDto vei = this.virtualProvidesInterface.getVirtualEditionInterByExternalId(id);
            if (vei != null) {
                inters.add(vei);
            }
        }

        Map<String, Object> results = new LinkedHashMap<>();

        for (VirtualEditionInterDto inter : inters) {
            Map<String, Object> info = new LinkedHashMap<>();

            VirtualEditionDto virtualEditionDto = this.virtualProvidesInterface.getVirtualEditions().stream()
                    .filter(dto -> this.virtualProvidesInterface.isInterInVirtualEdition(inter.getXmlId(), dto.getAcronym()))
                    .findAny().orElseThrow(LdoDException::new);

            info.put("reference", virtualEditionDto.getReference());

            List<Object> tags = new ArrayList<>();
            // TODO: Nuno, check if the new name makes sense in this context
            for (TagDto tag : inter.getAllDepthTagsNotHumanAnnotationAccessibleByUser(this.feUserProvidesInterface.getAuthenticatedUser())) {
                Map<String, String> tagInfo = new LinkedHashMap<>();
                tagInfo.put("username", tag.getUsername());
                tagInfo.put("acronym", tag.getAcronym());
                tagInfo.put("urlId", tag.getUrlId());
                tagInfo.put("name", tag.getNameInEdition());
                tags.add(tagInfo);
            }
            info.put("tags", tags);

            List<Object> annotations = new ArrayList<>();

            for (HumanAnnotationDto annotation : inter.getVirtualEditionInterHumanAnnotationsAccessibleByUser(this.feUserProvidesInterface.getAuthenticatedUser())) {
                Map<String, Object> annotationInfo = new LinkedHashMap<>();
                annotationInfo.put("quote", annotation.getQuote());
                annotationInfo.put("text", annotation.getText());

                List<Object> annotationTags = new ArrayList<>();
                for (TagDto tag : annotation.getTags()) {
                    Map<String, String> tagInfo = new LinkedHashMap<>();
                    tagInfo.put("acronym", tag.getAcronym());
                    tagInfo.put("urlId", tag.getUrlId());
                    tagInfo.put("name", tag.getNameInEdition());
                    annotationTags.add(tagInfo);
                }

                annotationInfo.put("tags", annotationTags);
                annotationInfo.put("username", annotation.getUsername());

                annotations.add(annotationInfo);
            }

            for (AwareAnnotationDto annotation : inter.getVirtualEditionInterAwareAnnotationsAccessibleByUser(this.feUserProvidesInterface.getAuthenticatedUser())) {
                Map<String, Object> annotationInfo = new LinkedHashMap<>();
                annotationInfo.put("quote", annotation.getQuote());
                annotationInfo.put("source", annotation.getSource());
                annotationInfo.put("profile", annotation.getProfile());
                annotationInfo.put("date", annotation.getDate());
                annotationInfo.put("country", annotation.getCountry());
                annotationInfo.put("username", annotation.getUsername());

                annotations.add(annotationInfo);
            }

            info.put("annotations", annotations);

            results.put(inter.getExternalId(), info);
        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping("/restricted/associate-category")
    public ResponseEntity<?> associateCategoriesToInter(@RequestParam String externalId, @RequestParam String categories) {
        String[] names = categories.split("%2C");

        this.virtualProvidesInterface.associateVirtualEditionInterCategories(externalId, this.feUserProvidesInterface.getAuthenticatedUser(), Arrays.stream(names).collect(Collectors.toSet()));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/restricted/dissociate-category")
    public ResponseEntity<?> dissociateCategoryFromInter(@RequestParam String externalId, @RequestParam String categoryId) {
        this.virtualProvidesInterface.dissociateVirtualEditionInterCategory(externalId, this.feUserProvidesInterface.getAuthenticatedUser(), categoryId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/restricted/change-password")
    public ResponseEntity<?> changeUserPassword(@RequestParam String username, @RequestParam String currentPassword,
                                                @RequestParam String newPassword, @RequestParam String retypedPassword) {

        if (username == null || currentPassword == null || newPassword == null || retypedPassword == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (newPassword.length() < 6 || retypedPassword.length() < 6 || !newPassword.equals(retypedPassword)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        UserDto user = this.userProvidesInterface.getUser(username);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        user.updatePassword(this.passwordEncoder, currentPassword, newPassword);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Annotation related methods

    @GetMapping("/inter/categories")
    public ResponseEntity<?> getCategoriesForInter(@RequestParam String id) {

        VirtualEditionInterDto inter = this.virtualProvidesInterface.getVirtualEditionInterByExternalId(id);

        if (inter == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ObjectMapper mapper = new ObjectMapper();

        List<CategoryDTO> categories = inter.getAllDepthCategoriesAccessibleByUser(this.feUserProvidesInterface.getAuthenticatedUser());

        try {
            return new ResponseEntity<>(mapper.writeValueAsString(categories), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new LdoDException("VirtualEditionInter::getAllDepthCategoriesJSON");
        }
    }

    @PostMapping("/restricted/annotations")
    public ResponseEntity<?> createAnnotation(@RequestBody AnnotationDTO annotationJson) {
        logger.debug("Got annotation for quote:");
        logger.debug(annotationJson.getQuote());

        VirtualEditionInterDto inter = this.virtualProvidesInterface.getVirtualEditionInterByExternalId(annotationJson.getUri());
        VirtualEditionDto virtualEdition = inter.getVirtualEditionDto();

        String username = this.feUserProvidesInterface.getAuthenticatedUser();

        HumanAnnotationDto annotation;
        if (HumanAnnotationDto.canCreate(virtualEdition.getAcronym(), username)) {
            annotation = inter.createHumanAnnotation(annotationJson.getQuote(), annotationJson.getText(), username,
                    annotationJson.getRanges(), annotationJson.getTags());

            annotationJson.setId(annotation.getExternalId());

            return new ResponseEntity<>(new AnnotationDTO(annotation), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/restricted/annotations/{id}")
    public ResponseEntity<?> getAnnotation(@PathVariable String id) {
       HumanAnnotationDto annotation = this.virtualProvidesInterface.getHumanAnnotationfromId(id);
        if (annotation != null) {
            return new ResponseEntity<>(new AnnotationDTO(annotation), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/restricted/annotations/{id}")
    public ResponseEntity<?> updateAnnotation(@PathVariable String id, @RequestBody AnnotationDTO annotationJson) {
          HumanAnnotationDto annotation = this.virtualProvidesInterface.getHumanAnnotationfromId(id);
        if (annotation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (annotation.canUpdate(id, this.feUserProvidesInterface.getAuthenticatedUser())) {
            annotation.update(annotationJson.getText(), annotationJson.getTags());
            return new ResponseEntity<>(new AnnotationDTO(annotation), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/restricted/annotations/{id}")
    public ResponseEntity<?> deleteAnnotation(@PathVariable String id) {

        HumanAnnotationDto annotation = this.virtualProvidesInterface.getHumanAnnotationfromId(id);

        if (annotation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (annotation.canDelete(this.feUserProvidesInterface.getAuthenticatedUser())) {
            annotation.remove();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/restricted/search")
    public AnnotationSearchJson searchAnnotations(@RequestParam String uri) {

        logger.debug("searchAnnotations uri: " + uri);
        List<AnnotationDTO> annotations = new ArrayList<>();

        VirtualEditionInterDto inter = this.virtualProvidesInterface.getVirtualEditionInterByExternalId(uri);

        for (AnnotationDto annotation : inter.getAllDepthAnnotationsAccessibleByUser(this.feUserProvidesInterface.getAuthenticatedUser())) {
            AnnotationDTO annotationJson = new AnnotationDTO(annotation);
            annotations.add(annotationJson);
        }

        return new AnnotationSearchJson(annotations);
    }
}
