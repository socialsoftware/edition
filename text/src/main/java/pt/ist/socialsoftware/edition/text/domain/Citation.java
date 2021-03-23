package pt.ist.socialsoftware.edition.text.domain;

import pt.ist.socialsoftware.edition.text.api.dto.FragmentDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Citation extends Citation_Base {

	public void init(Fragment fragment, String sourceLink, String date, String fragText) {
		setFragment(fragment);
		setSourceLink(sourceLink);
		setDate(date);
		setFragText(fragText);
	}

	public void init(FragmentDto fragment, String sourceLink, String date, String fragText) {
		setFragment(TextModule.getInstance().getFragmentByXmlId(fragment.getXmlId()));
		setSourceLink(sourceLink);
		setDate(date);
		setFragText(fragText);
	}

	public void remove() {
		setFragment(null);

		//getAwareAnnotationSet().stream().forEach(aa -> aa.remove());

		getInfoRangeSet().stream().forEach(infoRange -> infoRange.remove());

		deleteDomainObject();
	}

	public abstract long getId();

	// atualmente está a ser utilizado o método da AwareFactory em vez deste
	// TODO: não deveria ser == inter.getLastUsed() ??
	public InfoRange getInfoRangeByInter(ScholarInter inter) {
		return getInfoRangeSet().stream().filter(infoRange -> infoRange.getScholarInter() == inter).findFirst()
				.orElse(null);
	}

	public abstract int getNumberOfRetweets();

	public LocalDateTime getFormatedDate() {
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
		return LocalDateTime.parse(getDate(), formater);
	}

	public boolean isTwitterCitation() { return false; }
}
