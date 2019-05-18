package pt.ist.socialsoftware.edition.ldod.controller.api;

import javafx.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.domain.Module;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services/frontend")
public class FrontEndInfoController {

    // /api/services/frontend/module-info TODO: fix not being able to access api without being logged in
    @GetMapping(value = "/module-info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getModuleInfo() {
        Set<Module> moduleSet =  FenixFramework.getDomainRoot().getModuleSet();

        Map<String, Map<String, List<Pair<String,String>>>> results = new HashMap<>();


        for (Module module: moduleSet){
            Map<String, List<Pair<String,String>>> temp = new HashMap<>();
            for (Menu menu : module.getUiComponent().getMenuSet()) {
                List<Pair<String,String>> links = menu.getOptionSet().stream()
                        .map(option -> new Pair<>(option.getName(),option.getLink())).collect(Collectors.toList());
                temp.put(menu.getName(),links);
            }
            results.put(module.getName(), temp);
        }

        return new ResponseEntity<>(results, HttpStatus.OK);
    }
}
