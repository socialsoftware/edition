package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.documents;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.SourceInterDto;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/microfrontend/documents")
public class MicrofrontendDocumentsController {

    @GetMapping("/fragments")
    public List<FragmentDto> getFragments() {

        return LdoD.getInstance().getFragmentsSet().stream()
                .map(FragmentDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/sources")
    public List<SourceInterDto> getSources() {

        return LdoD.getInstance().getFragmentsSet()
                .stream()
                .flatMap(fragment -> new ArrayList<>(fragment.getSourcesSet())
                        .stream())
                .map(SourceInterDto::new)
                .sorted(Comparator.comparing((SourceInterDto::getTitle)))
                .collect(Collectors.toList());

    }
}
