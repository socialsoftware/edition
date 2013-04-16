package pt.ist.socialsoftware.edition.utils;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.visitors.HtmlWriterCompareInters;

public class ElFunctions {

	public static Integer getPercentage(HtmlWriter4OneInter writer,
			FragInter inter) {
		return writer.getInterPercentage(inter);
	}

	public static String getTranscription(HtmlWriterCompareInters writer,
			FragInter inter) {
		return writer.getTranscription(inter);
	}
}
