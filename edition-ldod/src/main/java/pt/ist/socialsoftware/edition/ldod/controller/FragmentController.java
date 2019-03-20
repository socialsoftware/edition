package pt.ist.socialsoftware.edition.ldod.controller;

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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.ldod.api.text.TextInterface;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.dto.MainFragmentDto;
import pt.ist.socialsoftware.edition.ldod.generators.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.ldod.generators.HtmlWriter4Variations;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.session.LdoDSession;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.ldod.utils.AnnotationDTO;
import pt.ist.socialsoftware.edition.ldod.utils.AnnotationSearchJson;

@Controller
@SessionAttributes({ "ldoDSession" })
@RequestMapping("/fragments")
public class FragmentController {
	private static Logger logger = LoggerFactory.getLogger(FragmentController.class);

	@ModelAttribute("ldoDSession")
	public LdoDSession getLdoDSession() {
		return LdoDSession.getLdoDSession();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getFragmentsList(Model model) {
		logger.debug("getFragmentsList");
		LdoD ldoD = LdoD.getInstance();
		Text text = Text.getInstance();
		TextInterface textInterface = new TextInterface();
		/*model.addAttribute("jpcEdition", text.getJPCEdition());
		model.addAttribute("tscEdition", text.getTSCEdition());
		model.addAttribute("rzEdition", text.getRZEdition());
		model.addAttribute("jpEdition", text.getJPEdition());*/
		model.addAttribute("jpcEdition", textInterface.getExpertEdition(Edition.COELHO_EDITION_ACRONYM));
		model.addAttribute("tscEdition", textInterface.getExpertEdition(Edition.CUNHA_EDITION_ACRONYM));
		model.addAttribute("rzEdition",textInterface.getExpertEdition(Edition.ZENITH_EDITION_ACRONYM));
		model.addAttribute("jpEdition",textInterface.getExpertEdition(Edition.PIZARRO_EDITION_ACRONYM));
		model.addAttribute("fragments", textInterface.getFragmentsSet());

		return "fragment/list";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}")
	public String getFragment(Model model, @PathVariable String xmlId) {
		TextInterface textInterface = new TextInterface();
		Fragment fragment = textInterface.getFragmentByXmlId(xmlId);

		if (fragment == null) {
			return "redirect:/error";
		} else {
			model.addAttribute("ldoD", LdoD.getInstance());
			model.addAttribute("text", Text.getInstance());
			model.addAttribute("user", LdoDUser.getAuthenticatedUser());
			model.addAttribute("fragment", fragment);
			model.addAttribute("fragmentDto",new MainFragmentDto(fragment));
			model.addAttribute("inters", new ArrayList<FragInter>());
			return "fragment/main";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}")
	@PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
	public String getFragmentWithInterForUrlId(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String xmlId, @PathVariable String urlId) {

		TextInterface textInterface = new TextInterface();
		Fragment fragment = textInterface.getFragmentByXmlId(xmlId);

		if (fragment == null) {
			return "redirect:/error";
		}

		FragInter inter = fragment.getFragInterByUrlId(urlId);

		if (inter == null) {
			return "redirect:/error";
		}

		PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter((ScholarInter) inter.getLastUsed());
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

		List<FragInter> inters = new ArrayList<>();
		inters.add(inter);
		model.addAttribute("ldoD", LdoD.getInstance());
		model.addAttribute("text", Text.getInstance());
		model.addAttribute("user", LdoDUser.getAuthenticatedUser());
		model.addAttribute("fragment", inter.getFragment());
		model.addAttribute("inters", inters);
		model.addAttribute("writer", writer);

		return "fragment/main";
	}

	// added until the controller below is removed
	@ExceptionHandler({ Exception.class })
	public String handleException(Exception ex) {
		return "redirect:/error";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/inter/{externalId}")
	@PreAuthorize("hasPermission(#externalId, 'fragInter.public')")
	public String getFragmentWithInter(Model model, @PathVariable String externalId) {
		logger.debug("getFragmentWithInter externalId:{}", externalId);

		FragInter inter = FenixFramework.getDomainObject(externalId);

		if (inter == null) {
			return "redirect:/error";
		}

		return "redirect:/fragments/fragment/" + inter.getFragment().getXmlId() + "/inter/" + inter.getUrlId();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/inter/{externalId}/taxonomy")
	@PreAuthorize("hasPermission(#externalId, 'fragInter.public')")
	public String getTaxonomy(Model model, @ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String externalId) {

		VirtualEditionInter inter = FenixFramework.getDomainObject(externalId);

		if (inter == null) {
			return "redirect:/error";
		}

		if (inter.getSourceType() != Edition.EditionType.VIRTUAL) {
			return "redirect:/error";
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

		List<FragInter> inters = new ArrayList<>();
		inters.add(inter);
		model.addAttribute("ldoD", LdoD.getInstance());
		model.addAttribute("user", LdoDUser.getAuthenticatedUser());
		model.addAttribute("inters", inters);

		return "fragment/taxonomy";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}/next")
	@PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
	public String getNextFragmentWithInter(Model model, @PathVariable String xmlId, @PathVariable String urlId) {
		TextInterface textInterface = new TextInterface();
		Fragment fragment = textInterface.getFragmentByXmlId(xmlId);
		if (fragment == null) {
			return "redirect:/error";
		}

		FragInter inter = fragment.getFragInterByUrlId(urlId);
		if (inter == null) {
			return "redirect:/error";
		}

		Edition edition = inter.getEdition();
		inter = edition.getNextNumberInter(inter, inter.getNumber());

		return "redirect:/fragments/fragment/" + inter.getFragment().getXmlId() + "/inter/" + inter.getUrlId();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/{xmlId}/inter/{urlId}/prev")
	@PreAuthorize("hasPermission(#xmlId, #urlId, 'fragInter.public')")
	public String getPrevFragmentWithInter(Model model, @PathVariable String xmlId, @PathVariable String urlId) {
		TextInterface textInterface = new TextInterface();
		Fragment fragment = textInterface.getFragmentByXmlId(xmlId);
		if (fragment == null) {
			return "redirect:/error";
		}

		FragInter inter = fragment.getFragInterByUrlId(urlId);
		if (inter == null) {
			return "redirect:/error";
		}

		Edition edition = inter.getEdition();
		inter = edition.getPrevNumberInter(inter, inter.getNumber());

		return "redirect:/fragments/fragment/" + inter.getFragment().getXmlId() + "/inter/" + inter.getUrlId();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/inter")
	public String getInter(Model model, @RequestParam(value = "fragment", required = true) String externalId,
			@RequestParam(value = "inters[]", required = false) String[] intersID) {

		Fragment fragment = FenixFramework.getDomainObject(externalId);

		List<ScholarInter> inters = new ArrayList<>();
		if (intersID != null) {
			for (String interID : intersID) {
				ScholarInter inter = FenixFramework.getDomainObject(interID);
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
			PlainHtmlWriter4OneInter writer4One = new PlainHtmlWriter4OneInter((ScholarInter) inter.getLastUsed());
			writer4One.write(false);
			model.addAttribute("writer", writer4One);
		} else if (inters.size() > 1) {
			HtmlWriter2CompInters writer = new HtmlWriter2CompInters(inters);
			Boolean lineByLine = false;
			if (inters.size() > 2) {
				lineByLine = true;
			}

			Map<ScholarInter, HtmlWriter4Variations> variations = new HashMap<>();
			for (ScholarInter inter : inters) {
				variations.put(inter, new HtmlWriter4Variations(inter));
			}

			List<AppText> apps = new ArrayList<>();
			inters.get(0).getFragment().getTextPortion().putAppTextWithVariations(apps, inters);
			Collections.reverse(apps);

			writer.write(lineByLine, false);
			model.addAttribute("lineByLine", lineByLine);
			model.addAttribute("writer", writer);
			model.addAttribute("variations", variations);
			model.addAttribute("apps", apps);
		}

		return "fragment/main";

	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/inter/editorial")
	public String getInterEditorial(@RequestParam(value = "interp[]", required = true) String[] interID,
			@RequestParam(value = "diff", required = true) boolean displayDiff, Model model) {
		ScholarInter inter = FenixFramework.getDomainObject(interID[0]);

		PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);
		writer.write(displayDiff);

		List<FragInter> inters = new ArrayList<>();
		inters.add(inter);
		model.addAttribute("inters", inters);
		model.addAttribute("writer", writer);

		return "fragment/transcription";

	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/inter/authorial")
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
		if (pbTextID != null && !pbTextID.equals("")) {
			pbText = FenixFramework.getDomainObject(pbTextID);
		}

		PlainHtmlWriter4OneInter writer = new PlainHtmlWriter4OneInter(inter);

		List<FragInter> inters = new ArrayList<>();
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

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/inter/compare")
	public String getInterCompare(@RequestParam(value = "inters[]", required = true) String[] intersID,
			@RequestParam(value = "line") boolean lineByLine,
			@RequestParam(value = "spaces", required = true) boolean showSpaces, Model model) {
		List<ScholarInter> inters = new ArrayList<>();
		for (String interID : intersID) {
			inters.add(FenixFramework.getDomainObject(interID));
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

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/search")
	public @ResponseBody AnnotationSearchJson searchAnnotations(Model model, @RequestParam int limit,
			@RequestParam String uri) {
		// código alterado para funcionar com os dois tipos de anotações
		logger.debug("searchAnnotations uri: " + uri);
		List<AnnotationDTO> annotations = new ArrayList<>();

		VirtualEditionInter inter = FenixFramework.getDomainObject(uri);

		for (Annotation annotation : inter.getAllDepthAnnotations()) {
			AnnotationDTO annotationJson = new AnnotationDTO(annotation);
			annotations.add(annotationJson);
		}

		return new AnnotationSearchJson(annotations);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/fragment/annotations")
	public @ResponseBody ResponseEntity<AnnotationDTO> createAnnotation(Model model,
			@RequestBody final AnnotationDTO annotationJson, HttpServletRequest request) {
		VirtualEditionInter inter = FenixFramework.getDomainObject(annotationJson.getUri());
		VirtualEdition virtualEdition = (VirtualEdition) inter.getEdition();
		LdoDUser user = LdoDUser.getAuthenticatedUser();

		HumanAnnotation annotation;
		if (HumanAnnotation.canCreate(virtualEdition, user)) {
			annotation = inter.createHumanAnnotation(annotationJson.getQuote(), annotationJson.getText(), user,
					annotationJson.getRanges(), annotationJson.getTags());

			annotationJson.setId(annotation.getExternalId());

			return new ResponseEntity<>(new AnnotationDTO(annotation), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/annotations/{id}")
	public @ResponseBody ResponseEntity<AnnotationDTO> getAnnotation(Model model, @PathVariable String id) {

		HumanAnnotation annotation = FenixFramework.getDomainObject(id);
		if (annotation != null) {
			return new ResponseEntity<>(new AnnotationDTO(annotation), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/fragment/annotations/{id}")
	public @ResponseBody ResponseEntity<AnnotationDTO> updateAnnotation(Model model, @PathVariable String id,
			@RequestBody final AnnotationDTO annotationJson) {

		HumanAnnotation annotation = FenixFramework.getDomainObject(id);
		LdoDUser user = LdoDUser.getAuthenticatedUser();

		if (annotation == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (annotation.canUpdate(user)) {
			annotation.update(annotationJson.getText(), annotationJson.getTags());
			return new ResponseEntity<>(new AnnotationDTO(annotation), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/fragment/annotations/{id}")
	public @ResponseBody ResponseEntity<AnnotationDTO> deleteAnnotation(Model model, @PathVariable String id,
			@RequestBody final AnnotationDTO annotationJson) {

		HumanAnnotation annotation = FenixFramework.getDomainObject(id);
		LdoDUser user = LdoDUser.getAuthenticatedUser();

		if (annotation == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (annotation.canDelete(user)) {
			annotation.remove();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/fragment/annotation/{annotationId}/categories")
	public @ResponseBody ResponseEntity<String[]> getAnnotationInter(Model model, @PathVariable String annotationId) {

		HumanAnnotation annotation = FenixFramework.getDomainObject(annotationId);

		List<Category> listCategories = annotation.getCategories();

		String[] categories = listCategories.stream()
				.map(c -> c.getNameInEditionContext(annotation.getVirtualEditionInter().getVirtualEdition()))
				.toArray(size -> new String[size]);

		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

}
