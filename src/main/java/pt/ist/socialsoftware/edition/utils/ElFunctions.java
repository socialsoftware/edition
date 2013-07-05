package pt.ist.socialsoftware.edition.utils;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.Fragment;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter2CompInters;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter4OneInter;

public class ElFunctions {

	public static Integer getPercentage(HtmlWriter4OneInter writer,
			FragInter inter) {
		return writer.getInterPercentage(inter);
	}

	public static String getTranscription(HtmlWriter2CompInters writer,
			FragInter inter) {
		return writer.getTranscription(inter);
	}

	public static ExpertEditionInter getExpertEditionInter(Fragment fragment,
			String editor) {
		return fragment.getExpertEditionInter(editor);
	}
}
