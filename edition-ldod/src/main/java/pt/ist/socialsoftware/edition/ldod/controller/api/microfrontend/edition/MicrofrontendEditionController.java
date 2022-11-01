package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.edition;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.*;
import pt.ist.socialsoftware.edition.ldod.domain.*;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/microfrontend/edition")
public class MicrofrontendEditionController {

    @GetMapping(value = "/user/{username}")
    public UserContributionsDto getUserContributions(@PathVariable String username) {
        return Stream
                .of(LdoD.getInstance().getUser(username))
                .map(UserContributionsDto::new).findFirst().orElse(null);
    }

    @GetMapping(value = "/acronym/{acronym}")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public String getEditionByAcronym(@PathVariable String acronym) {
        return LdoD.getInstance().getEdition(acronym).getSourceType().toString();
    }

    @GetMapping(value = "/expert/acronym/{acronym}")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public ExpertEditionListDto getExpertEditionTableOfContentsByAcronym(@PathVariable String acronym) {
        ExpertEdition epEd = LdoD.getInstance().getExpertEdition(acronym);
        return epEd != null ? new ExpertEditionListDto(epEd) : null;
    }

    @GetMapping(value = "/virtual/acronym/{acronym}")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public VirtualEditionListDto getVirtualEditionTableOfContentsByAcronym(@PathVariable String acronym) {
        VirtualEdition ve = LdoD.getInstance().getVirtualEdition(acronym);
        return ve != null ? new VirtualEditionListDto(ve, "deep") : null;
    }

    @GetMapping(value = "/acronym/{acronym}/taxonomy")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public Optional<TaxoDto> getTaxonomyTableOfContents(@PathVariable String acronym) {
        return LdoD.getInstance()
                .getOptionalVirtualEdition(acronym)
                .map(VirtualEdition_Base::getTaxonomy)
                .map(TaxoDto::new);
    }

    @GetMapping(value = "/acronym/{acronym}/category/{urlId}")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public Optional<CategoryDto> getCategoryTableOfContents(@PathVariable String acronym, @PathVariable String urlId) {
        return LdoD.getInstance()
                .getOptionalVirtualEdition(acronym)
                .map(VirtualEdition_Base::getTaxonomy)
                .map(taxonomy -> new CategoryDto(taxonomy.getCategoryByUrlId(urlId), CategoryDto.PresentationTarget.CATEGORY_PAGE));
    }

    @GetMapping(value = "/game/{externalId}/classificationGame/{gameId}")
    public ClassificationGameContentDto getClassificationGameContent(@PathVariable String externalId, @PathVariable String gameId) {
        VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
        ClassificationGame game = FenixFramework.getDomainObject(gameId);
        if (virtualEdition == null || game == null) {
            return null;
        } else {
            return new ClassificationGameContentDto(virtualEdition, game, game.getClassificationGameParticipantSet().stream()
                    .sorted(Comparator.comparing(ClassificationGameParticipant::getScore).reversed())
                    .collect(Collectors.toList()));
        }
    }

}
