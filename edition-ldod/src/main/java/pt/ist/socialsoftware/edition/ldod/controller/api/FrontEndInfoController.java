package pt.ist.socialsoftware.edition.ldod.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.api.ui.UiInterface;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.domain.Module;
import pt.ist.socialsoftware.edition.ldod.generators.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.ldod.generators.HtmlWriter4Variations;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;

import javax.print.attribute.standard.Media;
import java.util.*;
import java.util.stream.Collectors;

import static pt.ist.socialsoftware.edition.ldod.api.ui.UiInterface.InterType.AUTHORIAL;
import static pt.ist.socialsoftware.edition.ldod.api.ui.UiInterface.InterType.EDITORIAL;
import static pt.ist.socialsoftware.edition.ldod.domain.Source.SourceType.MANUSCRIPT;
import static pt.ist.socialsoftware.edition.ldod.domain.Source.SourceType.PRINTED;

@RestController
@RequestMapping("/api/services/frontend")
public class FrontEndInfoController {
    private static Logger logger = LoggerFactory.getLogger(FrontEndInfoController.class);

    // /api/services/frontend/module-info
    @GetMapping(value = "/module-info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getModuleInfo() {
        Set<Module> moduleSet =  FenixFramework.getDomainRoot().getModuleSet();

        Map<String, Map<String, List<AbstractMap.SimpleEntry<String,String>>>> results = new LinkedHashMap<>();


        for (Module module: moduleSet){
            Map<String, List<AbstractMap.SimpleEntry<String,String>>> temp = new LinkedHashMap<>();
            for (Menu menu : module.getUiComponent().getMenuSet().stream().sorted(Comparator.comparingInt(Menu::getPosition)).collect(Collectors.toList())) {
                List<AbstractMap.SimpleEntry<String,String>> links = menu.getOptionSet().stream().sorted(Comparator.comparingInt(Option::getPosition))
                        .map(option -> new AbstractMap.SimpleEntry<>(option.getName(),option.getLink())).collect(Collectors.toList());
                temp.put(menu.getName(),links);
            }
            results.put(module.getName(), temp);
        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping(value = "/frag-info",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFragmentInfo(@RequestParam String xmlId){

        Fragment fragment = Text.getInstance().getFragmentByXmlId(xmlId);

        if(fragment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Map<String,String> fragInfo = new LinkedHashMap<>();

        fragInfo.put("xmlId", fragment.getXmlId());
        fragInfo.put("title", fragment.getTitle());

        return new ResponseEntity<>(fragInfo,HttpStatus.OK);
    }

    @GetMapping(value = "/expert-inter",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFragmentExpertInterInfo(@RequestParam String xmlId){

        Fragment fragment = Text.getInstance().getFragmentByXmlId(xmlId);

        if(fragment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<ExpertEditionInter> expertEditionInters = fragment.getExpertEditionInterSet().stream().sorted().collect(Collectors.toList());

        Map<String,List<Map<String,String>>> interInfo = new LinkedHashMap<>();

        for(ExpertEditionInter expertEditionInter : expertEditionInters){
            Map<String,String> info = new LinkedHashMap<>();
            info.put("xmlId", expertEditionInter.getXmlId());
            info.put("title", expertEditionInter.getTitle());
            info.put("number", Integer.toString(expertEditionInter.getNumber()));
            info.put("urlId", expertEditionInter.getUrlId());
            info.put("externalId", expertEditionInter.getExternalId());
            info.put("nextXmlId", expertEditionInter.getNextNumberInter().getFragment().getXmlId());
            info.put("nextUrlId", expertEditionInter.getNextNumberInter().getUrlId());
            info.put("prevXmlId", expertEditionInter.getPrevNumberInter().getFragment().getXmlId());
            info.put("prevUrlId", expertEditionInter.getPrevNumberInter().getUrlId());

            if(interInfo.containsKey(expertEditionInter.getExpertEdition().getAcronym())){
                interInfo.get(expertEditionInter.getExpertEdition().getAcronym()).add(info);
            }
            else {
                List<Map<String,String>> infoList = new ArrayList<>();
                infoList.add(info);
                interInfo.put(expertEditionInter.getExpertEdition().getAcronym(), infoList);
            }
        }

        return new ResponseEntity<>(interInfo,HttpStatus.OK);
    }

    @GetMapping(value = "/source-inter",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFragmentSourceInterInfo(@RequestParam String xmlId){

        Fragment fragment = Text.getInstance().getFragmentByXmlId(xmlId);

        if(fragment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Set<Map<String,String>> interInfo = new LinkedHashSet<>();

        for(SourceInter sourceInter : fragment.getSortedSourceInter()){
            Map<String,String> info = new LinkedHashMap<>();
            info.put("xmlId", sourceInter.getXmlId());
            info.put("shortName", sourceInter.getShortName());
            info.put("urlId", sourceInter.getUrlId());
            info.put("externalId", sourceInter.getExternalId());

            interInfo.add(info);
        }

        return new ResponseEntity<>(interInfo,HttpStatus.OK);
    }

    @GetMapping(value = "/expert-edition",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getExpertEditionInfo(){
        List<AbstractMap.SimpleEntry<String,String>> expertEditionInfo = Text.getInstance().getSortedExpertEdition().stream()
                .map(expertEdition -> new AbstractMap.SimpleEntry<>(expertEdition.getAcronym(), expertEdition.getEditor())).collect(Collectors.toList());

        return new ResponseEntity<>(expertEditionInfo,HttpStatus.OK);
    }

    @GetMapping(value = "/inter-writer", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> getInterWriterResult(@RequestParam String xmlId, @RequestParam String urlId){

        Fragment fragment = Text.getInstance().getFragmentByXmlId(xmlId);

        if(fragment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        ScholarInter scholarInter = fragment.getScholarInterByUrlId(urlId);

        PlainHtmlWriter4OneInter writer;

        if(scholarInter != null)
            writer = new PlainHtmlWriter4OneInter(scholarInter);
        else {
            VirtualEditionInter virtualEditionInter = LdoD.getInstance().getVirtualEditionInterByUrlId(urlId);
            if (virtualEditionInter == null)
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            writer = new PlainHtmlWriter4OneInter(virtualEditionInter.getLastUsed());
        }

        writer.write(false);
        return new ResponseEntity<>(writer.getTranscription(),HttpStatus.OK);
    }

    @GetMapping(value = "/meta-info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getInterMetaInfo(@RequestParam String xmlId, @RequestParam String urlId) {

        Fragment fragment = Text.getInstance().getFragmentByXmlId(xmlId);

        if(fragment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        ScholarInter scholarInter = fragment.getScholarInterByUrlId(urlId);

        if(scholarInter == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        Map<String, Object> metaInfo = new LinkedHashMap<>();

        UiInterface uiInterface = new UiInterface();

        boolean isEditorial = uiInterface.getSourceTypeOfInter(urlId.replace("_", ".")).equals(EDITORIAL);
        boolean isManuscript = uiInterface.getSourceTypeOfInter(urlId.replace("_", ".")).equals(AUTHORIAL)
                                && ((SourceInter) scholarInter).getSource().getType().equals(MANUSCRIPT);
        boolean isPublication = uiInterface.getSourceTypeOfInter(urlId.replace("_", ".")).equals(AUTHORIAL)
                                && ((SourceInter) scholarInter).getSource().getType().equals(PRINTED);

        if(isEditorial)
            metaInfo.put("title", scholarInter.getTitle());
        else if (isPublication){
            PrintedSource source = (PrintedSource) ((SourceInter) scholarInter).getSource();
            metaInfo.put("title", source.getTitle());
        }
        else
            metaInfo.put("title", "");


        if(isManuscript){
            ManuscriptSource source = (ManuscriptSource) ((SourceInter) scholarInter).getSource();
            metaInfo.put("idno", source.getIdno());
        }


        metaInfo.put("heteronym", scholarInter.getHeteronym().getName());

        if(isManuscript) {
            ManuscriptSource source = (ManuscriptSource) ((SourceInter) scholarInter).getSource();

            String dimension = source.getDimensionsSet().stream().map(dimensions -> dimensions.getHeight() + "x" + dimensions.getWidth())
                    .collect(Collectors.joining(";"));
            metaInfo.put("dimension",dimension);

            metaInfo.put("material", source.getMaterial());

            metaInfo.put("columns", source.getColumns());

            metaInfo.put("ldoDKey",((ManuscriptSource) ((SourceInter) scholarInter).getSource()).getHasLdoDLabel());

            List<AbstractMap.SimpleEntry<String, String>> handNotes = source.getHandNoteSet().stream().map(handNote ->
                    new AbstractMap.SimpleEntry<>((handNote.getMedium() != null ? handNote.getMedium().getDesc() : ""), handNote.getNote())).collect(Collectors.toList());

            metaInfo.put("handNotes", handNotes);

            List<AbstractMap.SimpleEntry<String, String>> typeNotes = source.getTypeNoteSet().stream().map(typeNote ->
                    new AbstractMap.SimpleEntry<>((typeNote.getMedium() != null ? typeNote.getMedium().getDesc() : ""), typeNote.getNote())).collect(Collectors.toList());

            metaInfo.put("typeNotes", typeNotes);
        }

        if(isEditorial) {
            metaInfo.put("volume", ((ExpertEditionInter) scholarInter).getVolume());
        }

        if (isPublication) {
            PrintedSource source = (PrintedSource) ((SourceInter) scholarInter).getSource();
            metaInfo.put("journal", source.getJournal());
        }


        if(isEditorial) {
            metaInfo.put("number", ((ExpertEditionInter) scholarInter).getCompleteNumber());
        }
        else if (isPublication) {
            PrintedSource source = (PrintedSource) ((SourceInter) scholarInter).getSource();
            metaInfo.put("number", source.getIssue());
        }
        else {
            metaInfo.put("number", "");
        }

        if(isEditorial) {
            metaInfo.put("startPage", ((ExpertEditionInter) scholarInter).getStartPage());
            metaInfo.put("endPage", ((ExpertEditionInter) scholarInter).getEndPage());
        }
        else if (isPublication) {
            PrintedSource source = (PrintedSource) ((SourceInter) scholarInter).getSource();
            metaInfo.put("startPage", source.getStartPage());
            metaInfo.put("endPage", source.getEndPage());
        }
        else {
            metaInfo.put("startPage", 0);
        }

        if (isPublication) {
            PrintedSource source = (PrintedSource) ((SourceInter) scholarInter).getSource();
            metaInfo.put("pubPlace", source.getPubPlace());
        }

        if(isEditorial) {
            LdoDDate ldoDDate = scholarInter.getLdoDDate();
            metaInfo.put("date", (ldoDDate != null ? ldoDDate.print() : ""));
            if( ldoDDate != null && ldoDDate.getPrecision() != null)
                metaInfo.put("datePrecision", ldoDDate.getPrecision().getDesc());
        }
        else {
            LdoDDate ldoDDate = ((SourceInter) scholarInter).getSource().getLdoDDate();
            metaInfo.put("date", (ldoDDate != null ? ldoDDate.print() : ""));
            if(ldoDDate != null && ldoDDate.getPrecision() != null)
                metaInfo.put("datePrecision", ldoDDate.getPrecision().getDesc());
        }

        if(isEditorial) {
            metaInfo.put("notes", ((ExpertEditionInter) scholarInter).getNotes());
        }
        else if (isManuscript) {
            ManuscriptSource source = (ManuscriptSource) ((SourceInter) scholarInter).getSource();
            metaInfo.put("notes", source.getNotes());
        }
        else {
            metaInfo.put("notes", "");
        }

        List<String> notes = scholarInter.getSortedAnnexNote().stream().map(annexNote -> annexNote.getNoteText().generatePresentationText())
                .collect(Collectors.toList());
        metaInfo.put("annexNotes",notes);

        if (isManuscript || isPublication){
            Source source = ((SourceInter) scholarInter).getSource();
            List<AbstractMap.SimpleEntry<String, String>> surfaces = source.getFacsimile().getSurfaces().stream()
                    .map(surface -> new AbstractMap.SimpleEntry<>("/facs/" + surface.getGraphic(), source.getAltIdentifier())).collect(Collectors.toList());
            metaInfo.put("surfaces", surfaces);
        }

        return new ResponseEntity<>(metaInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/source-writer", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> getSourceWriterWithOptions(@RequestParam String xmlId, @RequestParam String urlId, @RequestParam boolean diff,
                                                        @RequestParam boolean del, @RequestParam boolean ins,
                                                        @RequestParam boolean subst, @RequestParam boolean notes) {

        Fragment fragment = Text.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        SourceInter inter = fragment.getSortedSourceInter().stream().filter(sourceInter -> sourceInter.getUrlId().equals(urlId)).findFirst().orElse(null);

        if (inter == null){
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);

        writer.write(diff,del,ins,subst,notes,false,null);

        return new ResponseEntity<>(writer.getTranscription(),HttpStatus.OK);
    }

    @GetMapping(value = "/expert-writer", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<?> getExpertWriterWithOptions(@RequestParam String xmlId, @RequestParam String urlId,
                                                        @RequestParam boolean diff) {

        Fragment fragment = Text.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        ExpertEditionInter inter = fragment.getExpertEditionInterSet().stream().filter(sourceInter -> sourceInter.getUrlId().equals(urlId)).findFirst().orElse(null);

        if (inter == null){
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);

        writer.write(diff);

        return new ResponseEntity<>(writer.getTranscription(),HttpStatus.OK);
    }

    @GetMapping(value = "/fac-urls", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFacsForSourceInter(@RequestParam String xmlId, @RequestParam String urlId,
                                                   @RequestParam(required = false) String pbTextID){

        Fragment fragment = Text.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        SourceInter inter = fragment.getSortedSourceInter().stream().filter(sourceInter -> sourceInter.getUrlId().equals(urlId)).findFirst().orElse(null);

        if (inter == null){
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<String> urls = inter.getSource().getFacsimile().getSurfaces().stream().map(Surface::getGraphic).collect(Collectors.toList());

        return new ResponseEntity<>(urls,HttpStatus.OK);
    }


    @GetMapping(value = "/virtual-inter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFragmentVirtualInterInfo(@RequestParam String xmlId) {

        Fragment fragment = Text.getInstance().getFragmentByXmlId(xmlId);

        if(fragment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Map<String,List<Map<String,String>>> interInfo = new LinkedHashMap<>();


        //TODO : delete the following assignment and uncomment the following lines when login capabilities are added to react frontend.
        Set<VirtualEdition> virtualEditions = LdoD.getInstance().getVirtualEditionsSet();

        /*Set<VirtualEdition> virtualEditions = new LinkedHashSet<>(LdoDSession.getLdoDSession().materializeVirtualEditions());

        virtualEditions.addAll((LdoDUser.getAuthenticatedUser() != null ? LdoDUser.getAuthenticatedUser().getSelectedVirtualEditionsSet() : new LinkedHashSet<>()));

        virtualEditions.add(LdoD.getInstance().getArchiveEdition());

        logger.debug(String.valueOf(virtualEditions.size()));*/

        for(VirtualEdition virtualEdition : virtualEditions){
            Set<VirtualEditionInter> editionInters = virtualEdition.getVirtualEditionInterSetForFragment(fragment);

            List<Map<String,String>> data = new ArrayList<>();
            for(VirtualEditionInter vei : editionInters){
                Map<String,String> info = new LinkedHashMap<>();
                info.put("xmlId", vei.getXmlId());
                info.put("title", vei.getTitle());
                info.put("number", Integer.toString(vei.getNumber()));
                info.put("urlId", vei.getUrlId());
                info.put("externalId", vei.getExternalId());
                info.put("nextXmlId", vei.getNextNumberInter().getFragment().getXmlId());
                info.put("nextUrlId", vei.getNextNumberInter().getUrlId());
                info.put("prevXmlId", vei.getPrevNumberInter().getFragment().getXmlId());
                info.put("prevUrlId", vei.getPrevNumberInter().getUrlId());
                data.add(info);
            }
            interInfo.put(virtualEdition.getAcronym(),data);
        }

        return new ResponseEntity<>(interInfo,HttpStatus.OK);
    }

    @GetMapping(value = "/virtual-edition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFragmentVirtualInterInfo(@RequestParam String xmlId, @RequestParam String urlId) {

        Fragment fragment = Text.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VirtualEditionInter inter = LdoD.getInstance().getVirtualEditionInterSet(fragment).stream()
                .filter(virtualEditionInter -> virtualEditionInter.getUrlId().equals(urlId)).findFirst().orElse(null);

        if (inter == null){
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String,String> editionInfo = new LinkedHashMap<>();

        editionInfo.put("editionTitle", inter.getEdition().getTitle());
        editionInfo.put("externalId", inter.getExternalId());

        if(inter.getUses() != null){
            editionInfo.put("editionReference", inter.getUses().getEdition().getReference());
            editionInfo.put("interReference", inter.getUses().getReference());
        }
        else {
            editionInfo.put("editionReference", inter.getLastUsed().getEdition().getReference());
            editionInfo.put("interReference", inter.getLastUsed().getReference());
        }

        return new ResponseEntity<>(editionInfo,HttpStatus.OK);
    }

    @GetMapping(value = "/taxonomy", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getTaxonomyForInter(@RequestParam String xmlId, @RequestParam String urlId){

        Fragment fragment = Text.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VirtualEditionInter inter = LdoD.getInstance().getVirtualEditionInterSet(fragment).stream()
                .filter(virtualEditionInter -> virtualEditionInter.getUrlId().equals(urlId)).findFirst().orElse(null);

        if (inter == null){
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Map<String,Object>> categoryInfo = new ArrayList<>();

        for (Category category : inter.getAssignedCategories()){
            Map<String, Object> infoMap = new LinkedHashMap<>();
            infoMap.put("interExternal", inter.getExternalId());
            infoMap.put("acronym", category.getTaxonomy().getEdition().getAcronym());
            infoMap.put("urlId", category.getUrlId());
            infoMap.put("name", category.getNameInEditionContext(inter.getEdition().getTaxonomy().getEdition()));
            infoMap.put("categoryExternal", category.getExternalId());

            List<Map<String,String>> userList = new ArrayList<>();
            for (LdoDUser user : inter.getContributorSet(category)){
                Map<String,String> userInfo = new LinkedHashMap<>();
                userInfo.put("username", user.getUsername());
                userInfo.put("firstName", user.getFirstName());
                userInfo.put("lastName", user.getLastName());
                userList.add(userInfo);
            }
            infoMap.put("users", userList);

            categoryInfo.add(infoMap);
        }

        return new ResponseEntity<>(categoryInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/categories", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getCategoriesForInter(@RequestParam String xmlId, @RequestParam String urlId){

        Fragment fragment = Text.getInstance().getFragmentByXmlId(xmlId);

        if (fragment == null) {
            logger.debug("Could find frag");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        VirtualEditionInter inter = LdoD.getInstance().getVirtualEditionInterSet(fragment).stream()
                .filter(virtualEditionInter -> virtualEditionInter.getUrlId().equals(urlId)).findFirst().orElse(null);

        if (inter == null){
            logger.debug("Could not find inter");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Map<String,Object> catInfo = new LinkedHashMap<>();

        //TODO: temp solution to get some category info for frontend display. Should be replaced with some sort of login
        // info usage
        LdoDUser user = LdoD.getInstance().getUser("ars");

        List<String> assignedInfo = new ArrayList<>();
        for (Category category : inter.getAssignedCategories(user)){
            assignedInfo.add(category.getNameInEditionContext(inter.getEdition()));
        }

        catInfo.put("assigned", assignedInfo);

        List<String> nonAssignedInfo = new ArrayList<>();
        for (Category category : inter.getNonAssignedCategories(user)){
            nonAssignedInfo.add(category.getNameInEditionContext(inter.getEdition()));
        }

        catInfo.put("nonAssigned", nonAssignedInfo);

        return new ResponseEntity<>(catInfo, HttpStatus.OK);
    }

    @GetMapping(value = "/multiple-writer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getMultipleInterWriter(@RequestParam String[] interIds,
                                                    @RequestParam(required = false, defaultValue = "false") boolean lineByLine,
                                                    @RequestParam(required = false, defaultValue = "false") boolean showSpaces){

        List<ScholarInter> inters = new ArrayList<>();

        for (String id : interIds[0].split("%2C")){
            ScholarInter inter = FenixFramework.getDomainObject(id);
            if(inter != null)
                inters.add(inter);
        }

        if(inters.size() == 1){
            ScholarInter inter = inters.get(0);
            PlainHtmlWriter4OneInter writer4One = new PlainHtmlWriter4OneInter(inter.getLastUsed());
            writer4One.write(false);
            return new ResponseEntity<>(writer4One.getTranscription(),HttpStatus.OK);
        }

        if(inters.size() > 2){
            lineByLine = true;
        }

        HtmlWriter2CompInters writer = new HtmlWriter2CompInters(inters);

        Map<String,Object> results = new LinkedHashMap<>();

        writer.write(lineByLine,showSpaces);

        if(!lineByLine)
            for(ScholarInter inter : inters) {
                Map<String, String> interInfo = new LinkedHashMap<>();
                interInfo.put("transcription", writer.getTranscription(inter));
                interInfo.put("urlId", inter.getUrlId());
                interInfo.put("fragId", inter.getFragment().getXmlId());
                results.put(inter.getExternalId(),interInfo);
            }
        else
            results.put("transcription", writer.getTranscriptionLineByLine());

        List<AppText> apps = new ArrayList<>();
        inters.get(0).getFragment().getTextPortion().putAppTextWithVariations(apps, inters);
        Collections.reverse(apps);

        Map<String, List<String>> variations = new HashMap<>();

        for (ScholarInter scholarInter : inters){
            List<String> interVariation = new ArrayList<>();
            for (AppText app : apps) {
                HtmlWriter4Variations writer4Variations =  new HtmlWriter4Variations(scholarInter);
                interVariation.add(writer4Variations.getAppTranscription(app));

            }
            variations.put(scholarInter.getShortName() + "#" + scholarInter.getTitle(), interVariation);
        }

        results.put("variations",variations);
        results.put("title",inters.get(0).getTitle());
        results.put("lineByLine", lineByLine);
        results.put("showSpaces", showSpaces);

        return new ResponseEntity<>(results,HttpStatus.OK);
    }

    @GetMapping(value = "/multiple-virtual", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getMultipleVirtualInfo(@RequestParam String[] interIds) {

        List<VirtualEditionInter> inters = new ArrayList<>();

        for (String id : interIds[0].split("%2C")){
            VirtualEditionInter vei = FenixFramework.getDomainObject(id);
            if (vei != null)
                inters.add(vei);
        }

        Map<String, Object> results = new LinkedHashMap<>();

        for (VirtualEditionInter inter : inters) {
            Map<String,Object> info = new LinkedHashMap<>();

            info.put("reference", inter.getEdition().getReference());

            List<Object> tags = new ArrayList<>();
            for(Tag tag : inter.getTagsCompleteInter()){
                Map<String,String> tagInfo = new LinkedHashMap<>();
                tagInfo.put("username", tag.getContributor().getUsername());
                tagInfo.put("acronym", tag.getCategory().getTaxonomy().getEdition().getAcronym());
                tagInfo.put("urlId", tag.getCategory().getUrlId());
                tagInfo.put("name", tag.getCategory().getNameInEditionContext(inter.getEdition()));
                tags.add(tagInfo);
            }
            info.put("tags", tags);

            List<Object> annotations = new ArrayList<>();

            for (Annotation annotation : inter.getAllDepthAnnotations()){
                Map<String,Object> annotationInfo = new LinkedHashMap<>();
                annotationInfo.put("quote", annotation.getQuote());

                if(annotation.isHumanAnnotation()) {
                    annotationInfo.put("text", annotation.getText());

                    List<Object> annotationTags = new ArrayList<>();
                    for(Tag tag : ((HumanAnnotation) annotation).getTagSet()){
                        Map<String,String> tagInfo = new LinkedHashMap<>();
                        tagInfo.put("acronym", tag.getCategory().getTaxonomy().getEdition().getAcronym());
                        tagInfo.put("urlId", tag.getCategory().getUrlId());
                        tagInfo.put("name", tag.getCategory().getNameInEditionContext(inter.getEdition()));
                        annotationTags.add(tagInfo);
                    }

                    annotationInfo.put("tags", annotationTags);
                }
                else {
                    annotationInfo.put("source", ((AwareAnnotation) annotation).getSourceLink());
                    annotationInfo.put("profile", ((AwareAnnotation) annotation).getProfileURL());
                    annotationInfo.put("date", ((AwareAnnotation) annotation).getDate());
                    annotationInfo.put("country", ((AwareAnnotation) annotation).getCountry());
                }

                annotationInfo.put("username", annotation.getUser().getUsername());

                annotations.add(annotationInfo);
            }

            info.put("annotations", annotations);

            results.put(inter.getExternalId(), info);
        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
