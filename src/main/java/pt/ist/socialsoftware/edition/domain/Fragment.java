package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.domain.Edition.EditionType;

public class Fragment extends Fragment_Base {

	public enum PrecisionType {
		HIGH("high"), MEDIUM("medium"), LOW("low"), UNKNOWN("unknown");

		private final String desc;

		PrecisionType(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public Fragment(LdoD ldoD, String title, String xmlId) {
		setLdoD(ldoD);
		setTitle(title);
		setXmlId(xmlId);
	}

	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setLdoD(null);

		getTextPortion().remove();

		for (FragInter inter : getFragmentInterSet()) {
			inter.remove();
		}

		for (Source source : getSourcesSet()) {
			source.remove();
		}

		deleteDomainObject();
	}

	public List<FragInter> getSortedInterps() {
		List<FragInter> interps = new ArrayList<FragInter>(
				getFragmentInterSet());

		Collections.sort(interps);

		return interps;
	}

	public List<SourceInter> getSortedSourceInter() {
		List<SourceInter> interps = new ArrayList<SourceInter>();

		for (FragInter inter : getFragmentInterSet()) {
			if ((inter.getSourceType() == EditionType.AUTHORIAL)) {
				interps.add((SourceInter) inter);
			}
		}

		Collections.sort(interps);

		return interps;
	}

	public ExpertEditionInter getExpertEditionInter(String editor) {
		for (FragInter inter : getFragmentInterSet()) {
			if (inter.getSourceType() == EditionType.EDITORIAL) {
				ExpertEditionInter edInter = (ExpertEditionInter) inter;
				if (edInter.getExpertEdition().getEditor().equals(editor)) {
					return edInter;
				}
			}
		}
		return null;
	}

	public int getNumberOfInter4Edition(Edition edition) {
		int number = 0;
		for (FragInter inter : getFragmentInterSet()) {
			if (inter.belongs2Edition(edition)) {
				number = number + 1;
			}
		}
		return number;
	}

	public Surface getSurface(String xmlId) {
		for (Source source : getSourcesSet()) {
			if (source.getFacsimile() != null) {
				for (Surface surface : source.getFacsimile().getSurfaces()) {
					if (xmlId.equals(surface.getXmlId())) {
						return surface;
					}
				}
			}
		}
		return null;
	}

	public FragInter getFragInter(String xmlId) {
		for (FragInter inter : getFragmentInterSet()) {
			if (xmlId.equals(inter.getXmlId())) {
				return inter;
			}
		}
		return null;
	}

}
