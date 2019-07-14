package pt.ist.socialsoftware.edition.ldod.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.api.ui.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.api.ui.UiInterface;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.text.api.TextProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.text.api.dto.ScholarInterDto;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.HtmlWriter4Variations;
import pt.ist.socialsoftware.edition.ldod.text.feature.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.user.api.UserProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.user.api.dto.UserDto;
import pt.ist.socialsoftware.edition.ldod.utils.AnnotationDTO;
import pt.ist.socialsoftware.edition.ldod.utils.AnnotationSearchJson;
import pt.ist.socialsoftware.edition.ldod.utils.CategoryDTO;
import pt.ist.socialsoftware.edition.ldod.utils.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.virtual.api.VirtualProvidesInterface;
import pt.ist.socialsoftware.edition.ldod.virtual.api.dto.VirtualEditionInterDto;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

import static pt.ist.socialsoftware.edition.ldod.domain.Source.SourceType.MANUSCRIPT;
import static pt.ist.socialsoftware.edition.ldod.domain.Source.SourceType.PRINTED;

@RestController
@CrossOrigin(methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.PUT})
@RequestMapping("/api/services/frontend")
public class ReactUiController {
    private static final Logger logger = LoggerFactory.getLogger(ReactUiController.class);

    private final UserProvidesInterface userProvidesInterface = new UserProvidesInterface();
    private final TextProvidesInterface textProvidesInterface = new TextProvidesInterface();
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

        if(customModule != null){
            Map<String, List<AbstractMap.SimpleEntry<String, String>>> temp = new LinkedHashMap<>();
            for (Menu menu : customModule.getUiComponent().getMenuSet().stream().sorted(Comparator.comparingInt(Menu::getPosition)).collect(Collectors.toList())) {
                List<AbstractMap.SimpleEntry<String, String>> links = menu.getOptionSet().stream().sorted(Comparator.comparingInt(Option::getPosition))
                        .map(option -> new AbstractMap.SimpleEntry<>(option.getName(), option.getLink())).collect(Collectors.toList());
                temp.put(menu.getName(), links);
            }
            results.add(temp);
        }
        else {
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

        FragmentDto fragment = this.textProvidesInterface.getFragmentByXmlId(xmlId);

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

        FragmentDto fragment = this.textProvidesInterface.getFragmentByXmlId(xmlId);

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

        FragmentDto fragment = this.textProvidesInterface.getFragmentByXmlId(xmlId);

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
        List<AbstractMap.SimpleEntry<String, String>> expertEditionInfo = this.textProvidesInterface.getSortedExpertEditionsDto().stream()
                .map(expertEdition -> new AbstractMap.SimpleEntry<>(expertEdition.getAcronym(), expertEdition.getEditor())).collect(Collectors.toList());

        return new ResponseEntity<>(expertEditionInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/inter-writer", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> getInterWriterResult(@RequestParam String xmlId, @RequestParam String urlId) {

        FragmentDto fragment = this.textProvidesInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Fragment not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ScholarInterDto scholarInter = fragment.getScholarInterDtoByUrlId(urlId);

        String result;

        if (scholarInter != null) {
            result = this.textProvidesInterface.getScholarInterTranscription(scholarInter.getXmlId());
        } else {
            VirtualEditionInterDto virtualEditionInter = this.virtualProvidesInterface.getVirtualEditionInterSet().stream()
                    .filter(dto -> dto.getUrlId().equals(urlId)).findAny().orElse(null);
            if (virtualEditionInter == null) {
                logger.debug("Inter not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            result = this.textProvidesInterface.getScholarInterTranscription(virtualEditionInter.getUsesScholarInterId());
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/meta-info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getInterMetaInfo(@RequestParam String xmlId, @RequestParam String urlId) {

        Fragment fragment = TextModule.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ScholarInter scholarInter = fragment.getScholarInterByUrlId(urlId);

        if (scholarInter == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        Map<String, Object> metaInfo = new LinkedHashMap<>();

        UiInterface uiInterface = new UiInterface();

        boolean isEditorial = uiInterface.getSourceTypeOfInter(urlId.replace("_", ".")).equals(FragInterDto.InterType.EDITORIAL);
        boolean isManuscript = uiInterface.getSourceTypeOfInter(urlId.replace("_", ".")).equals(FragInterDto.InterType.AUTHORIAL)
                && ((SourceInter) scholarInter).getSource().getType().equals(MANUSCRIPT);
        boolean isPublication = uiInterface.getSourceTypeOfInter(urlId.replace("_", ".")).equals(FragInterDto.InterType.AUTHORIAL)
                && ((SourceInter) scholarInter).getSource().getType().equals(PRINTED);

        if (isEditorial) {
            metaInfo.put("title", scholarInter.getTitle());
        } else if (isPublication) {
            PrintedSource source = (PrintedSource) ((SourceInter) scholarInter).getSource();
            metaInfo.put("title", source.getTitle());
        } else {
            metaInfo.put("title", "");
        }


        if (isManuscript) {
            ManuscriptSource source = (ManuscriptSource) ((SourceInter) scholarInter).getSource();
            metaInfo.put("idno", source.getIdno());
        }


        metaInfo.put("heteronym", scholarInter.getHeteronym().getName());

        if (isManuscript) {
            ManuscriptSource source = (ManuscriptSource) ((SourceInter) scholarInter).getSource();

            String dimension = source.getDimensionsSet().stream().map(dimensions -> dimensions.getHeight() + "x" + dimensions.getWidth())
                    .collect(Collectors.joining(";"));
            metaInfo.put("dimension", dimension);

            metaInfo.put("material", source.getMaterial());

            metaInfo.put("columns", source.getColumns());

            metaInfo.put("ldoDKey", ((ManuscriptSource) ((SourceInter) scholarInter).getSource()).getHasLdoDLabel());

            List<AbstractMap.SimpleEntry<String, String>> handNotes = source.getHandNoteSet().stream().map(handNote ->
                    new AbstractMap.SimpleEntry<>((handNote.getMedium() != null ? handNote.getMedium().getDesc() : ""), handNote.getNote())).collect(Collectors.toList());

            metaInfo.put("handNotes", handNotes);

            List<AbstractMap.SimpleEntry<String, String>> typeNotes = source.getTypeNoteSet().stream().map(typeNote ->
                    new AbstractMap.SimpleEntry<>((typeNote.getMedium() != null ? typeNote.getMedium().getDesc() : ""), typeNote.getNote())).collect(Collectors.toList());

            metaInfo.put("typeNotes", typeNotes);
        }

        if (isEditorial) {
            metaInfo.put("volume", ((ExpertEditionInter) scholarInter).getVolume());
        }

        if (isPublication) {
            PrintedSource source = (PrintedSource) ((SourceInter) scholarInter).getSource();
            metaInfo.put("journal", source.getJournal());
        }


        if (isEditorial) {
            metaInfo.put("number", ((ExpertEditionInter) scholarInter).getCompleteNumber());
        } else if (isPublication) {
            PrintedSource source = (PrintedSource) ((SourceInter) scholarInter).getSource();
            metaInfo.put("number", source.getIssue());
        } else {
            metaInfo.put("number", "");
        }

        if (isEditorial) {
            metaInfo.put("startPage", ((ExpertEditionInter) scholarInter).getStartPage());
            metaInfo.put("endPage", ((ExpertEditionInter) scholarInter).getEndPage());
        } else if (isPublication) {
            PrintedSource source = (PrintedSource) ((SourceInter) scholarInter).getSource();
            metaInfo.put("startPage", source.getStartPage());
            metaInfo.put("endPage", source.getEndPage());
        } else {
            metaInfo.put("startPage", 0);
        }

        if (isPublication) {
            PrintedSource source = (PrintedSource) ((SourceInter) scholarInter).getSource();
            metaInfo.put("pubPlace", source.getPubPlace());
        }

        if (isEditorial) {
            LdoDDate ldoDDate = scholarInter.getLdoDDate();
            metaInfo.put("date", (ldoDDate != null ? ldoDDate.print() : ""));
            if (ldoDDate != null && ldoDDate.getPrecision() != null) {
                metaInfo.put("datePrecision", ldoDDate.getPrecision().getDesc());
            }
        } else {
            LdoDDate ldoDDate = ((SourceInter) scholarInter).getSource().getLdoDDate();
            metaInfo.put("date", (ldoDDate != null ? ldoDDate.print() : ""));
            if (ldoDDate != null && ldoDDate.getPrecision() != null) {
                metaInfo.put("datePrecision", ldoDDate.getPrecision().getDesc());
            }
        }

        if (isEditorial) {
            metaInfo.put("notes", ((ExpertEditionInter) scholarInter).getNotes());
        } else if (isManuscript) {
            ManuscriptSource source = (ManuscriptSource) ((SourceInter) scholarInter).getSource();
            metaInfo.put("notes", source.getNotes());
        } else {
            metaInfo.put("notes", "");
        }

        List<String> notes = scholarInter.getSortedAnnexNote().stream().map(annexNote -> annexNote.getNoteText().generatePresentationText())
                .collect(Collectors.toList());
        metaInfo.put("annexNotes", notes);

        if (isManuscript || isPublication) {
            Source source = ((SourceInter) scholarInter).getSource();
            List<AbstractMap.SimpleEntry<String, String>> surfaces = source.getFacsimile().getSurfaces().stream()
                    .map(surface -> new AbstractMap.SimpleEntry<>("/facs/" + surface.getGraphic(), source.getAltIdentifier())).collect(Collectors.toList());
            metaInfo.put("surfaces", surfaces);
        }

        return new ResponseEntity<>(metaInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/source-writer", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> getSourceWriterWithOptions(@RequestParam String xmlId, @RequestParam String urlId,
                                                        @RequestParam boolean diff,
                                                        @RequestParam boolean del, @RequestParam boolean ins,
                                                        @RequestParam boolean subst, @RequestParam boolean notes) {

        FragmentDto fragment = this.textProvidesInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could not find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ScholarInterDto inter = fragment.getScholarInterDtoByUrlId(urlId);

        if (inter == null || !inter.isSourceInter()) {
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String result = this.textProvidesInterface.getSourceInterTranscription(inter.getXmlId(),diff, del, ins, subst, notes);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/expert-writer", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> getExpertWriterWithOptions(@RequestParam String xmlId, @RequestParam String urlId,
                                                        @RequestParam boolean diff) {

        FragmentDto fragment = this.textProvidesInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could not find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ScholarInterDto inter = fragment.getScholarInterDtoByUrlId(urlId);

        if (inter == null || inter.isSourceInter()) {
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String result = this.textProvidesInterface.getExpertInterTranscription(inter.getXmlId(), diff);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/fac-urls", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFacsForSourceInter(@RequestParam String xmlId, @RequestParam String urlId) {

        FragmentDto fragment = this.textProvidesInterface.getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ScholarInterDto inter = fragment.getScholarInterDtoByUrlId(urlId);

        if (inter == null || !inter.isSourceInter()) {
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<String> urls = this.textProvidesInterface.getSourceInterFacUrls(inter.getXmlId());

        return new ResponseEntity<>(urls, HttpStatus.OK);
    }


    @GetMapping(value = "/virtual-inter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFragmentVirtualInterInfo(@RequestParam String xmlId) {

        Fragment fragment = TextModule.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, List<Map<String, String>>> interInfo = new LinkedHashMap<>();


        //TODO : delete the following assignment and uncomment the following lines when
        // login capabilities are added to react frontend and a way to select virtual editions is possible
        Set<VirtualEdition> virtualEditions = VirtualModule.getInstance().getVirtualEditionsSet();

        /*Set<VirtualEdition> virtualEditions = new LinkedHashSet<>(LdoDSession.getLdoDSession().materializeVirtualEditions());

        virtualEditions.addAll((User.getAuthenticatedUser() != null ? User.getAuthenticatedUser().getSelectedVirtualEditionsSet() : new LinkedHashSet<>()));

        virtualEditions.add(VirtualModule.getInstance().getArchiveEdition());

        logger.debug(String.valueOf(virtualEditions.size()));*/

        for (VirtualEdition virtualEdition : virtualEditions) {

            Set<VirtualEditionInter> editionInters = virtualEdition.getVirtualEditionInterSetForFragment(fragment.getXmlId());

            List<Map<String, String>> data = new ArrayList<>();
            for (VirtualEditionInter vei : editionInters) {
                Map<String, String> info = new LinkedHashMap<>();
                info.put("xmlId", vei.getXmlId());
                info.put("title", vei.getTitle());
                info.put("number", Integer.toString(vei.getNumber()));
                info.put("urlId", vei.getUrlId());
                info.put("externalId", vei.getExternalId());
                info.put("nextXmlId", vei.getNextNumberInter().getFragmentXmlId());
                info.put("nextUrlId", vei.getNextNumberInter().getUrlId());
                info.put("prevXmlId", vei.getPrevNumberInter().getFragmentXmlId());
                info.put("prevUrlId", vei.getPrevNumberInter().getUrlId());
                data.add(info);
            }
            interInfo.put(virtualEdition.getAcronym(), data);
        }

        return new ResponseEntity<>(interInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/virtual-edition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFragmentVirtualInterInfo(@RequestParam String xmlId, @RequestParam String urlId) {

        Fragment fragment = TextModule.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VirtualEditionInter inter = VirtualModule.getInstance().getVirtualEditionInterSet(fragment.getXmlId()).stream()
                .filter(virtualEditionInter -> virtualEditionInter.getUrlId().equals(urlId)).findFirst().orElse(null);

        if (inter == null) {
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, String> editionInfo = new LinkedHashMap<>();

        editionInfo.put("editionTitle", inter.getEdition().getTitle());
        editionInfo.put("externalId", inter.getExternalId());

        if (inter.getUses() != null) {
            editionInfo.put("editionReference", inter.getUses().getEdition().getReference());
            editionInfo.put("interReference", inter.getUses().getReference());
        } else {
            ScholarInter scholarInter = TextModule.getInstance().getScholarInterByXmlId(inter.getLastUsed().getXmlId());
            editionInfo.put("editionReference", scholarInter.getEdition().getReference());
            editionInfo.put("interReference", scholarInter.getReference());
        }

        editionInfo.put("openVocabulary", String.valueOf(inter.getEdition().getTaxonomy().getOpenVocabulary()));

        return new ResponseEntity<>(editionInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/taxonomy", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getTaxonomyForInter(@RequestParam String xmlId, @RequestParam String urlId) {

        Fragment fragment = TextModule.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VirtualEditionInter inter = VirtualModule.getInstance().getVirtualEditionInterSet(fragment.getXmlId()).stream()
                .filter(virtualEditionInter -> virtualEditionInter.getUrlId().equals(urlId)).findFirst().orElse(null);

        if (inter == null) {
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Map<String, Object>> categoryInfo = new ArrayList<>();

        for (Category category : inter.getAssignedCategories()) {
            Map<String, Object> infoMap = new LinkedHashMap<>();
            infoMap.put("interExternal", inter.getExternalId());
            infoMap.put("acronym", category.getTaxonomy().getEdition().getAcronym());
            infoMap.put("urlId", category.getUrlId());
            infoMap.put("name", category.getNameInEditionContext(inter.getEdition().getTaxonomy().getEdition()));
            infoMap.put("categoryExternal", category.getExternalId());

            List<Map<String, String>> userList = new ArrayList<>();
            inter.getContributorSet(category).stream().map(UserDto::new).forEach(userDto -> {
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

        Fragment fragment = TextModule.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VirtualEditionInter inter = VirtualModule.getInstance().getVirtualEditionInterSet(fragment.getXmlId()).stream()
                .filter(virtualEditionInter -> virtualEditionInter.getUrlId().equals(urlId)).findFirst().orElse(null);

        if (inter == null) {
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String, Object> catInfo = new LinkedHashMap<>();

        String user = this.userProvidesInterface.getAuthenticatedUser();

        if (this.userProvidesInterface.getUser(user) != null) {
            List<String> assignedInfo = new ArrayList<>();
            for (Category category : inter.getAssignedCategories(user)) {
                assignedInfo.add(category.getNameInEditionContext(inter.getEdition()));
            }

            catInfo.put("assigned", assignedInfo);

            List<String> nonAssignedInfo = new ArrayList<>();
            for (Category category : inter.getNonAssignedCategories(user)) {
                nonAssignedInfo.add(category.getNameInEditionContext(inter.getEdition()));
            }

            catInfo.put("nonAssigned", nonAssignedInfo);
        }

        return new ResponseEntity<>(catInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/multiple-writer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getMultipleInterWriter(@RequestParam String interIds,
                                                    @RequestParam(required = false, defaultValue = "false") boolean lineByLine,
                                                    @RequestParam(required = false, defaultValue = "false") boolean showSpaces) {

        List<ScholarInter> inters = new ArrayList<>();

        for (String id : interIds.split("%2C")) {
            ScholarInter inter = FenixFramework.getDomainObject(id);
            if (inter != null) {
                inters.add(inter);
            }
        }

        if (inters.size() == 1) {
            ScholarInter inter = inters.get(0);
            PlainHtmlWriter4OneInter writer4One = new PlainHtmlWriter4OneInter(inter);
            writer4One.write(false);
            return new ResponseEntity<>(writer4One.getTranscription(), HttpStatus.OK);
        }

        if (inters.size() > 2) {
            lineByLine = true;
        }

        HtmlWriter2CompInters writer = new HtmlWriter2CompInters(inters);

        Map<String, Object> results = new LinkedHashMap<>();

        writer.write(lineByLine, showSpaces);

        if (!lineByLine) {
            for (ScholarInter inter : inters) {
                Map<String, String> interInfo = new LinkedHashMap<>();
                interInfo.put("transcription", writer.getTranscription(inter));
                interInfo.put("urlId", inter.getUrlId());
                interInfo.put("fragId", inter.getFragment().getXmlId());
                results.put(inter.getExternalId(), interInfo);
            }
        } else {
            results.put("transcription", writer.getTranscriptionLineByLine());
        }

        List<AppText> apps = new ArrayList<>();
        inters.get(0).getFragment().getTextPortion().putAppTextWithVariations(apps, inters);
        Collections.reverse(apps);

        Map<String, List<String>> variations = new HashMap<>();

        for (ScholarInter scholarInter : inters) {
            List<String> interVariation = new ArrayList<>();
            for (AppText app : apps) {
                HtmlWriter4Variations writer4Variations = new HtmlWriter4Variations(scholarInter);
                interVariation.add(writer4Variations.getAppTranscription(app));

            }
            variations.put(scholarInter.getShortName() + "#" + scholarInter.getTitle(), interVariation);
        }

        results.put("variations", variations);
        results.put("title", inters.get(0).getTitle());
        results.put("lineByLine", lineByLine);
        results.put("showSpaces", showSpaces);

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping(value = "/multiple-virtual", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getMultipleVirtualInfo(@RequestParam String interIds) {

        List<VirtualEditionInter> inters = new ArrayList<>();

        for (String id : interIds.split("%2C")) {
            VirtualEditionInter vei = FenixFramework.getDomainObject(id);
            if (vei != null) {
                inters.add(vei);
            }
        }

        Map<String, Object> results = new LinkedHashMap<>();

        for (VirtualEditionInter inter : inters) {
            Map<String, Object> info = new LinkedHashMap<>();

            info.put("reference", inter.getEdition().getReference());

            List<Object> tags = new ArrayList<>();
            for (Tag tag : inter.getTagsCompleteInter()) {
                Map<String, String> tagInfo = new LinkedHashMap<>();
                tagInfo.put("username", tag.getContributor());
                tagInfo.put("acronym", tag.getCategory().getTaxonomy().getEdition().getAcronym());
                tagInfo.put("urlId", tag.getCategory().getUrlId());
                tagInfo.put("name", tag.getCategory().getNameInEditionContext(inter.getEdition()));
                tags.add(tagInfo);
            }
            info.put("tags", tags);

            List<Object> annotations = new ArrayList<>();

            for (Annotation annotation : inter.getAllDepthAnnotations()) {
                Map<String, Object> annotationInfo = new LinkedHashMap<>();
                annotationInfo.put("quote", annotation.getQuote());

                if (annotation.isHumanAnnotation()) {
                    annotationInfo.put("text", annotation.getText());

                    List<Object> annotationTags = new ArrayList<>();
                    for (Tag tag : ((HumanAnnotation) annotation).getTagSet()) {
                        Map<String, String> tagInfo = new LinkedHashMap<>();
                        tagInfo.put("acronym", tag.getCategory().getTaxonomy().getEdition().getAcronym());
                        tagInfo.put("urlId", tag.getCategory().getUrlId());
                        tagInfo.put("name", tag.getCategory().getNameInEditionContext(inter.getEdition()));
                        annotationTags.add(tagInfo);
                    }

                    annotationInfo.put("tags", annotationTags);
                } else {
                    annotationInfo.put("source", ((AwareAnnotation) annotation).getSourceLink());
                    annotationInfo.put("profile", ((AwareAnnotation) annotation).getProfileURL());
                    annotationInfo.put("date", ((AwareAnnotation) annotation).getDate());
                    annotationInfo.put("country", ((AwareAnnotation) annotation).getCountry());
                }

                annotationInfo.put("username", annotation.getUser());

                annotations.add(annotationInfo);
            }

            info.put("annotations", annotations);

            results.put(inter.getExternalId(), info);
        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @PostMapping("/restricted/associate-category")
    public ResponseEntity<?> associateCategoriesToInter(@RequestParam String externalId, @RequestParam String categories) {

        DomainObject object = FenixFramework.getDomainObject(externalId);

        if (!(object instanceof VirtualEditionInter)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VirtualEditionInter inter = (VirtualEditionInter) object;

        User user = User.getAuthenticatedUser();

        if (user == null || !inter.getEdition().isPublicOrIsParticipant()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String[] names = categories.split("%2C");

        inter.associate(this.userProvidesInterface.getAuthenticatedUser(), Arrays.stream(names).collect(Collectors.toSet()));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/restricted/dissociate-category")
    public ResponseEntity<?> dissociateCategoryFromInter(@RequestParam String externalId, @RequestParam String categoryId) {
        DomainObject object = FenixFramework.getDomainObject(externalId);

        Category category = FenixFramework.getDomainObject(categoryId);

        if (!(object instanceof VirtualEditionInter) || category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VirtualEditionInter inter = (VirtualEditionInter) object;

        inter.dissociate(this.userProvidesInterface.getAuthenticatedUser(), category);

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

        User user = UserModule.getInstance().getUser(username);

        if (!this.passwordEncoder.matches(currentPassword, user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        user.updatePassword(this.passwordEncoder, currentPassword, newPassword);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //Annotation related methods

    @GetMapping("/inter/categories")
    public ResponseEntity<?> getCategoriesForInter(@RequestParam String id) {

        VirtualEditionInter inter = FenixFramework.getDomainObject(id);

        if (inter == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ObjectMapper mapper = new ObjectMapper();

        List<CategoryDTO> categories = inter.getAllDepthCategories().stream()
                .sorted((c1, c2) -> c1.compareInEditionContext(inter.getVirtualEdition(), c2))
                .map(c -> new CategoryDTO(inter.getVirtualEdition(), c)).collect(Collectors.toList());

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

        VirtualEditionInter inter = FenixFramework.getDomainObject(annotationJson.getUri());
        VirtualEdition virtualEdition = inter.getEdition();

        String user = this.userProvidesInterface.getAuthenticatedUser();

        HumanAnnotation annotation;
        if (HumanAnnotation.canCreate(virtualEdition, user)) {
            annotation = inter.createHumanAnnotation(annotationJson.getQuote(), annotationJson.getText(), user,
                    annotationJson.getRanges(), annotationJson.getTags());

            annotationJson.setId(annotation.getExternalId());

            return new ResponseEntity<>(new AnnotationDTO(annotation), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/restricted/annotations/{id}")
    public ResponseEntity<?> getAnnotation(@PathVariable String id) {
        HumanAnnotation annotation = FenixFramework.getDomainObject(id);
        if (annotation != null) {
            return new ResponseEntity<>(new AnnotationDTO(annotation), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PutMapping("/restricted/annotations/{id}")
    public ResponseEntity<?> updateAnnotation(@PathVariable String id, @RequestBody AnnotationDTO annotationJson) {
        HumanAnnotation annotation = FenixFramework.getDomainObject(id);

        if (annotation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (annotation.canUpdate(this.userProvidesInterface.getAuthenticatedUser())) {
            annotation.update(annotationJson.getText(), annotationJson.getTags());
            return new ResponseEntity<>(new AnnotationDTO(annotation), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/restricted/annotations/{id}")
    public ResponseEntity<?> deleteAnnotation(@PathVariable String id) {

        HumanAnnotation annotation = FenixFramework.getDomainObject(id);

        if (annotation == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (annotation.canDelete(this.userProvidesInterface.getAuthenticatedUser())) {
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

        VirtualEditionInter inter = FenixFramework.getDomainObject(uri);

        for (Annotation annotation : inter.getAllDepthAnnotations()) {
            AnnotationDTO annotationJson = new AnnotationDTO(annotation);
            annotations.add(annotationJson);
        }

        return new AnnotationSearchJson(annotations);
    }
}
