package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.edition;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.CategoryDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.ExpertEditionListDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.TaxoDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.UserContributionsDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.VirtualEditionListDto;
import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGame;
import pt.ist.socialsoftware.edition.ldod.domain.ClassificationGameParticipant;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.Taxonomy;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.ClassificationGameContentDto;

@RestController
@RequestMapping("/api/microfrontend/edition")
public class MicrofrontendEditionController {
	
    @GetMapping(value = "/user/{username}")
    public UserContributionsDto getUserContributions(@PathVariable String username) {
    	
    	LdoDUser user = LdoD.getInstance().getUser(username);
    	
    	if(user != null) {
    		return new UserContributionsDto(user);
    	}
		return null;
    	
    }
    
    @GetMapping(value = "/acronym/{acronym}")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public String getEditionbyAcronym(@PathVariable String acronym) {
    	Edition edition = LdoD.getInstance().getEdition(acronym);
    	return edition.getSourceType().toString();
    }
    
    @GetMapping(value = "/expert/acronym/{acronym}")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public ExpertEditionListDto getExpertEditionTableOfContentsbyAcronym(@PathVariable String acronym) {
   		for (ExpertEdition edition : LdoD.getInstance().getExpertEditionsSet()) {
			if (acronym.toUpperCase().equals(edition.getAcronym().toUpperCase())) {
				return new ExpertEditionListDto(edition);
			}
		}
		return null;
	}
    
    @GetMapping(value = "/virtual/acronym/{acronym}")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public VirtualEditionListDto getVirtualEditionTableOfContentsbyAcronym(@PathVariable String acronym) {
		for (VirtualEdition edition : LdoD.getInstance().getVirtualEditionsSet()) {
			if (acronym.toUpperCase().equals(edition.getAcronym().toUpperCase())) {
				return new VirtualEditionListDto(edition, "deep");
			}
		}
		return null;
		
	}
    
    @GetMapping(value = "/acronym/{acronym}/taxonomy")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public TaxoDto getTaxonomyTableOfContents(@PathVariable String acronym) {
    	Taxonomy taxonomy = LdoD.getInstance().getVirtualEdition(acronym).getTaxonomy();
    	
    	if(taxonomy != null) {
    		return new TaxoDto(taxonomy);
    	}
		return null;
    }
    
    @GetMapping(value = "/acronym/{acronym}/category/{urlId}")
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
	public CategoryDto getCategoryTableOfContents(@PathVariable String acronym, @PathVariable String urlId) {
    	
    	VirtualEdition virtualEdition = (VirtualEdition) LdoD.getInstance().getEdition(acronym);
    	Category category = virtualEdition.getTaxonomy().getCategoryByUrlId(urlId);
    	
    	if(category != null) {
    		return new CategoryDto(category, "categoryPage");
    	}
    	
		return null;
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
