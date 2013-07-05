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
import pt.ist.socialsoftware.edition.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
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
			model.addAttribute("fragment", fragment);
			return "fragment/presentation";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/interpretation/{id}")
	public String getFragmentWithInterpretation(Model model,
			@PathVariable String id) {
		FragInter interpretation = FenixFramework.getDomainObject(id);

		return writeFragmentWithInterpretation(model, interpretation);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/interpretation/next/number/{id}")
	public String getNextFragmentWithInterpretationByNumber(Model model,
			@PathVariable String id) {

		ExpertEditionInter interpretation = FenixFramework.getDomainObject(id);

		ExpertEdition edition = interpretation.getExpertEdition();
		interpretation = edition.getNextNumberInter(interpretation,
				interpretation.getNumber());

		return writeFragmentWithInterpretation(model, interpretation);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/interpretation/prev/number/{id}")
	public String getPrevFragmentWithInterpretationByNumber(Model model,
			@PathVariable String id) {
		ExpertEditionInter interpretation = FenixFramework.getDomainObject(id);

		ExpertEdition edition = interpretation.getExpertEdition();
		interpretation = edition.getPrevNumberInter(interpretation,
				interpretation.getNumber());

		return writeFragmentWithInterpretation(model, interpretation);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/interpretation/next/heteronym/{id}")
	public String getNextFragmentWithInterpretationByHeteronym(Model model,
			@PathVariable String id) {
		ExpertEditionInter interpretation = FenixFramework.getDomainObject(id);

		ExpertEdition edition = interpretation.getExpertEdition();
		interpretation = edition.getNextHeteronymInter(interpretation,
				interpretation.getHeteronym());

		return writeFragmentWithInterpretation(model, interpretation);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/interpretation/prev/heteronym/{id}")
	public String getPrevFragmentWithInterpretationByHeteronym(Model model,
			@PathVariable String id) {
		ExpertEditionInter interpretation = FenixFramework.getDomainObject(id);

		ExpertEdition edition = interpretation.getExpertEdition();
		interpretation = edition.getNextHeteronymInter(interpretation,
				interpretation.getHeteronym());

		return writeFragmentWithInterpretation(model, interpretation);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getInterpretation(
			@RequestParam(value = "interp", required = false) String interID,
			Model model) {

		FragInter fragInter = FenixFramework.getDomainObject(interID);

		HtmlWriter4OneInter writer = new HtmlWriter4OneInter(fragInter);
		writer.write(false);

		model.addAttribute("inter", fragInter);
		model.addAttribute("writer", writer);
		return "fragment/interpretation";
	}

	private String writeFragmentWithInterpretation(Model model,
			FragInter interpretation) {
		HtmlWriter4OneInter writer = new HtmlWriter4OneInter(interpretation);
		writer.write(false);

		if (interpretation == null) {
			return "util/pageNotFound";
		} else {
			model.addAttribute("fragment", interpretation.getFragment());
			model.addAttribute("inter", interpretation);
			model.addAttribute("writer", writer);

			return "fragment/presentation";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/interpretation")
	public String getInterpretationMenu(
			@RequestParam(value = "baseID", required = true) String baseID,
			@RequestParam(value = "interpsID[]", required = true) String[] interpsID,
			Model model) {
		FragInter fragInter = FenixFramework.getDomainObject(baseID);
		List<FragInter> interps = new ArrayList<FragInter>();
		for (String inter : interpsID) {
			interps.add((FragInter) FenixFramework.getDomainObject(inter));
		}

		if (interps.size() == 1) {
			HtmlWriter4OneInter writer4One = new HtmlWriter4OneInter(fragInter);
			writer4One.write(false);

			model.addAttribute("inter", fragInter);
			model.addAttribute("writer", writer4One);
			return "fragment/textual";
		} else {
			HtmlWriter2CompInters writer2Compare = new HtmlWriter2CompInters(
					interps);

			Boolean lineByLine = false;
			if (interps.size() > 2) {
				lineByLine = true;
			}
			writer2Compare.write(lineByLine, false);

			model.addAttribute("lineByLine", lineByLine);
			model.addAttribute("inter", fragInter);
			model.addAttribute("inter2Compare", interps);
			model.addAttribute("writer", writer2Compare);

			return "fragment/textualCompare";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/interpretation/mode")
	public String getInterpretationCompare(
			@RequestParam(value = "interp", required = true) String interID,
			@RequestParam(value = "interp2Compare[]", required = true) String[] interID2Compare,
			@RequestParam(value = "line") boolean lineByLine,
			@RequestParam(value = "spaces", required = true) boolean showSpaces,
			Model model) {
		FragInter fragInter = FenixFramework.getDomainObject(interID);
		List<FragInter> fragInter2Compare = new ArrayList<FragInter>();
		for (String interID2 : interID2Compare) {
			fragInter2Compare.add((FragInter) FenixFramework
					.getDomainObject(interID2));
		}

		HtmlWriter2CompInters writer = new HtmlWriter2CompInters(
				fragInter2Compare);

		if (fragInter2Compare.size() > 2) {
			lineByLine = true;
			model.addAttribute("lineByLine", lineByLine);
		}
		writer.write(lineByLine, false);

		model.addAttribute("inter", fragInter);
		model.addAttribute("inter2Compare", fragInter2Compare);
		model.addAttribute("writer", writer);

		writer.write(lineByLine, showSpaces);

		model.addAttribute("inter", fragInter);
		model.addAttribute("inter2Compare", fragInter2Compare);
		model.addAttribute("writer", writer);

		if (lineByLine) {
			return "fragment/textualCompareLineByLine";
		} else {
			return "fragment/textualCompareSideBySide";
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/textualauthorial")
	public String getInterpretationTextual(
			@RequestParam(value = "interp[]", required = true) String[] interID,
			@RequestParam(value = "diff", required = true) boolean displayDiff,
			@RequestParam(value = "del", required = true) boolean displayDel,
			@RequestParam(value = "ins", required = true) boolean highlightIns,
			@RequestParam(value = "subst", required = true) boolean highlightSubst,
			@RequestParam(value = "notes", required = true) boolean showNotes,
			Model model) {
		FragInter fragInter = FenixFramework.getDomainObject(interID[0]);

		HtmlWriter4OneInter writer = new HtmlWriter4OneInter(fragInter);
		writer.write(displayDiff, displayDel, highlightIns, highlightSubst,
				showNotes);

		model.addAttribute("writer", writer);
		return "fragment/transcription";

	}

	@RequestMapping(method = RequestMethod.GET, value = "/textualeditorial")
	public String getInterpretationTextual(
			@RequestParam(value = "interp[]", required = true) String[] interID,
			@RequestParam(value = "diff", required = true) boolean displayDiff,
			Model model) {
		FragInter fragInter = FenixFramework.getDomainObject(interID[0]);

		HtmlWriter4OneInter writer = new HtmlWriter4OneInter(fragInter);
		writer.write(displayDiff);

		model.addAttribute("inter", fragInter);
		model.addAttribute("writer", writer);
		return "fragment/transcription";

	}
}
