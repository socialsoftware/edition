package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/microfrontend")
public class MicrofrontendController {
    private static Logger logger = LoggerFactory.getLogger(MicrofrontendController.class);

    @GetMapping("/fragments")
    public List<FragmentDto> getFragments() {
        logger.debug("getFragments");

        return LdoD.getInstance().getFragmentsSet().stream()
                .map(FragmentDto::new)
                .collect(Collectors.toList());
    }
}
