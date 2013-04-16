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
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.visitors.HtmlWriterCompareInters;

@Controller
@RequestMapping("/fragments/fragment")
public class FragmentController {

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public String getFragment(Model model, @PathVariable String id) {
		Fragment fragment = AbstractDomainObject.fromExternalId(id);

		if (fragment == null) {
			return "fragmentNotFound";
		} else {
			model.addAttribute("fragment", fragment);

			return "fragment";
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public String getInterpretation(
			@RequestParam(value = "interp", required = true) String interID,
			Model model) {
		FragInter fragInter = AbstractDomainObject.fromExternalId(interID);

		HtmlWriter4OneInter writer = new HtmlWriter4OneInter(fragInter);
		writer.write();

		model.addAttribute("inter", fragInter);
		model.addAttribute("writer", writer);
		return "fragmentInterpretation";
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
			HtmlWriterCompareInters writer = new HtmlWriterCompareInters(list);
			writer.write(list);

			model.addAttribute("inter", fragInter);
			model.addAttribute("inter2Compare", fragInter2Compare);
			model.addAttribute("writer", writer);
			return "fragmentTextualCompare";
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

		List<FragInter> list = new ArrayList<FragInter>();
		list.add(fragInter);
		HtmlWriterCompareInters writer = new HtmlWriterCompareInters(fragInter
				.getFragment().getFragmentInter());
		writer.write(list);

		model.addAttribute("writer", writer);
		return "fragmentTranscription";

	}
}
