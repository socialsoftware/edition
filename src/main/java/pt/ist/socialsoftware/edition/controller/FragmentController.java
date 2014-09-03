package pt.ist.socialsoftware.edition.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import pt.ist.socialsoftware.edition.security.LdoDSession;
import pt.ist.socialsoftware.edition.shared.exception.LdoDException;
import pt.ist.socialsoftware.edition.utils.AnnotationJson;
import pt.ist.socialsoftware.edition.utils.AnnotationSearchJson;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter4Variations;

@Controller
@SessionAttributes({ "ldoDSession" })
@RequestMapping("/fragments/fragment")
public class FragmentController {

	@ModelAttribute("ldoDSession")
	public LdoDSession getLdoDSession() {
		LdoDSession ldoDSession = new LdoDSession();

		System.out.println("VirtualEditionController:getLdoDSession()");

		LdoDUser user = LdoDUser.getUser();
		if (user != null) {
			for (VirtualEdition virtualEdition : user
					.getSelectedVirtualEditionsSet()) {
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
			model.addAttribute("user", LdoDUser.getUser());
			model.addAttribute("fragment", fragment);
			model.addAttribute("inters", new ArrayList<FragInter>());
			return "fragment/main";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/{id}")
	public String getFragmentWithInter(Model model,
			@ModelAttribute("ldoDSession") LdoDSession ldoDSession,
			@PathVariable String id) {
		FragInter inter = FenixFramework.getDomainObject(id);

		HtmlWriter4OneInter writer = new HtmlWriter4OneInter(inter);
		writer.write(false);

		if (inter == null) {
			return "util/pageNotFound";
		}

		// if it is a virtual interpretation check access and set session
		if (inter.getSourceType() == Edition.EditionType.VIRTUAL) {
			VirtualEdition virtualEdition = (VirtualEdition) inter.getEdition();

			LdoDUser user = LdoDUser.getUser();
			if (virtualEdition.checkAccess(user)) {
				if (!ldoDSession.hasSelectedVE(virtualEdition.getExternalId())) {
					ldoDSession.toggleSelectedVirtualEdition(user,
							virtualEdition);
				}
			} else {
				// TODO: a userfriendly reimplementation
				throw new LdoDException("Não tem acesso a esta edição virtual");
			}
		}

		System.out.println("INTER=" + id);

		List<FragInter> inters = new ArrayList<FragInter>();
		inters.add(inter);
		model.addAttribute("ldoD", LdoD.getInstance());
		model.addAttribute("user", LdoDUser.getUser());
		model.addAttribute("fragment", inter.getFragment());
		model.addAttribute("inters", inters);
		model.addAttribute("writer", writer);

		return "fragment/main";
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/next/number/{id}")
	public String getNextFragmentWithInter(Model model, @PathVariable String id) {

		FragInter inter = FenixFramework.getDomainObject(id);

		Edition edition = inter.getEdition();
		inter = edition.getNextNumberInter(inter, inter.getNumber());

		return "redirect:/fragments/fragment/inter/" + inter.getExternalId();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/prev/number/{id}")
	public String getPrevFragmentWithInter(Model model, @PathVariable String id) {
		FragInter inter = FenixFramework.getDomainObject(id);

		Edition edition = inter.getEdition();
		inter = edition.getPrevNumberInter(inter, inter.getNumber());

		return "redirect:/fragments/fragment/inter/" + inter.getExternalId();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter")
	public String getInter(
			Model model,
			@RequestParam(value = "fragment", required = true) String fragID,
			@RequestParam(value = "inters[]", required = false) String[] intersID) {

		Fragment fragment = FenixFramework.getDomainObject(fragID);

		List<FragInter> inters = new ArrayList<FragInter>();
		if (intersID != null) {
			for (String interID : intersID) {
				FragInter inter = (FragInter) FenixFramework
						.getDomainObject(interID);
				if (inter != null) {
					inters.add(inter);
				}
			}
		}

		model.addAttribute("ldoD", LdoD.getInstance());
		model.addAttribute("user", LdoDUser.getUser());
		model.addAttribute("fragment", fragment);
		model.addAttribute("inters", inters);

		if (inters.size() == 1) {
			FragInter inter = inters.get(0);
			HtmlWriter4OneInter writer4One = new HtmlWriter4OneInter(inter);
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
			for (TextPortion text : inters.get(0).getFragment()
					.getTextPortion().getChildTextSet()) {
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
	public String getInterEditorial(
			@RequestParam(value = "interp[]", required = true) String[] interID,
			@RequestParam(value = "diff", required = true) boolean displayDiff,
			Model model) {
		FragInter inter = FenixFramework.getDomainObject(interID[0]);

		HtmlWriter4OneInter writer = new HtmlWriter4OneInter(inter);
		writer.write(displayDiff);

		List<FragInter> inters = new ArrayList<FragInter>();
		inters.add(inter);
		model.addAttribute("inters", inters);
		model.addAttribute("writer", writer);

		return "fragment/transcription";

	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/authorial")
	public String getInterAuthorial(
			@RequestParam(value = "interp[]", required = true) String[] interID,
			@RequestParam(value = "diff", required = true) boolean displayDiff,
			@RequestParam(value = "del", required = true) boolean displayDel,
			@RequestParam(value = "ins", required = true) boolean highlightIns,
			@RequestParam(value = "subst", required = true) boolean highlightSubst,
			@RequestParam(value = "notes", required = true) boolean showNotes,
			@RequestParam(value = "facs", required = true) boolean showFacs,
			@RequestParam(value = "pb", required = false) String pbTextID,
			Model model) {
		SourceInter inter = FenixFramework.getDomainObject(interID[0]);
		PbText pbText = null;
		if (pbTextID != "") {
			pbText = FenixFramework.getDomainObject(pbTextID);
		}

		HtmlWriter4OneInter writer = new HtmlWriter4OneInter(inter);

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

			writer.write(displayDiff, displayDel, highlightIns, highlightSubst,
					showNotes, pbText);
			model.addAttribute("surface", surface);
			model.addAttribute("prevsurface", inter.getPrevSurface(pbText));
			model.addAttribute("nextsurface", inter.getNextSurface(pbText));
			model.addAttribute("prevpb", inter.getPrevPbText(pbText));
			model.addAttribute("nextpb", inter.getNextPbText(pbText));
			model.addAttribute("writer", writer);
			return "fragment/facsimile";
		} else {
			writer.write(displayDiff, displayDel, highlightIns, highlightSubst,
					showNotes, null);
			model.addAttribute("writer", writer);
			return "fragment/transcription";
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/inter/compare")
	public String getInterCompare(
			@RequestParam(value = "inters[]", required = true) String[] intersID,
			@RequestParam(value = "line") boolean lineByLine,
			@RequestParam(value = "spaces", required = true) boolean showSpaces,
			Model model) {
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
	public @ResponseBody AnnotationSearchJson searchAnnotations(Model model,
			@RequestParam int limit, @RequestParam String uri) {
		System.out.println("searchAnnotations:" + limit + uri);

		List<AnnotationJson> annotations = new ArrayList<AnnotationJson>();

		FragInter inter = FenixFramework.getDomainObject(uri);

		for (Annotation annotation : inter.getAnnotationSet()) {
			AnnotationJson annotationJson = new AnnotationJson(annotation);
			annotations.add(annotationJson);
		}

		return new AnnotationSearchJson(annotations);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/annotations")
	public @ResponseBody ResponseEntity<AnnotationJson> createAnnotation(
			Model model, @RequestBody final AnnotationJson annotationJson,
			HttpServletRequest request) {
		FragInter inter = FenixFramework.getDomainObject(annotationJson
				.getUri());
		VirtualEdition virtualEdition = (VirtualEdition) inter.getEdition();
		LdoDUser user = LdoDUser.getUser();

		Annotation annotation;
		if (virtualEdition.getParticipantSet().contains(user)) {

			annotation = inter.createAnnotation(annotationJson.getQuote(),
					annotationJson.getText(), user, annotationJson.getRanges(),
					annotationJson.getTags());

			annotationJson.setId(annotation.getExternalId());

			return new ResponseEntity<AnnotationJson>(new AnnotationJson(
					annotation), HttpStatus.CREATED);
		} else {
			return new ResponseEntity<AnnotationJson>(HttpStatus.UNAUTHORIZED);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/annotations/{id}")
	public @ResponseBody ResponseEntity<AnnotationJson> getAnnotation(
			Model model, @PathVariable String id) {
		Annotation annotation = FenixFramework.getDomainObject(id);
		if (annotation != null) {
			return new ResponseEntity<AnnotationJson>(new AnnotationJson(
					annotation), HttpStatus.OK);
		} else {
			return new ResponseEntity<AnnotationJson>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/annotations/{id}")
	public @ResponseBody ResponseEntity<AnnotationJson> updateAnnotation(
			Model model, @PathVariable String id,
			@RequestBody final AnnotationJson annotationJson) {
		Annotation annotation = FenixFramework.getDomainObject(id);
		if (annotation != null) {
			annotation.update(annotationJson.getText(),
					annotationJson.getTags());
			return new ResponseEntity<AnnotationJson>(new AnnotationJson(
					annotation), HttpStatus.OK);
		} else {
			return new ResponseEntity<AnnotationJson>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/annotations/{id}")
	public @ResponseBody ResponseEntity<AnnotationJson> deleteAnnotation(
			Model model, @PathVariable String id,
			@RequestBody final AnnotationJson annotationJson) {
		Annotation annotation = FenixFramework.getDomainObject(id);
		if (annotation != null) {
			annotation.remove();
			return new ResponseEntity<AnnotationJson>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<AnnotationJson>(HttpStatus.NOT_FOUND);
		}
	}
}
