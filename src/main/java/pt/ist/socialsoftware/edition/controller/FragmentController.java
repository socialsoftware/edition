package pt.ist.socialsoftware.edition.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.Annotation;
import pt.ist.socialsoftware.edition.domain.AppText;
import pt.ist.socialsoftware.edition.domain.Category;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.PbText;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.Surface;
import pt.ist.socialsoftware.edition.domain.TextPortion;
import pt.ist.socialsoftware.edition.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.security.LdoDSession;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.utils.AnnotationDTO;
import pt.ist.socialsoftware.edition.utils.AnnotationSearchJson;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter4Variations;
import pt.ist.socialsoftware.edition.visitors.PlainHtmlWriter4OneInter;

@Controller
@SessionAttributes({ "ldoDSession" })
@RequestMapping("/fragments/fragment")
public class FragmentController {
	private static Logger logger = LoggerFactory.getLogger(FragmentController.class);

	@ModelAttribute("ldoDSession")
	public LdoDSession getLdoDSession() {
		LdoDSession ldoDSession = new LdoDSession();

		System.out.println("VirtualEditionController:getLdoDSession()");

		LdoDUser user = LdoDUser.getAuthenticatedUser();
		if (user != null) {
			for (VirtualEdition virtualEdition : user.getSelectedVirtualEditionsSet()) {
				ldoDSession.addSelectedVE(virtualEdition);
			}
		}
		return ldoDSession;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public String getFragment(Model model, @PathVariable String id) {
		Fragment fragment = FenixFramework.getDomainObject(id);

		if (fragment == null) {
			return "utils/pageNotFound";
		} else {
			model.addAttribute("ldoD", LdoD.getInstance());
			model.addAttribute("user", LdoDUser.getAuthenticatedUser());
			model.addAttribute("fragment", fragment);
			model.addAttribute("inters", new ArrayList<FragInter>());
			return "fragment/main";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'fragInter.public')")
	public String getFragmentWithInter(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String externalId) {
		logger.debug("getFragmentWithInter externalId:{}", externalId);

		FragInter inter = FenixFramework.getDomainObject(externalId);

		if (inter == null) {
			return "util/pageNotFound";
		}

		PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter.getLastUsed());
		writer.write(false);

		// if it is a virtual interpretation check access and set session
		if (inter.getSourceType() == Edition.EditionType.VIRTUAL) {
			VirtualEdition virtualEdition = (VirtualEdition) inter.getEdition();

			LdoDUser user = LdoDUser.getAuthenticatedUser();
			if (virtualEdition.checkAccess()) {
				if (!ldoDSession.hasSelectedVE(virtualEdition.getAcronym())) {
					ldoDSession.toggleSelectedVirtualEdition(user, virtualEdition);
				}
			} else {
				// TODO: a userfriendly reimplementation
				throw new LdoDException("Não tem acesso a esta edição virtual");
			}
		}

		List<FragInter> inters = new ArrayList<FragInter>();
		inters.add(inter);
		model.addAttribute("ldoD", LdoD.getInstance());
		model.addAttribute("user", LdoDUser.getAuthenticatedUser());
		model.addAttribute("fragment", inter.getFragment());
		model.addAttribute("inters", inters);
		model.addAttribute("writer", writer);

		return "fragment/main";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/{externalId}/taxonomy")
	@PreAuthorize("hasPermission(#externalId, 'fragInter.public')")
	public String getTaxonomy(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String externalId) {
		logger.debug("getTaxonomy externalId:{}", externalId);

		VirtualEditionInter inter = FenixFramework.getDomainObject(externalId);

		if (inter == null) {
			return "util/pageNotFound";
		}

		if (inter.getSourceType() != Edition.EditionType.VIRTUAL) {
			return "util/pageNotFound";
		}

		VirtualEdition virtualEdition = (VirtualEdition) inter.getEdition();

		LdoDUser user = LdoDUser.getAuthenticatedUser();
		if (virtualEdition.checkAccess()) {
			if (!ldoDSession.hasSelectedVE(virtualEdition.getAcronym())) {
				ldoDSession.toggleSelectedVirtualEdition(user, virtualEdition);
			}
		} else {
			// TODO: a userfriendly reimplementation
			throw new LdoDException("Não tem acesso a esta edição virtual");
		}

		List<FragInter> inters = new ArrayList<FragInter>();
		inters.add(inter);
		model.addAttribute("ldoD", LdoD.getInstance());
		model.addAttribute("user", LdoDUser.getAuthenticatedUser());
		model.addAttribute("inters", inters);

		return "fragment/taxonomy";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/next/number/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'fragInter.public')")
	public String getNextFragmentWithInter(Model model, @PathVariable String externalId) {

		FragInter inter = FenixFramework.getDomainObject(externalId);

		Edition edition = inter.getEdition();
		inter = edition.getNextNumberInter(inter, inter.getNumber());

		return "redirect:/fragments/fragment/inter/" + inter.getExternalId();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/prev/number/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'fragInter.public')")
	public String getPrevFragmentWithInter(Model model, @PathVariable String externalId) {
		FragInter inter = FenixFramework.getDomainObject(externalId);

		Edition edition = inter.getEdition();
		inter = edition.getPrevNumberInter(inter, inter.getNumber());

		return "redirect:/fragments/fragment/inter/" + inter.getExternalId();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter")
	public String getInter(Model model, @RequestParam(value = "fragment", required = true) String externalId,
			@RequestParam(value = "inters[]", required = false) String[] intersID) {

		Fragment fragment = FenixFramework.getDomainObject(externalId);

		List<FragInter> inters = new ArrayList<FragInter>();
		if (intersID != null) {
			for (String interID : intersID) {
				FragInter inter = (FragInter) FenixFramework.getDomainObject(interID);
				if (inter != null) {
					inters.add(inter);
				}
			}
		}

		model.addAttribute("ldoD", LdoD.getInstance());
		model.addAttribute("user", LdoDUser.getAuthenticatedUser());
		model.addAttribute("fragment", fragment);
		model.addAttribute("inters", inters);

		if (inters.size() == 1) {
			FragInter inter = inters.get(0);
			PlainHtmlWriter4OneInter writer4One = new PlainHtmlWriter4OneInter(inter);
			writer4One.write(false);
			model.addAttribute("writer", writer4One);
		} else if (inters.size() > 1) {
			HtmlWriter2CompInters writer = new HtmlWriter2CompInters(inters);
			Boolean lineByLine = false;
			if (inters.size() > 2) {
				lineByLine = true;
			}

			Map<FragInter, HtmlWriter4Variations> variations = new HashMap<FragInter, HtmlWriter4Variations>();
			for (FragInter inter : inters) {
				variations.put(inter, new HtmlWriter4Variations(inter));
			}

			List<AppText> apps = new ArrayList<AppText>();
			for (TextPortion text : inters.get(0).getFragment().getTextPortion().getChildTextSet()) {
				text.putAppTextWithVariations(apps, inters);
			}
			Collections.reverse(apps);

			writer.write(lineByLine, false);
			model.addAttribute("lineByLine", lineByLine);
			model.addAttribute("writer", writer);
			model.addAttribute("variations", variations);
			model.addAttribute("apps", apps);
		}

		return "fragment/main";

	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/editorial")
	public String getInterEditorial(@RequestParam(value = "interp[]", required = true) String[] interID,
			@RequestParam(value = "diff", required = true) boolean displayDiff, Model model) {
		FragInter inter = FenixFramework.getDomainObject(interID[0]);

		PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);
		writer.write(displayDiff);

		List<FragInter> inters = new ArrayList<FragInter>();
		inters.add(inter);
		model.addAttribute("inters", inters);
		model.addAttribute("writer", writer);

		return "fragment/transcription";

	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/authorial")
	public String getInterAuthorial(@RequestParam(value = "interp[]", required = true) String[] interID,
			@RequestParam(value = "diff", required = true) boolean displayDiff,
			@RequestParam(value = "del", required = true) boolean displayDel,
			@RequestParam(value = "ins", required = true) boolean highlightIns,
			@RequestParam(value = "subst", required = true) boolean highlightSubst,
			@RequestParam(value = "notes", required = true) boolean showNotes,
			@RequestParam(value = "facs", required = true) boolean showFacs,
			@RequestParam(value = "pb", required = false) String pbTextID, Model model) {
		SourceInter inter = FenixFramework.getDomainObject(interID[0]);
		PbText pbText = null;
		if ((pbTextID != null) && !pbTextID.equals("")) {
			pbText = FenixFramework.getDomainObject(pbTextID);
		}

		PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);

		List<FragInter> inters = new ArrayList<FragInter>();
		inters.add(inter);
		model.addAttribute("inters", inters);
		

		if (showFacs) {
			Surface surface = null;
			if (pbText == null) {
				surface = inter.getSource().getFacsimile().getFirstSurface();
			} else {
				surface = pbText.getSurface();
			}

			writer.write(displayDiff, displayDel, highlightIns, highlightSubst, showNotes, showFacs, pbText);
			model.addAttribute("surface", surface);
			model.addAttribute("prevsurface", inter.getPrevSurface(pbText));
			model.addAttribute("nextsurface", inter.getNextSurface(pbText));
			model.addAttribute("prevpb", inter.getPrevPbText(pbText));
			model.addAttribute("nextpb", inter.getNextPbText(pbText));
			model.addAttribute("writer", writer);
			
			return "fragment/facsimile";
		} else {
			writer.write(displayDiff, displayDel, highlightIns, highlightSubst, showNotes, showFacs, null);
			model.addAttribute("writer", writer);
			return "fragment/transcription";
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/compare")
	public String getInterCompare(@RequestParam(value = "inters[]", required = true) String[] intersID,
			@RequestParam(value = "line") boolean lineByLine,
			@RequestParam(value = "spaces", required = true) boolean showSpaces, Model model) {
		List<FragInter> inters = new ArrayList<FragInter>();
		for (String interID : intersID) {
			inters.add((FragInter) FenixFramework.getDomainObject(interID));
		}

		HtmlWriter2CompInters writer = new HtmlWriter2CompInters(inters);

		if (inters.size() > 2) {
			lineByLine = true;
		}
		writer.write(lineByLine, showSpaces);

		model.addAttribute("fragment", inters.get(0).getFragment());
		model.addAttribute("lineByLine", lineByLine);
		model.addAttribute("inters", inters);
		model.addAttribute("writer", writer);

		if (lineByLine) {
			return "fragment/inter2CompareLineByLine";
		} else {
			return "fragment/inter2CompareSideBySide";
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/search")
	public @ResponseBody AnnotationSearchJson searchAnnotations(Model model, @RequestParam int limit,
			@RequestParam String uri) {
		logger.debug("searchAnnotations limit:{}, uri:{}", limit, uri);

		List<AnnotationDTO> annotations = new ArrayList<AnnotationDTO>();

		VirtualEditionInter inter = FenixFramework.getDomainObject(uri);

		for (Annotation annotation : inter.getAllDepthAnnotations()) {
			AnnotationDTO annotationJson = new AnnotationDTO(annotation);
			annotations.add(annotationJson);
		}

		return new AnnotationSearchJson(annotations);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/annotations")
	public @ResponseBody ResponseEntity<AnnotationDTO> createAnnotation(Model model,
			@RequestBody final AnnotationDTO annotationJson, HttpServletRequest request) {
		VirtualEditionInter inter = FenixFramework.getDomainObject(annotationJson.getUri());
		VirtualEdition virtualEdition = (VirtualEdition) inter.getEdition();
		LdoDUser user = LdoDUser.getAuthenticatedUser();

		Annotation annotation;
		if (Annotation.canCreate(virtualEdition, user)) {
			annotation = inter.createAnnotation(annotationJson.getQuote(), annotationJson.getText(), user,
					annotationJson.getRanges(), annotationJson.getTags());

			annotationJson.setId(annotation.getExternalId());

			return new ResponseEntity<AnnotationDTO>(new AnnotationDTO(annotation), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<AnnotationDTO>(HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/annotations/{id}")
	public @ResponseBody ResponseEntity<AnnotationDTO> getAnnotation(Model model, @PathVariable String id) {
		Annotation annotation = FenixFramework.getDomainObject(id);
		if (annotation != null) {
			return new ResponseEntity<AnnotationDTO>(new AnnotationDTO(annotation), HttpStatus.OK);
		} else {
			return new ResponseEntity<AnnotationDTO>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/annotations/{id}")
	public @ResponseBody ResponseEntity<AnnotationDTO> updateAnnotation(Model model, @PathVariable String id,
			@RequestBody final AnnotationDTO annotationJson) {
		Annotation annotation = FenixFramework.getDomainObject(id);
		LdoDUser user = LdoDUser.getAuthenticatedUser();

		if (annotation == null)
			return new ResponseEntity<AnnotationDTO>(HttpStatus.NOT_FOUND);

		if (annotation.canUpdate(user)) {
			annotation.update(annotationJson.getText(), annotationJson.getTags());
			return new ResponseEntity<AnnotationDTO>(new AnnotationDTO(annotation), HttpStatus.OK);
		} else {
			return new ResponseEntity<AnnotationDTO>(HttpStatus.UNAUTHORIZED);
		}

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/annotations/{id}")
	public @ResponseBody ResponseEntity<AnnotationDTO> deleteAnnotation(Model model, @PathVariable String id,
			@RequestBody final AnnotationDTO annotationJson) {
		Annotation annotation = FenixFramework.getDomainObject(id);
		LdoDUser user = LdoDUser.getAuthenticatedUser();

		if (annotation == null)
			return new ResponseEntity<AnnotationDTO>(HttpStatus.NOT_FOUND);

		if (annotation.canDelete(user)) {
			annotation.remove();
			return new ResponseEntity<AnnotationDTO>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<AnnotationDTO>(HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/annotation/{annotationId}/categories")
	public @ResponseBody ResponseEntity<String[]> getAnnotationInter(Model model, @PathVariable String annotationId) {
		logger.debug("getAnnotationInter id:{}", annotationId);

		Annotation annotation = FenixFramework.getDomainObject(annotationId);

		List<Category> listCategories = annotation.getCategories();

		String[] categories = listCategories.stream()
				.map(c -> c.getNameInEditionContext(annotation.getVirtualEditionInter().getVirtualEdition()))
				.toArray(size -> new String[size]);

		return new ResponseEntity<String[]>(categories, HttpStatus.OK);
	}

}
