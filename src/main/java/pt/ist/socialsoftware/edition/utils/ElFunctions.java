package pt.ist.socialsoftware.edition.utils;

import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.visitors.HtmlWriter4OneInter;

public class ElFunctions {

	public static Integer getPercentage(HtmlWriter4OneInter writer,
			FragInter inter) {
		return writer.getInterPercentage(inter);
	}

}
