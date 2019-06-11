package pt.ist.socialsoftware.edition.ldod.controller.api;

import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.api.ui.UiInterface;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.domain.Module;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;

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

        Map<String, Map<String, List<Pair<String,String>>>> results = new LinkedHashMap<>();


        for (Module module: moduleSet){
            Map<String, List<Pair<String,String>>> temp = new LinkedHashMap<>();
            for (Menu menu : module.getUiComponent().getMenuSet().stream().sorted(Comparator.comparingInt(Menu::getPosition)).collect(Collectors.toList())) {
                List<Pair<String,String>> links = menu.getOptionSet().stream().sorted(Comparator.comparingInt(Option::getPosition))
                        .map(option -> new Pair<>(option.getName(),option.getLink())).collect(Collectors.toList());
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
        List<Pair<String,String>> expertEditionInfo = Text.getInstance().getSortedExpertEdition().stream()
                .map(expertEdition -> new Pair<>(expertEdition.getAcronym(), expertEdition.getEditor())).collect(Collectors.toList());

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
            logger.debug(dimension);
            metaInfo.put("dimension",dimension);

            metaInfo.put("material", source.getMaterial());

            metaInfo.put("columns", source.getColumns());

            metaInfo.put("ldoDKey",((ManuscriptSource) ((SourceInter) scholarInter).getSource()).getHasLdoDLabel());

            List<Pair<String, String>> handNotes = source.getHandNoteSet().stream().map(handNote ->
                    new Pair<>((handNote.getMedium() != null ? handNote.getMedium().getDesc() : ""), handNote.getNote())).collect(Collectors.toList());

            metaInfo.put("handNotes", handNotes);

            List<Pair<String, String>> typeNotes = source.getTypeNoteSet().stream().map(typeNote ->
                    new Pair<>((typeNote.getMedium() != null ? typeNote.getMedium().getDesc() : ""), typeNote.getNote())).collect(Collectors.toList());

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
            List<Pair<String, String>> surfaces = source.getFacsimile().getSurfaces().stream().map(surface -> new Pair<>("/facs/" + surface.getGraphic(), source.getAltIdentifier())).collect(Collectors.toList());
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


    @GetMapping(value = "virtual-inter", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getFragmentVirtualInterInfo(@RequestParam String xmlId) {

        Fragment fragment = Text.getInstance().getFragmentByXmlId(xmlId);

        if(fragment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Map<String,List<Map<String,String>>> interInfo = new LinkedHashMap<>();

        Set<VirtualEdition> virtualEditions = new LinkedHashSet<>(LdoDSession.getLdoDSession().materializeVirtualEditions());

        virtualEditions.add(LdoD.getInstance().getArchiveEdition());

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

    @GetMapping(value = "virtual-edition", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
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

        if(inter.getUses() != null){
            editionInfo.put("editionReference", inter.getUses().getEdition().getReference());
            editionInfo.put("interReference", inter.getUses().getReference());
        }
        else {
            editionInfo.put("editionReference", inter.getLastUsed().getEdition().getReference());
            editionInfo.put("interReference", inter.getLastUsed().getReference());
        }

        logger.debug(editionInfo.toString());

        return new ResponseEntity<>(editionInfo,HttpStatus.OK);
    }
}
