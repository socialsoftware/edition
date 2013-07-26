package pt.ist.socialsoftware.edition.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.socialsoftware.edition.domain.Edition;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.Surface;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter4OneInter;

@Controller
@RequestMapping("/fragments/fragment")
public class FragmentController {

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
	public String getFragmentWithInter(Model model, @PathVariable String id) {
		FragInter inter = FenixFramework.getDomainObject(id);

		HtmlWriter4OneInter writer = new HtmlWriter4OneInter(inter);
		writer.write(false);

		if (inter == null) {
			return "util/pageNotFound";
		} else {
			List<FragInter> inters = new ArrayList<FragInter>();
			inters.add(inter);
			model.addAttribute("ldoD", LdoD.getInstance());
			model.addAttribute("user", LdoDUser.getUser());
			model.addAttribute("fragment", inter.getFragment());
			model.addAttribute("inters", inters);
			model.addAttribute("writer", writer);

			return "fragment/main";
		}
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
			writer.write(lineByLine, false);
			model.addAttribute("lineByLine", lineByLine);
			model.addAttribute("writer", writer);
		}

		return "fragment/body";

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
			@RequestParam(value = "surf", required = false) String surfID,
			Model model) {
		FragInter inter = FenixFramework.getDomainObject(interID[0]);
		Surface surface = FenixFramework.getDomainObject(surfID);

		HtmlWriter4OneInter writer = new HtmlWriter4OneInter(inter);
		writer.write(displayDiff, displayDel, highlightIns, highlightSubst,
				showNotes);

		List<FragInter> inters = new ArrayList<FragInter>();
		inters.add(inter);
		model.addAttribute("inters", inters);
		model.addAttribute("writer", writer);

		if (showFacs) {
			if (surface == null) {
				SourceInter sourceInter = (SourceInter) inter;
				surface = sourceInter.getSource().getFacsimile()
						.getFirstSurface();
			}
			model.addAttribute("surface", surface);
			return "fragment/facsimile";
		} else {
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

		model.addAttribute("lineByLine", lineByLine);
		model.addAttribute("inters", inters);
		model.addAttribute("writer", writer);

		if (lineByLine) {
			return "fragment/inter2CompareLineByLine";
		} else {
			return "fragment/inter2CompareSideBySide";
		}

	}

}
