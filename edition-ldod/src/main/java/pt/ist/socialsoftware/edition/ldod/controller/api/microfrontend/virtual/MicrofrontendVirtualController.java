package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.virtual;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.CategoryDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.ExpertVirtualDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.FragInterDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.VirtualEditionDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.VirtualRecommendationDto;
import pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto.VirtualTaxonomyDto;
import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.RecommendationWeights;
import pt.ist.socialsoftware.edition.ldod.domain.Section;
import pt.ist.socialsoftware.edition.ldod.domain.Taxonomy;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.Edition.EditionType;
import pt.ist.socialsoftware.edition.ldod.domain.Member.MemberRole;
import pt.ist.socialsoftware.edition.ldod.recommendation.dto.RecommendVirtualEditionParam;
import pt.ist.socialsoftware.edition.ldod.security.LdoDUserDetails;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateAcronymException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDDuplicateNameException;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.social.aware.AwareAnnotationFactory;
import pt.ist.socialsoftware.edition.ldod.topicmodeling.TopicModeler;
import pt.ist.socialsoftware.edition.ldod.utils.TopicListDTO;
import pt.ist.socialsoftware.edition.ldod.validator.VirtualEditionValidator;

@RestController
@RequestMapping("/api/microfrontend/virtual")
public class MicrofrontendVirtualController {
	
	private static Logger logger = LoggerFactory.getLogger(MicrofrontendVirtualController.class);
	
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
	
	@RequestMapping(value = "/linear/{externalId}", method = RequestMethod.POST, headers = {
		"Content-type=application/json;charset=UTF-8" })
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public VirtualRecommendationDto setLinearVirtualEdition(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String externalId, @RequestBody RecommendVirtualEditionParam params) {
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
	
	@RequestMapping(value = "/linear/save/{externalId}", method = RequestMethod.POST)
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public VirtualRecommendationDto saveLinearVirtualEdition(@AuthenticationPrincipal LdoDUserDetails currentUser, @PathVariable String externalId, @RequestParam("acronym") String acronym,
			@RequestParam(value = "inter[]", required = false) String[] inters) {
		// logger.debug("saveLinearVirtualEdition");

		LdoD ldod = LdoD.getInstance();
		VirtualEdition virtualEdition = (VirtualEdition) ldod.getEdition(acronym);
		if (inters != null && virtualEdition.getSourceType().equals(EditionType.VIRTUAL)) {
			Section section = virtualEdition.createSection(Section.DEFAULT);
			VirtualEditionInter VirtualEditionInter;
			int i = 0;
			for (String externalId1 : inters) {
				VirtualEditionInter = FenixFramework.getDomainObject(externalId1);
				section.addVirtualEditionInter(VirtualEditionInter, ++i);
			}
			virtualEdition.clearEmptySections();
		}

		return new VirtualRecommendationDto(virtualEdition);
	}
	

	@RequestMapping(method = RequestMethod.POST, value = "/restricted/reorder/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public VirtualRecommendationDto reorderVirtualEdition(@AuthenticationPrincipal LdoDUserDetails currentUser,
			@PathVariable String externalId, @RequestParam("fraginters") String fraginters) {
		logger.debug("reorderVirtualEdition externalId:{}, fraginters:{}", externalId, fraginters);

		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return null;
		}

		virtualEdition.updateVirtualEditionInters(fraginters);

		return new VirtualRecommendationDto(virtualEdition);
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
	
	@RequestMapping(method = RequestMethod.POST, value = "/restricted/delete")
	@PreAuthorize("hasPermission(#externalId, 'virtualedition.participant')")
	public ResponseEntity<Object> deleteVirtualEdition(@AuthenticationPrincipal LdoDUserDetails currentUser, @RequestParam("externalId") String externalId) {
		logger.debug("deleteVirtualEdition externalId:{}", externalId);
		VirtualEdition virtualEdition = FenixFramework.getDomainObject(externalId);
		if (virtualEdition == null) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		} else {

			virtualEdition.remove();

			return new ResponseEntity<>(HttpStatus.OK);
		}
	}

}
