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
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.domain.Module;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;

import java.util.*;
import java.util.stream.Collectors;

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

        Map<String,Map<String,String>> interInfo = new LinkedHashMap<>();

        for(ExpertEditionInter expertEditionInter : expertEditionInters){
            Map<String,String> info = new LinkedHashMap<>();
            info.put("xmlId", expertEditionInter.getXmlId());
            info.put("title", expertEditionInter.getTitle());
            info.put("number", Integer.toString(expertEditionInter.getNumber()));
            info.put("urlId", expertEditionInter.getUrlId());
            info.put("externalId", expertEditionInter.getExternalId());

            interInfo.put(expertEditionInter.getExpertEdition().getAcronym(),info);
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
}
