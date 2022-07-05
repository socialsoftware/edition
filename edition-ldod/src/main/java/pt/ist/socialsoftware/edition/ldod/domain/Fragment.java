package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class Fragment extends Fragment_Base implements Comparable<Fragment> {
	public enum PrecisionType {
		HIGH("high"), MEDIUM("medium"), LOW("low"), UNKNOWN("unknown");

		private final String desc;

		PrecisionType(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return this.desc;
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

		// remove virtual edition interpretations first
		for (VirtualEditionInter inter : getVirtualEditionInters()) {
			inter.remove();
		}

		for (FragInter inter : getFragmentInterSet()) {
			inter.remove();
		}

		for (Source source : getSourcesSet()) {
			source.remove();
		}

		for (RefText ref : getRefTextSet()) {
			// the reference is removed
			ref.remove();
		}

		getCitationSet().stream().forEach(c -> c.remove());

		deleteDomainObject();
	}

	public List<FragInter> getSortedInterps() {
		List<FragInter> interps = new ArrayList<>(getFragmentInterSet());

		Collections.sort(interps);

		return interps;
	}

	public List<SourceInter> getSortedSourceInter() {
		List<SourceInter> interps = new ArrayList<>();

		for (FragInter inter : getFragmentInterSet()) {
			if (inter.getSourceType() == Edition.EditionType.AUTHORIAL) {
				interps.add((SourceInter) inter);
			}
		}

		Collections.sort(interps);

		return interps;
	}

	public Set<ExpertEditionInter> getExpertEditionInterSet() {
		Set<ExpertEditionInter> result = new HashSet<>();
		for (FragInter inter : getFragmentInterSet()) {
			if (inter.getSourceType() == Edition.EditionType.EDITORIAL) {
				result.add((ExpertEditionInter) inter);
			}
		}
		return result;
	}

	public Set<ExpertEditionInter> getExpertEditionInters(ExpertEdition expertEdition) {
		// return getFragmentInterSet().stream().filter(inter ->
		// inter.getEdition() == expertEdition)
		// .map(ExpertEditionInter.class::cast).collect(Collectors.toSet());

		Set<ExpertEditionInter> result = new HashSet<>();
		for (FragInter inter : getFragmentInterSet()) {
			if (inter.getEdition() == expertEdition) {
				result.add((ExpertEditionInter) inter);
			}
		}
		return result;
	}

	public Set<VirtualEditionInter> getVirtualEditionInters() {
		Set<VirtualEditionInter> result = new HashSet<>();
		for (FragInter inter : getFragmentInterSet()) {
			if (inter instanceof VirtualEditionInter) {
				result.add((VirtualEditionInter) inter);
			}
		}
		return result;
	}

	public List<VirtualEditionInter> getVirtualEditionInters(VirtualEdition virtualEdition) {
		return getFragmentInterSet().stream().filter(inter -> inter.getEdition() == virtualEdition)
				.map(VirtualEditionInter.class::cast).sorted().collect(Collectors.toList());
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

	public FragInter getFragInterByXmlId(String xmlId) {
		for (FragInter inter : getFragmentInterSet()) {
			if (xmlId.equals(inter.getXmlId())) {
				return inter;
			}
		}
		return null;
	}

	public FragInter getFragInterByUrlId(String urlId) {
		for (FragInter inter : getFragmentInterSet()) {
			if (urlId.equals(inter.getUrlId())) {
				return inter;
			}
		}
		return null;
	}

	public SourceInter getRepresentativeSourceInter() {
		// get the last one, since it is ordered, it will be printed, or
		// dactiloscript, or manuscript
		List<SourceInter> sourceInters = getSortedSourceInter();
		return sourceInters.get(sourceInters.size() - 1);
	}

	@Override
	public int compareTo(Fragment fragment) {
		return this.getXmlId().compareTo(fragment.getXmlId());
	}

	public Citation getCitationById(long id) {
		return getCitationSet().stream().filter(citation -> citation.getId() == id).findFirst().orElse(null);
	}
}
