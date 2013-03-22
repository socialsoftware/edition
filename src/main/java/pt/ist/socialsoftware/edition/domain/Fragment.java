package pt.ist.socialsoftware.edition.domain;

import pt.ist.socialsoftware.edition.visitors.HtmlWriter4Interpretation;

public class Fragment extends Fragment_Base {

	public Fragment() {
		super();
	}

	public String getTranscription(FragInter fragInter) {
		HtmlWriter4Interpretation writer = new HtmlWriter4Interpretation(fragInter);
		writer.visit(this.getVariationPoint());
		return writer.getResult();
	}

}
