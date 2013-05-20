package pt.ist.socialsoftware.edition.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pt.ist.fenixframework.pstm.AbstractDomainObject;
import pt.ist.socialsoftware.edition.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.domain.LdoD;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter4OneInter;

@Controller
@RequestMapping("/fragments/fragment")
public class FragmentController {

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public String getFragment(Model model, @PathVariable String id) {
		Fragment fragment = AbstractDomainObject.fromExternalId(id);

		if (fragment == null) {
			return "pageNotFound";
		} else {
			model.addAttribute("fragment", fragment);
			return "fragment";
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/delete")
	public String deleteFragment(Model model,
			@RequestParam("externalId") String externalId) {
		Fragment fragment = AbstractDomainObject.fromExternalId(externalId);

		if (fragment == null) {
			return "fragmentNotFound";
		} else if (LdoD.getInstance().getFragmentsCount() >= 1) {
			fragment.remove();
		}
		model.addAttribute("fragments", LdoD.getInstance().getFragmentsSet());
		return "deleteFragment";

	}

	@RequestMapping(method = RequestMethod.GET, value = "/interpretation/{id}")
	public String getFragmentWithInterpretation(Model model,
			@PathVariable String id) {
		FragInter interpretation = AbstractDomainObject.fromExternalId(id);

		return writeFragmentWithInterpretation(model, interpretation);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/interpretation/next/number/{id}")
	public String getNextFragmentWithInterpretationByNumber(Model model,
			@PathVariable String id) {

		ExpertEditionInter interpretation = AbstractDomainObject
				.fromExternalId(id);

		ExpertEdition edition = interpretation.getExpertEdition();
		interpretation = edition.getNextNumberInter(interpretation,
				interpretation.getNumber());

		return writeFragmentWithInterpretation(model, interpretation);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/interpretation/prev/number/{id}")
	public String getPrevFragmentWithInterpretationByNumber(Model model,
			@PathVariable String id) {
		ExpertEditionInter interpretation = AbstractDomainObject
				.fromExternalId(id);

		ExpertEdition edition = interpretation.getExpertEdition();
		interpretation = edition.getPrevNumberInter(interpretation,
				interpretation.getNumber());

		return writeFragmentWithInterpretation(model, interpretation);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/interpretation/next/heteronym/{id}")
	public String getNextFragmentWithInterpretationByHeteronym(Model model,
			@PathVariable String id) {
		ExpertEditionInter interpretation = AbstractDomainObject
				.fromExternalId(id);

		ExpertEdition edition = interpretation.getExpertEdition();
		interpretation = edition.getNextHeteronymInter(interpretation,
				interpretation.getHeteronym());

		return writeFragmentWithInterpretation(model, interpretation);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/interpretation/prev/heteronym/{id}")
	public String getPrevFragmentWithInterpretationByHeteronym(Model model,
			@PathVariable String id) {
		ExpertEditionInter interpretation = AbstractDomainObject
				.fromExternalId(id);

		ExpertEdition edition = interpretation.getExpertEdition();
		interpretation = edition.getNextHeteronymInter(interpretation,
				interpretation.getHeteronym());

		return writeFragmentWithInterpretation(model, interpretation);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getInterpretation(
			@RequestParam(value = "interp", required = false) String interID,
			Model model) {

		FragInter fragInter = AbstractDomainObject.fromExternalId(interID);

		HtmlWriter4OneInter writer = new HtmlWriter4OneInter(fragInter);
		writer.write();

		model.addAttribute("inter", fragInter);
		model.addAttribute("writer", writer);
		return "fragmentInterpretation";
	}

	private String writeFragmentWithInterpretation(Model model,
			FragInter interpretation) {
		HtmlWriter4OneInter writer = new HtmlWriter4OneInter(interpretation);
		writer.write();

		if (interpretation == null) {
			return "fragmentNotFound";
		} else {
			model.addAttribute("fragment", interpretation.getFragment());
			model.addAttribute("inter", interpretation);
			model.addAttribute("writer", writer);

			return "fragment";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/interpretation")
	public String getInterpretationMenu(
			@RequestParam(value = "interp", required = true) String interID,
			@RequestParam(value = "interp2Compare", required = true) String interID2Compare,
			Model model) {
		FragInter fragInter = AbstractDomainObject.fromExternalId(interID);
		FragInter fragInter2Compare = AbstractDomainObject
				.fromExternalId(interID2Compare);

		if (interID.equals(interID2Compare)) {
			HtmlWriter4OneInter writer = new HtmlWriter4OneInter(fragInter);
			writer.write();

			model.addAttribute("inter", fragInter);
			model.addAttribute("writer", writer);
			return "fragmentTextual";
		} else {
			List<FragInter> list = new ArrayList<FragInter>();
			list.add(fragInter);
			list.add(fragInter2Compare);
			HtmlWriter2CompInters writer = new HtmlWriter2CompInters(list);
			writer.write(list);

			model.addAttribute("inter", fragInter);
			model.addAttribute("inter2Compare", fragInter2Compare);
			model.addAttribute("writer", writer);
			return "fragmentTextualCompare";
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/interpretation/mode")
	public String getInterpretationCompare(
			@RequestParam(value = "interp", required = true) String interID,
			@RequestParam(value = "interp2Compare", required = true) String interID2Compare,
			@RequestParam(value = "line", required = true) boolean lineByLine,
			@RequestParam(value = "spaces", required = true) boolean showSpaces,
			Model model) {
		FragInter fragInter = AbstractDomainObject.fromExternalId(interID);
		FragInter fragInter2Compare = AbstractDomainObject
				.fromExternalId(interID2Compare);

		List<FragInter> list = new ArrayList<FragInter>();
		list.add(fragInter);
		list.add(fragInter2Compare);
		HtmlWriter2CompInters writer = new HtmlWriter2CompInters(list);
		writer.setLineByLine(lineByLine);
		writer.setShowSpaces(showSpaces);
		writer.write(list);

		model.addAttribute("inter", fragInter);
		model.addAttribute("inter2Compare", fragInter2Compare);
		model.addAttribute("writer", writer);

		if (lineByLine) {
			return "fragmentTextualCompareLineByLine";
		} else {
			return "fragmentTextualCompareSideBySide";
		}

	}

	@RequestMapping(method = RequestMethod.GET, value = "/textualauthorial")
	public String getInterpretationTextual(
			@RequestParam(value = "interp", required = true) String interID,
			@RequestParam(value = "del", required = true) boolean displayDel,
			@RequestParam(value = "ins", required = true) boolean highlightIns,
			@RequestParam(value = "subst", required = true) boolean highlightSubst,
			@RequestParam(value = "notes", required = true) boolean showNotes,
			Model model) {
		FragInter fragInter = AbstractDomainObject.fromExternalId(interID);

		HtmlWriter4OneInter writer = new HtmlWriter4OneInter(fragInter);
		writer.write(displayDel, highlightIns, highlightSubst, showNotes);

		model.addAttribute("writer", writer);
		return "fragmentTranscription";

	}

	@RequestMapping(method = RequestMethod.GET, value = "/textualeditorial")
	public String getInterpretationTextual(
			@RequestParam(value = "interp", required = true) String interID,
			@RequestParam(value = "diff", required = true) boolean displayDiff,
			Model model) {
		FragInter fragInter = AbstractDomainObject.fromExternalId(interID);

		HtmlWriter writer = null;
		if (displayDiff) {
			List<FragInter> list = new ArrayList<FragInter>();
			list.add(fragInter);
			writer = new HtmlWriter2CompInters(fragInter.getFragment()
					.getFragmentInter());
			((HtmlWriter2CompInters) writer).write(list);
		} else {
			writer = new HtmlWriter4OneInter(fragInter);
			((HtmlWriter4OneInter) writer).write();
		}

		model.addAttribute("writer", writer);
		return "fragmentTranscription";

	}
}
