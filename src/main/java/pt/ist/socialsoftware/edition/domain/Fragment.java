package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.ist.socialsoftware.edition.visitors.HtmlWriter4Interpretation;

public class Fragment extends Fragment_Base {

	public Fragment() {
		super();
	}

	public String getTranscription(FragInter fragInter) {
		HtmlWriter4Interpretation writer = new HtmlWriter4Interpretation(
				fragInter);
		writer.visit(this.getVariationPoint());
		return writer.getResult();
	}

	public List<FragInter> getSortedInterps() {
		List<FragInter> interps = new ArrayList<FragInter>(getFragmentInter());

		Collections.sort(interps);

		return interps;
	}

}
