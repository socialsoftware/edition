package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.controller.api.APIUserController;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.AdvancedSearchDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.CategoryDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.CitationDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.ExpertEditionDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.ExpertEditionListDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.ExpertVirtualDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.FragmentBodyDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.FragmentDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.ReadingDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.SimpleSearchDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.SourceInterDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.TaxonomyMicrofrontendDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.UserContributionsDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.VirtualEditionListDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.VirtualRecommendationDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.VirtualTaxonomyDto;
import pt.ist.socialsoftware.edition.ldod.domain.AppText;
import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.Edition.EditionType;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.Member.MemberRole;
import pt.ist.socialsoftware.edition.ldod.dto.JWTAuthenticationDto;
import pt.ist.socialsoftware.edition.ldod.domain.PbText;
import pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.ldod.domain.Section;
import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;
import pt.ist.socialsoftware.edition.ldod.domain.Surface;
import pt.ist.socialsoftware.edition.ldod.domain.Taxonomy;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.generators.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.ldod.generators.HtmlWriter4Variations;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.recommendation.ReadingRecommendation;
import pt.ist.socialsoftware.edition.ldod.recommendation.dto.RecommendVirtualEditionParam;
import pt.ist.socialsoftware.edition.ldod.search.options.AuthoralSearchOption;
import pt.ist.socialsoftware.edition.ldod.search.options.DateSearchOption;
import pt.ist.socialsoftware.edition.ldod.search.options.EditionSearchOption;
import pt.ist.socialsoftware.edition.ldod.search.options.HeteronymSearchOption;
import pt.ist.socialsoftware.edition.ldod.search.options.PublicationSearchOption;
import pt.ist.socialsoftware.edition.ldod.search.options.Search;
import pt.ist.socialsoftware.edition.ldod.search.options.SearchOption;
import pt.ist.socialsoftware.edition.ldod.search.options.TaxonomySearchOption;
import pt.ist.socialsoftware.edition.ldod.search.options.TextSearchOption;
import pt.ist.socialsoftware.edition.ldod.search.options.VirtualEditionSearchOption;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateAcronymException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateNameException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.social.aware.AwareAnnotationFactory;
import pt.ist.socialsoftware.edition.ldod.topicmodeling.TopicModeler;
import pt.ist.socialsoftware.edition.ldod.utils.TopicListDTO;
import pt.ist.socialsoftware.edition.ldod.validator.VirtualEditionValidator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    
    @GetMapping("/sources")
    public List<SourceInterDto> getSources() {
        
        LdoD ldoD = LdoD.getInstance();
        
        List<Source> sources = new ArrayList<>();
		for (Fragment frag : ldoD.getFragmentsSet()) {
			sources.addAll(frag.getSourcesSet());
		}

        return sources.stream()
                .map(SourceInterDto::new)
                .collect(Collectors.toList());
    }
    
    @GetMapping(value = "/acronym/{acronym}")
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
				return new VirtualEditionListDto(edition);
			}
		}
		return null;
		
	}
    
    @GetMapping(value = "/user/{username}")
    public UserContributionsDto getUserContributions(@PathVariable String username) {
    	
    	LdoDUser user = LdoD.getInstance().getUser(username);
    	
    	if(user != null) {
    		return new UserContributionsDto(user);
    	}
		return null;
    	
    }
    
    @GetMapping(value = "/acronym/{acronym}/taxonomy")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public TaxonomyMicrofrontendDto getTaxonomyTableOfContents(@PathVariable String acronym) {
    	
    	Taxonomy taxonomy = LdoD.getInstance().getVirtualEdition(acronym).getTaxonomy();
    	
    	if(taxonomy != null) {
    		return new TaxonomyMicrofrontendDto(taxonomy);
    	}
		return null;
    	
    }
    
    @GetMapping(value = "/acronym/{acronym}/category/{urlId}")
    @PreAuthorize("hasPermission(#acronym, 'editionacronym.public')")
    public CategoryDto getCategoryTableOfContents(@PathVariable String acronym, @PathVariable String urlId) {
    	
    	VirtualEdition virtualEdition = (VirtualEdition) LdoD.getInstance().getEdition(acronym);
    	Category category = virtualEdition.getTaxonomy().getCategoryByUrlId(urlId);
    	
    	if(category != null) {
    		return new CategoryDto(category);
    	}
    	
		return null;
    }
    
    @RequestMapping(value = "/simple/result", method = RequestMethod.POST, headers = {
    "Content-type=text/plain;charset=UTF-8" })
	public SimpleSearchDto simpleSearchResult(@RequestBody String params) {
	  logger.debug("simpleSearchResult params:{}", params);
	
	  String split;
	  if (params.contains("&")) {
	    split = "&";
	  } else {
	    split = "%26";
	  }
	  System.out.println("/////////");
	  System.out.println(split);
	  String search = params.substring(0, params.indexOf(split));
	  params = params.substring(params.indexOf(split) + 1);
	  String searchType = params.substring(0, params.indexOf(split));
	  params = params.substring(params.indexOf(split) + 1);
	  String searchSource = params;
	
	  search = TextSearchOption.purgeSearchText(search);
	  System.out.println("/////////");
	  System.out.println(search);
	  TextSearchOption textSearchOption = new TextSearchOption(search);
	  List<FragInter> matches = textSearchOption.search();
	  System.out.println("/////////");
	  System.out.println(textSearchOption);
	
	  Map<Fragment, List<FragInter>> results = new HashMap<>();
	  int interCount = 0;
	  for (FragInter inter : matches) {
	    Fragment fragment = inter.getFragment();
	    if ((searchSource.compareTo("") == 0
	        || inter.getShortName().toLowerCase().contains(searchSource.toLowerCase()))
	        && (searchType.compareTo("") == 0
	            || inter.getTitle().toLowerCase().contains(search.toLowerCase()))) {
	
	      if (!results.containsKey(fragment)) {
	        results.put(fragment, new ArrayList<FragInter>());
	      }
	
	      if (!results.get(fragment).contains(inter)) {
	        results.get(fragment).add(inter);
	        interCount++;
	      }
	    }
	  }
	
	  
	  return new SimpleSearchDto(results, interCount);
	  
	}
    
    @RequestMapping(value = "/advanced/result", method = RequestMethod.POST, headers = {
    "Content-type=application/json" })
	public AdvancedSearchDto advancedSearchResultNew(@RequestBody Search search) {
	logger.debug("AdvancedSearchResult params:{}", search);
	  Map<Fragment, Map<FragInter, List<SearchOption>>> results = search.search();
	
	  int fragCount = 0;
	  int fragCountNotAdded = 0;
	  int interCount = 0;
	  int interCountNotAdded = 0;
	  boolean showSource = false;
	  boolean showSourceType = false;
	  boolean showEdition = false;
	  boolean showHeteronym = false;
	  boolean showDate = false;
	  boolean showLdoD = false;
	  boolean fragAdd = false;
	  boolean showTaxonomy = false;
	  for (Map.Entry<Fragment, Map<FragInter, List<SearchOption>>> entry : results.entrySet()) {
	    fragAdd = false;
	    for (Map.Entry<FragInter, List<SearchOption>> entry2 : entry.getValue().entrySet()) {
	      if (entry2.getValue().size() >= 1) {
	        interCount++;
	        fragAdd = true;
	      } else {
	        interCountNotAdded++;
	      }
	      for (SearchOption option : entry2.getValue()) {
	        if (option instanceof EditionSearchOption) {
	          showSource = true;
	          EditionSearchOption op = (EditionSearchOption) option;
	          if (!op.getEdition().equals(SearchOption.ALL)) {
	            showEdition = true;
	          }
	          if (op.hasHeteronym()) {
	            showHeteronym = true;
	          }
	          if (op.hasDate()) {
	            showDate = true;
	          }
	        } else if (option instanceof AuthoralSearchOption) {
	          showSource = true;
	          showSourceType = true;
	          AuthoralSearchOption op = (AuthoralSearchOption) option;
	          if (op.hasLdoDMark()) {
	            showLdoD = true;
	          }
	          if (op.hasDate()) {
	            showDate = true;
	          }
	        } else if (option instanceof PublicationSearchOption) {
	          showSource = true;
	          PublicationSearchOption op = (PublicationSearchOption) option;
	          if (op.hasDate()) {
	            showDate = true;
	          }
	        } else if (option instanceof HeteronymSearchOption) {
	          showHeteronym = true;
	        } else if (option instanceof DateSearchOption) {
	          showDate = true;
	        } else if (option instanceof TaxonomySearchOption) {
	          showTaxonomy = true;
	        } else if (option instanceof VirtualEditionSearchOption) {
	          showSource = true;
	        }
	      }
	    }
	    if (fragAdd) {
	      fragCount++;
	    } else {
	      fragCountNotAdded++;
	    }
	  }
	  
	  SearchOption[] searchOptions = search.getSearchOptions();
	  return new AdvancedSearchDto(showEdition, showHeteronym, showDate, showLdoD, showSource, showSourceType, showTaxonomy, fragCount, interCount, fragCountNotAdded, interCountNotAdded,
			  results, searchOptions, searchOptions.length);


	}
    
    @RequestMapping(value = "/getVirtualEditions")
    public Map<String, String> getVirtualEditions() {
      Stream<VirtualEdition> virtualEditionStream = LdoD.getInstance().getVirtualEditionsSet().stream().filter(virtualEdition -> virtualEdition.getPub());

      LdoDUser user = LdoDUser.getAuthenticatedUser();
      if (user != null) {
        virtualEditionStream =  Stream.concat(virtualEditionStream, user.getSelectedVirtualEditionsSet().stream()).distinct();
      }
      return virtualEditionStream.collect(Collectors.toMap(VirtualEdition::getAcronym, VirtualEdition::getTitle));
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value = "/reading")
	public List<ExpertEditionDto> startReading() {
    	
		return LdoD.getInstance().getSortedExpertEdition().stream().map(ExpertEditionDto::new)
				.collect(Collectors.toList());
		
	}
    
    @RequestMapping(method = RequestMethod.POST, value = "/edition/{acronym}/start", headers = {
    "Content-type=application/json" })
	public ReadingDto startReadingEdition(@PathVariable String acronym, @RequestBody ReadingRecommendation jsonRecomendation) {
		ExpertEdition expertEdition = (ExpertEdition) LdoD.getInstance().getEdition(acronym);
		ExpertEditionInter expertEditionInter = expertEdition.getFirstInterpretation();
		

		jsonRecomendation.clean();
		jsonRecomendation.setTextWeight(1.0);
		
		return this.readInterpretationJson(expertEditionInter.getFragment().getXmlId(), expertEditionInter.getUrlId(), jsonRecomendation);
	}
    
    @RequestMapping(method = RequestMethod.POST, value = "/fragment/{xmlId}/inter/{urlId}/next")
	public ReadingDto readNextInterpretation(@PathVariable String xmlId, @PathVariable String urlId, @RequestBody ReadingRecommendation jsonRecomendation) {
    	Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
		if (fragment == null) {
			return null;
		}

		ExpertEditionInter expertEditionInter = (ExpertEditionInter) fragment.getFragInterByUrlId(urlId);
		if (expertEditionInter == null) {
			return null;
		}

		FragInter nextExpertEditionInter = expertEditionInter.getEdition().getNextNumberInter(expertEditionInter,
				expertEditionInter.getNumber());

		return this.readInterpretationJson(nextExpertEditionInter.getFragment().getXmlId(), nextExpertEditionInter.getUrlId(), jsonRecomendation);
	}
    
    @RequestMapping(method = RequestMethod.POST, value = "/fragment/{xmlId}/inter/{urlId}/prev")
	public ReadingDto readPrevInterpretation(@PathVariable String xmlId, @PathVariable String urlId, @RequestBody ReadingRecommendation jsonRecomendation) {
    	Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
		if (fragment == null) {
			return null;
		}

		ExpertEditionInter expertEditionInter = (ExpertEditionInter) fragment.getFragInterByUrlId(urlId);
		if (expertEditionInter == null) {
			return null;
		}

		FragInter prevExpertEditionInter = expertEditionInter.getEdition().getPrevNumberInter(expertEditionInter,
				expertEditionInter.getNumber());

		return this.readInterpretationJson(prevExpertEditionInter.getFragment().getXmlId(), prevExpertEditionInter.getUrlId(), jsonRecomendation);
	}

    
    @RequestMapping(method = RequestMethod.POST, value = "reading/fragment/{xmlId}/interJson/{urlId}", headers = {
    "Content-type=application/json" })
	public ReadingDto readInterpretationJson(@PathVariable String xmlId, @PathVariable String urlId, @RequestBody ReadingRecommendation jsonRecomendation) {
		Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);
		if (fragment == null) {
			return null;
		}

		ExpertEditionInter expertEditionInter = (ExpertEditionInter) fragment.getFragInterByUrlId(urlId);
		if (expertEditionInter == null) {
			return null;
		}

		Set<ExpertEditionInter> recommendations = jsonRecomendation.getNextRecommendations(expertEditionInter.getExternalId());
		ExpertEditionInter prevRecom = jsonRecomendation.getPrevRecommendation();
		
		PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(expertEditionInter);
		writer.write(false);
		
		return new ReadingDto(LdoD.getInstance(), expertEditionInter, recommendations, prevRecom, writer, fragment, jsonRecomendation);

	}
    
    @RequestMapping(method = RequestMethod.POST, value = "/inter/prev/recom")
	public ReadingDto readPreviousRecommendedFragment(@RequestBody ReadingRecommendation jsonRecomendation) {

		String expertEditionInterId = jsonRecomendation.prevRecommendation();
		ExpertEditionInter expertEditionInter = FenixFramework.getDomainObject(expertEditionInterId);
		
		return this.readInterpretationJson(expertEditionInter.getFragment().getXmlId(), expertEditionInter.getUrlId(), jsonRecomendation);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/inter/prev/recom/reset")
	public ReadingRecommendation resetPreviousRecommendedFragments(@RequestBody ReadingRecommendation jsonRecomendation) {

		jsonRecomendation.resetPrevRecommendations();
		
		return jsonRecomendation;
	}
		
	@RequestMapping(method = RequestMethod.GET, value = "/citations")
	public List<CitationDto> listCitations() {
		logger.debug("listCitations");
		
		return LdoD.getInstance().getCitationsWithInfoRanges().stream().map(CitationDto::new).collect(Collectors.toList());
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}")
	public FragmentBodyDto getFragment(@PathVariable String xmlId) {
		Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);

		if (fragment == null) {
			return null;
		} else {
			return new FragmentBodyDto(LdoD.getInstance(), LdoDUser.getAuthenticatedUser(), fragment, new ArrayList<FragInter>());
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/fragment/{xmlId}/inter/{urlId}")
	@PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
	public FragmentBodyDto getFragmentWithInterForUrlId(@PathVariable String xmlId, @PathVariable String urlId, @RequestBody ArrayList<String> selectedVE) {

		Fragment fragment = LdoD.getInstance().getFragmentByXmlId(xmlId);

		if (fragment == null) {
			return null;
		}

		FragInter inter = fragment.getFragInterByUrlId(urlId);

		if (inter == null) {
			return null;
		}

		PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getLastUsed());
		writer.write(false);

		boolean hasAccess = true;
		// if it is a virtual interpretation check access and set session
		if (inter.getSourceType() == Edition.EditionType.VIRTUAL) {
			VirtualEdition virtualEdition = (VirtualEdition) inter.getEdition();

			LdoDUser user = LdoDUser.getAuthenticatedUser();
			if (!virtualEdition.checkAccess()) {
				hasAccess = false;
			}
		}

		List<FragInter> inters = new ArrayList<>();
		inters.add(inter);

		return new FragmentBodyDto(LdoD.getInstance(), LdoDUser.getAuthenticatedUser(), fragment, inters, writer, hasAccess, selectedVE);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/fragment/{xmlId}/inter/{urlId}/nextFrag")
	@PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
	public FragmentBodyDto getNextFragmentWithInter(@PathVariable String xmlId, @PathVariable String urlId, @RequestBody ArrayList<String> selectedVE) {
		Fragment fragment = FenixFramework.getDomainRoot().getLdoD().getFragmentByXmlId(xmlId);
		if (fragment == null) {
			return null;
		}

		FragInter inter = fragment.getFragInterByUrlId(urlId);
		if (inter == null) {
			return null;
		}

		Edition edition = inter.getEdition();
		inter = edition.getNextNumberInter(inter, inter.getNumber());

		return this.getFragmentWithInterForUrlId(inter.getFragment().getXmlId(), inter.getUrlId(), selectedVE);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/fragment/{xmlId}/inter/{urlId}/prevFrag")
	@PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
	public FragmentBodyDto getPrevFragmentWithInter(@PathVariable String xmlId, @PathVariable String urlId, @RequestBody ArrayList<String> selectedVE) {
		Fragment fragment = FenixFramework.getDomainRoot().getLdoD().getFragmentByXmlId(xmlId);
		if (fragment == null) {
			return null;
		}

		FragInter inter = fragment.getFragInterByUrlId(urlId);
		if (inter == null) {
			return null;
		}

		Edition edition = inter.getEdition();
		inter = edition.getPrevNumberInter(inter, inter.getNumber());

		return this.getFragmentWithInterForUrlId(inter.getFragment().getXmlId(), inter.getUrlId(), selectedVE);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/fragment/inter")
	public FragmentBodyDto getInter(@RequestParam(value = "fragment", required = true) String externalId,
			@RequestParam(value = "inters[]", required = false) String[] intersID) {

		Fragment fragment = FenixFramework.getDomainObject(externalId);

		List<FragInter> inters = new ArrayList<>();
		System.out.println(intersID);
		if (intersID != null) {
			for (String interID : intersID) {
				FragInter inter = (FragInter) FenixFramework.getDomainObject(interID);
				if (inter != null) {
					inters.add(inter);
				}
			}
		}

		
		HtmlWriter2CompInters writer = null;
		PlainHtmlWriter4OneInter writer4One = null;
		Boolean lineByLine = false;
		Map<FragInter, HtmlWriter4Variations> variations = new HashMap<>();
		List<AppText> apps = new ArrayList<>();

		if (inters.size() == 1) {
			FragInter inter = inters.get(0);
			writer4One = new PlainHtmlWriter4OneInter(inter);
			writer4One.write(false);
		} else if (inters.size() > 1) {
			writer = new HtmlWriter2CompInters(inters);
			if (inters.size() > 2) {
				lineByLine = true;
			}

			
			for (FragInter inter : inters) {
				variations.put(inter, new HtmlWriter4Variations(inter));
			}

			inters.get(0).getFragment().getTextPortion().putAppTextWithVariations(apps, inters);
			Collections.reverse(apps);

			writer.write(lineByLine, false);
		}

		
		return new FragmentBodyDto(LdoD.getInstance(), LdoDUser.getAuthenticatedUser(), fragment, inters, writer, writer4One, variations, apps);

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/fragment/inter/editorial")
	public FragmentBodyDto getInterEditorial(@RequestParam(value = "interp[]", required = true) String[] interID,
			@RequestParam(value = "diff", required = true) boolean displayDiff) {
		FragInter inter = FenixFramework.getDomainObject(interID[0]);

		PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);
		writer.write(displayDiff);

		List<FragInter> inters = new ArrayList<>();
		inters.add(inter);

		return new FragmentBodyDto(inters, writer);

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/fragment/inter/authorial")
	public FragmentBodyDto getInterAuthorial(@RequestParam(value = "interp[]", required = true) String[] interID,
			@RequestParam(value = "diff", required = true) boolean displayDiff,
			@RequestParam(value = "del", required = true) boolean displayDel,
			@RequestParam(value = "ins", required = true) boolean highlightIns,
			@RequestParam(value = "subst", required = true) boolean highlightSubst,
			@RequestParam(value = "notes", required = true) boolean showNotes,
			@RequestParam(value = "facs", required = true) boolean showFacs,
			@RequestParam(value = "pb", required = false) String pbTextID) {
		SourceInter inter = FenixFramework.getDomainObject(interID[0]);
		PbText pbText = null;
		System.out.println(pbTextID);
		if (pbTextID != null && !pbTextID.equals("")) {
			pbText = FenixFramework.getDomainObject(pbTextID);
		}

		PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);

		List<FragInter> inters = new ArrayList<>();
		inters.add(inter);

		if (showFacs) {
			Surface surface = null;
			if (pbText == null) {
				surface = inter.getSource().getFacsimile().getFirstSurface();
			} else {
				surface = pbText.getSurface();
			}

			writer.write(displayDiff, displayDel, highlightIns, highlightSubst, showNotes, showFacs, pbText);
			return new FragmentBodyDto(inters, surface, pbText, writer, inter);

		} else {
			writer.write(displayDiff, displayDel, highlightIns, highlightSubst, showNotes, showFacs, null);
			return new FragmentBodyDto(inters, writer);
		}

	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/fragment/inter/compare")
	public FragmentBodyDto getInterCompare(@RequestParam(value = "inters[]", required = true) String[] intersID,
			@RequestParam(value = "line") boolean lineByLine,
			@RequestParam(value = "spaces", required = true) boolean showSpaces) {
		List<FragInter> inters = new ArrayList<>();
		for (String interID : intersID) {
			inters.add((FragInter) FenixFramework.getDomainObject(interID));
		}

		HtmlWriter2CompInters writer = new HtmlWriter2CompInters(inters);

		if (inters.size() > 2) {
			lineByLine = true;
		}
		writer.write(lineByLine, showSpaces);


		return new FragmentBodyDto(inters, writer);

	}
	
	@RequestMapping(value = "/public/getAllEditions")
    public ResponseEntity<ExpertVirtualDto> getPublicAllEditions() {
		return new ResponseEntity<ExpertVirtualDto>(new ExpertVirtualDto(LdoD.getInstance().getExpertEditionsSet(), LdoD.getInstance().getVirtualEditionsSet(), LdoD.getInstance().getArchiveEdition()), HttpStatus.OK);
		
    }
	
	@RequestMapping(value = "/getAllEditions")
    public ResponseEntity<ExpertVirtualDto> getAllEditions(@AuthenticationPrincipal LdoDUserDetails currentUser) {
		LdoDUser user = currentUser.getUser();
		return new ResponseEntity<ExpertVirtualDto>(new ExpertVirtualDto(LdoD.getInstance().getExpertEditionsSet(), LdoD.getInstance().getVirtualEditionsSet(), LdoD.getInstance().getArchiveEdition(), user), HttpStatus.OK);
		
    }
	
	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/submit")
	public ResponseEntity<ExpertVirtualDto> submitParticipation(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable(value = "externalId") String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		LdoDUser user = currentUser.getUser();

		if (virtualEdition == null || user == null) {
			return null;
		} else {
			virtualEdition.addMember(user, MemberRole.MEMBER, false);
			return getAllEditions(currentUser);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/cancel")
	public ResponseEntity<ExpertVirtualDto> cancelParticipationSubmission(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable(value = "externalId") String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		LdoDUser user = currentUser.getUser();

		if (virtualEdition == null || user == null) {
			return null;
		} else {
			virtualEdition.cancelParticipationSubmission(user);
			return getAllEditions(currentUser);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/restricted/create/{acronym}/{title}/{pub}/{use}")
	public ResponseEntity<ExpertVirtualDto> createVirtualEdition(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable(value = "acronym") String acronym, @PathVariable(value = "title") String title,
			@PathVariable(value = "pub") boolean pub, @PathVariable(value = "use") String editionID) {

		Edition usedEdition = null;
		if (!editionID.equals("no")) {
			usedEdition = FenixFramework.getDomainObject(editionID);
		}

		LocalDate date = new LocalDate();

		title = title.trim();
		acronym = acronym.trim();

		VirtualEdition virtualEdition = null;

		VirtualEditionValidator validator = new VirtualEditionValidator(virtualEdition, acronym, title);
		validator.validate();

		try {
			LdoD.getInstance().createVirtualEdition(currentUser.getUser(),
					VirtualEdition.ACRONYM_PREFIX + acronym, title, date, pub, usedEdition);
			return getAllEditions(currentUser);

		} catch (LdoDDuplicateAcronymException ex) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/restricted/manage/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public ResponseEntity<VirtualEditionDto> manageVirtualEdition(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		logger.debug("manageVirtualEdition externalId:{}", externalId);
		List<String> countriesList = new ArrayList<String>();
		countriesList.add("Portugal");
		countriesList.add("Brazil");
		countriesList.add("Spain");
		countriesList.add("United Kingdom");
		countriesList.add("United States");
		countriesList.add("Lebanon");
		countriesList.add("Angola");
		countriesList.add("Mozambique");
		if (virtualEdition == null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} else {			
			return new ResponseEntity<VirtualEditionDto>(new VirtualEditionDto(virtualEdition, currentUser.getUser(), countriesList), HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/edit/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
	public ResponseEntity<VirtualEditionDto> editVirtualEdition(@AuthenticationPrincipal LdoDUserDetails currentUser,
			@PathVariable String externalId, @RequestParam("acronym") String acronym,
			@RequestParam("title") String title, @RequestParam("synopsis") String synopsis,
			@RequestParam("pub") boolean pub, @RequestParam("management") boolean management,
			@RequestParam("vocabulary") boolean vocabulary, @RequestParam("annotation") boolean annotation,
			@RequestParam("mediasource") String mediaSource, @RequestParam("begindate") String beginDate,
			@RequestParam("enddate") String endDate, @RequestParam("geolocation") String geoLocation,
			@RequestParam("frequency") String frequency) {

		logger.info("mediaSource:{}", mediaSource);
		logger.info("beginDate:{}", beginDate);
		logger.info("endDate:{}", endDate);
		logger.info("geoLocation:{}", geoLocation);
		logger.info("frequency:{}", frequency);

		logger.debug(
				"editVirtualEdition externalId:{}, acronym:{}, title:{}, pub:{}, management:{}, vocabulary:{}, annotation:{}",
				externalId, acronym, title, pub, management, vocabulary, annotation);

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		title = title.trim();
		acronym = acronym.trim();

		VirtualEditionValidator validator = new VirtualEditionValidator(virtualEdition, acronym, title);
		validator.validate();

		List<String> errors = validator.getErrors();

		if (errors.size() > 0) {
			System.out.println(errors);
			System.out.println("OLAAAAAAAAAAAAAA");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		try {
			virtualEdition.edit(VirtualEdition.ACRONYM_PREFIX + acronym, title, synopsis, pub, management, vocabulary,
					annotation, mediaSource, beginDate, endDate, geoLocation, frequency);
		} catch (LdoDDuplicateAcronymException ex) {
			errors.add("virtualedition.acronym.duplicate");
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		AwareAnnotationFactory awareFactory = new AwareAnnotationFactory();
		if (virtualEdition.isSAVE()) {
			awareFactory.searchForAwareAnnotations(virtualEdition);
		}
		// this virtual edition is not SAVE anymore, therefore we have to remove all the
		// aware annotations
		else {
			for (VirtualEditionInter inter : virtualEdition.getAllDepthVirtualEditionInters()) {
				awareFactory.removeAllAwareAnnotationsFromVEInter(inter);
			}
		}

		List<String> countriesList = new ArrayList<String>();
		countriesList.add("Portugal");
		countriesList.add("Brazil");
		countriesList.add("Spain");
		countriesList.add("United Kingdom");
		countriesList.add("United States");
		countriesList.add("Lebanon");
		countriesList.add("Angola");
		countriesList.add("Mozambique");
		return new ResponseEntity<VirtualEditionDto>(new VirtualEditionDto(virtualEdition, currentUser.getUser(), countriesList), HttpStatus.OK);
	}

	
	@RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/participants")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public ResponseEntity<VirtualEditionDto> showParticipants(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} else {
			return new ResponseEntity<VirtualEditionDto>(new VirtualEditionDto(virtualEdition, currentUser.getUser(), 1), HttpStatus.OK);
		}
	}

	
	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/approve")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
	public ResponseEntity<VirtualEditionDto> approveParticipant(@AuthenticationPrincipal LdoDUserDetails currentUser,
			@PathVariable("externalId") String externalId, @RequestParam("username") String username) {

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return null;
		}
		LdoD ldoD = LdoD.getInstance();
		LdoDUser user = ldoD.getUser(username);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			virtualEdition.addApprove(user);
			return new ResponseEntity<VirtualEditionDto>(new VirtualEditionDto(virtualEdition, currentUser.getUser(), 1), HttpStatus.OK);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/add")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
	public ResponseEntity<VirtualEditionDto> addParticipant(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable("externalId") String externalId,
			@RequestParam("username") String username) {

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return null;
		}
		LdoD ldoD = LdoD.getInstance();
		LdoDUser user = ldoD.getUser(username);
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			virtualEdition.addMember(user, MemberRole.MEMBER, true);
			return new ResponseEntity<VirtualEditionDto>(new VirtualEditionDto(virtualEdition, currentUser.getUser(), 1), HttpStatus.OK);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/role")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.admin')")
	public ResponseEntity<VirtualEditionDto> switchRole(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable("externalId") String externalId,
			@RequestParam("username") String username) {

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return null;
		}

		LdoD ldoD = LdoD.getInstance();
		LdoDUser user = ldoD.getUser(username);

		if (!virtualEdition.canSwitchRole(LdoDUser.getAuthenticatedUser(), user)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		virtualEdition.switchRole(user);
		return new ResponseEntity<VirtualEditionDto>(new VirtualEditionDto(virtualEdition, currentUser.getUser(), 1), HttpStatus.OK);

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/participants/remove")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public ResponseEntity<VirtualEditionDto> removeParticipant(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable("externalId") String externalId,
			@RequestParam("userId") String userId) {
		logger.debug("removeParticipant userId:{}", userId);

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		LdoDUser user = FenixFramework.getDomainObject(userId);

		if (virtualEdition == null || user == null) {
			return null;
		}

		if (!virtualEdition.canRemoveMember(currentUser.getUser(), user)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		LdoDUser admin = null;
		if (virtualEdition.getAdminSet().size() == 1) {
			admin = virtualEdition.getAdminSet().iterator().next();
		}

		if (admin != null && admin == user) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		} else {
			virtualEdition.removeMember(user);

			if (user == currentUser.getUser()) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<VirtualEditionDto>(new VirtualEditionDto(virtualEdition, currentUser.getUser(), 1), HttpStatus.OK);
			}
		}
	}


	@RequestMapping(method = RequestMethod.GET, value = "/restricted/recommendation/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public VirtualRecommendationDto presentEditionWithRecommendation(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String externalId) {
		// logger.debug("presentEditionWithRecommendation");

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return null;
		} else {


			RecommendationWeights recommendationWeights = currentUser.getUser()
					.getRecommendationWeights(virtualEdition);

			recommendationWeights.setWeightsZero();


			if (!virtualEdition.getAllDepthVirtualEditionInters().isEmpty()) {
				VirtualEditionInter inter = virtualEdition.getAllDepthVirtualEditionInters().get(0);

				List<VirtualEditionInter> recommendedEdition = virtualEdition.generateRecommendation(inter,
						recommendationWeights);

				return new VirtualRecommendationDto(virtualEdition, recommendedEdition, inter.getExternalId());
			}

			return new VirtualRecommendationDto(virtualEdition);
		}
	}
	
	@RequestMapping(value = "/linear", method = RequestMethod.POST, headers = {
		"Content-type=application/json;charset=UTF-8" })
	public VirtualRecommendationDto setLinearVirtualEdition(@AuthenticationPrincipal LdoDUserDetails currentUser, @RequestBody RecommendVirtualEditionParam params) {
	logger.debug("setLinearVirtualEdition acronym:{}, id:{}, properties:{}", params.getAcronym(), params.getId(),
			params.getProperties().stream()
					.map(p -> p.getClass().getName().substring(p.getClass().getName().lastIndexOf(".") + 1) + " "
							+ p.getWeight())
					.collect(Collectors.joining(";")));
	
	VirtualEdition virtualEdition = (VirtualEdition) LdoD.getInstance().getEdition(params.getAcronym());
	
	LdoDUser user = currentUser.getUser();
	RecommendationWeights recommendationWeights = user.getRecommendationWeights(virtualEdition);
	recommendationWeights.setWeights(params.getProperties());
	
	if(params.getId() != null && !params.getId().equals("")) {
		VirtualEditionInter inter = FenixFramework.getDomainObject(params.getId());
	
		List<VirtualEditionInter> recommendedEdition = virtualEdition.generateRecommendation(inter,
				recommendationWeights);
	
		return new VirtualRecommendationDto(virtualEdition, recommendedEdition);
	}
	
	return new VirtualRecommendationDto(virtualEdition);
	}
	
	@RequestMapping(value = "/linear/save", method = RequestMethod.POST)
	@PreAuthorize("hasPermission(#acronym, 'editionacronym.participant')")
	public VirtualRecommendationDto saveLinearVirtualEdition(@AuthenticationPrincipal LdoDUserDetails currentUser, @RequestParam("acronym") String acronym,
			@RequestParam(value = "inter[]", required = false) String[] inters) {
		// logger.debug("saveLinearVirtualEdition");

		LdoD ldod = LdoD.getInstance();
		VirtualEdition virtualEdition = (VirtualEdition) ldod.getEdition(acronym);
		if (inters != null && virtualEdition.getSourceType().equals(EditionType.VIRTUAL)) {
			Section section = virtualEdition.createSection(Section.DEFAULT);
			VirtualEditionInter VirtualEditionInter;
			int i = 0;
			for (String externalId : inters) {
				VirtualEditionInter = FenixFramework.getDomainObject(externalId);
				section.addVirtualEditionInter(VirtualEditionInter, ++i);
			}
			virtualEdition.clearEmptySections();
		}

		return new VirtualRecommendationDto(virtualEdition);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/restricted/manual/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public VirtualEditionDto showEditVirtualEdition(@PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return null;
		}
		logger.debug("showEditVirtualEditionn externalId:{}, acronym:{}, title:{}, pub:{}, fraginters.size:{}",
				externalId, virtualEdition.getAcronym(), virtualEdition.getTitle(), virtualEdition.getPub(),
				virtualEdition.getIntersSet().size());

		return new VirtualEditionDto(virtualEdition);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/restricted/reorder/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public VirtualEditionDto reorderVirtualEdition(@AuthenticationPrincipal LdoDUserDetails currentUser,
			@PathVariable String externalId, @RequestParam("fraginters") String fraginters) {
		logger.debug("reorderVirtualEdition externalId:{}, fraginters:{}", externalId, fraginters);

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return null;
		}

		virtualEdition.updateVirtualEditionInters(fraginters);

		return new VirtualEditionDto(virtualEdition);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/restricted/{externalId}/taxonomy")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public VirtualTaxonomyDto taxonomy(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String externalId) {
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		LdoDUser user = currentUser.getUser();
		if (virtualEdition == null) {
			return null;
		} else {
			return new VirtualTaxonomyDto(virtualEdition, user);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/create")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.taxonomy')")
	public VirtualTaxonomyDto createCategory(@AuthenticationPrincipal LdoDUserDetails currentUser, @RequestParam("externalId") String externalId,
			@RequestParam("name") String name) {
		VirtualEdition edition = FenixFramework.getDomainObject(externalId);
		LdoDUser user = currentUser.getUser();
		List<String> errors = new ArrayList<>();
		if (edition == null) {
			return null;
		} else {
			try {
				edition.getTaxonomy().createCategory(name);
			} catch (LdoDDuplicateNameException ex) {
				errors.add("general.category.exists");
			}

			if (errors.isEmpty()) {
				return new VirtualTaxonomyDto(edition, user);
			} else {
				return null;
			}
		}
	}
	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/delete")
	@PreAuthorize("hasPermission(#categoryId, 'category.taxonomy')")
	public VirtualTaxonomyDto deleteCategory(@AuthenticationPrincipal LdoDUserDetails currentUser, @RequestParam("categoryId") String categoryId) {
		Category category = FenixFramework.getDomainObject(categoryId);
		LdoDUser user = currentUser.getUser();
		if (category == null) {
			return null;
		}
		VirtualEdition virtualEdition = category.getTaxonomy().getEdition();
		try {
			category.remove();
		} catch (LdoDException ex) {
			return null;
		}
		return new VirtualTaxonomyDto(virtualEdition, user);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/mulop")
	@PreAuthorize("hasPermission(#taxonomyId, 'taxonomy.taxonomy')")
	public VirtualTaxonomyDto mergeCategories(@AuthenticationPrincipal LdoDUserDetails currentUser, @RequestParam("taxonomyId") String taxonomyId,
			@RequestParam("type") String type, @RequestParam("externalId") String externalId,
			@RequestParam(value = "categories[]", required = false) String categoriesIds[]) {
		Taxonomy taxonomy = FenixFramework.getDomainObject(taxonomyId);
		LdoDUser user = currentUser.getUser();
		VirtualEdition edition = FenixFramework.getDomainObject(externalId);
		if (taxonomy == null) {
			return null;
		}

		if (categoriesIds == null) {
			return null;
		}

		List<Category> categories = new ArrayList<>();
		for (String categoryId : categoriesIds) {
			Category category = FenixFramework.getDomainObject(categoryId);
			categories.add(category);
		}

		if (type.equals("merge") && categories.size() > 1) {
			taxonomy.merge(categories);
			return new VirtualTaxonomyDto(edition, user);
		}

		else if (type.equals("delete") && categories.size() >= 1) {
			taxonomy.delete(categories);
			return new VirtualTaxonomyDto(edition, user);
		}
		return new VirtualTaxonomyDto(edition, user);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/taxonomy/generateTopics")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.taxonomy')")
	public TopicListDTO generateTopicModelling(@AuthenticationPrincipal LdoDUserDetails currentUser,
			@PathVariable String externalId, @RequestParam("numTopics") int numTopics,
			@RequestParam("numWords") int numWords, @RequestParam("thresholdCategories") int thresholdCategories,
			@RequestParam("numIterations") int numIterations) throws IOException {
		logger.debug(
				"generateTopicModelling externalId:{}, numTopics:{}, numWords:{}, thresholdCategories:{}, numIterations:{}",
				externalId, numTopics, numWords, thresholdCategories, numIterations);

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return null;
		} else {
			
			TopicListDTO topicListDTO = null;
			TopicModeler modeler = new TopicModeler();
			try {
				topicListDTO = modeler.generate(currentUser.getUser(), virtualEdition, numTopics, numWords,
						thresholdCategories, numIterations);
			} catch (LdoDException ex) {
				return null;
			}

			return topicListDTO;
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/taxonomy/createTopics")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.taxonomy')")
	public VirtualTaxonomyDto createTopicModelling(@AuthenticationPrincipal LdoDUserDetails currentUser,
			@PathVariable String externalId, @RequestBody TopicListDTO topicList) throws IOException {
		logger.debug("createTopicModelling externalId:{}, username:{}", externalId, topicList.getUsername());

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return null;
		} else {
			Taxonomy taxonomy = virtualEdition.getTaxonomy();
			try {
				taxonomy.createGeneratedCategories(topicList);
			} catch (LdoDException ex) {
				return null;
			}
			

			return new VirtualTaxonomyDto(virtualEdition, currentUser.getUser());
		}

	}

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/{externalId}/taxonomy/clean")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.taxonomy')")
	public VirtualTaxonomyDto deleteTaxonomy(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable("externalId") String externalId,
			@RequestParam("taxonomyExternalId") String taxonomyExternalId) {
		Taxonomy taxonomy = FenixFramework.getDomainObject(taxonomyExternalId);
		if (taxonomy == null) {
			return null;
		} else {
			VirtualEdition edition = taxonomy.getEdition();
			taxonomy.remove();
			edition.setTaxonomy(new Taxonomy());

			return new VirtualTaxonomyDto(edition, currentUser.getUser());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/restricted/category/{categoryId}")
	@PreAuthorize("hasPermission(#categoryId, 'category.participant')")
	public CategoryDto showCategory(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String categoryId) {
		Category category = FenixFramework.getDomainObject(categoryId);
		if (category == null) {
			return null;
		} else {
			return new CategoryDto(category, currentUser.getUser());
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/update")
	@PreAuthorize("hasPermission(#categoryId, 'category.taxonomy')")
	public CategoryDto updateCategoryName(@AuthenticationPrincipal LdoDUserDetails currentUser,
			@RequestParam("categoryId") String categoryId, @RequestParam("name") String name) {
		Category category = FenixFramework.getDomainObject(categoryId);
		if (category == null) {
			return null;
		} else {
			try {
				category.setName(name);
			} catch (LdoDDuplicateNameException ex) {
				return null;
			}
			return new CategoryDto(category, currentUser.getUser());
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/restricted/fraginter/{fragInterId}")
	@PreAuthorize("hasPermission(#fragInterId, 'fragInter.participant')")
	public FragInterDto showFragmentInterpretation(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String fragInterId) {
		FragInter fragInter = FenixFramework.getDomainObject(fragInterId);
		if (fragInter == null) {
			return null;
		} else {
			return new FragInterDto(fragInter);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/restricted/category/extract")
	@PreAuthorize("hasPermission(#categoryId, 'category.taxonomy')")
	public String extractCategory(@AuthenticationPrincipal LdoDUserDetails currentUser, @RequestParam("categoryId") String categoryId,
			@RequestParam(value = "inters[]", required = false) String interIds[]) {
		Category category = FenixFramework.getDomainObject(categoryId);
		if (category == null) {
			return null;
		}

		if (interIds == null || interIds.length == 0) {
			return null;
		}

		Set<VirtualEditionInter> inters = new HashSet<>();
		for (String interId : interIds) {
			VirtualEditionInter inter = FenixFramework.getDomainObject(interId);
			inters.add(inter);
		}
		Category extractedCategory = null;
		try {
			extractedCategory = category.getTaxonomy().extract(category, inters);
		}
		catch (LdoDDuplicateNameException ex) {
			return null;
		}
		
		if(extractedCategory == null) {
			return null;
		} else {
			return extractedCategory.getExternalId();
		}
		
	}

}
