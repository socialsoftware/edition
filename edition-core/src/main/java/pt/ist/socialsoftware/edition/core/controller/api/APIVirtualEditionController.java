package pt.ist.socialsoftware.edition.core.controller.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.socialsoftware.edition.core.domain.LdoD;
import pt.ist.socialsoftware.edition.core.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.core.dto.EditionFragmentsDTO;

@RestController
@RequestMapping("/api")
public class APIVirtualEditionController {
    private static Logger logger = LoggerFactory.getLogger(APIUserController.class);

    @GetMapping(value = "/edition/{acronym}")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public EditionFragmentsDTO getEdition(@PathVariable(value = "acronym") String acronym) {
        logger.debug("getEdition");
        VirtualEdition virtualEdition = LdoD.getInstance().getVirtualEdition(acronym);
        EditionFragmentsDTO editionFragmentsDTO = new EditionFragmentsDTO();
        editionFragmentsDTO.setFragments(virtualEdition.buildEditionDTO());
        return editionFragmentsDTO;
    }

}
