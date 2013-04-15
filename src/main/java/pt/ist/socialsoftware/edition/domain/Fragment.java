package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fragment extends Fragment_Base {

	public Fragment() {
		super();
	}

	public List<FragInter> getSortedInterps() {
		List<FragInter> interps = new ArrayList<FragInter>(getFragmentInter());

		Collections.sort(interps);

		return interps;
	}

}
