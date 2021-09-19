package pt.ist.socialsoftware.edition.text.domain;

import pt.ist.socialsoftware.edition.text.api.dto.FragmentDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Citation extends Citation_Base {

	public void init(Fragment fragment, String sourceLink, String date, String fragText) {
		setFragment(fragment);
		setSourceLink(sourceLink);
		setDate(date);
		setFragText(fragText);
	}

	public void init(String fragmentXmlId, String sourceLink, String date, String fragText, long id) {
		setFragment(TextModule.getInstance().getFragmentByXmlId(fragmentXmlId));
		setSourceLink(sourceLink);
		setDate(date);
		setFragText(fragText);
		setId(id);
	}

	public void remove() {
		setFragment(null);

		//getAwareAnnotationSet().stream().forEach(aa -> aa.remove());

		getInfoRangeSet().stream().forEach(infoRange -> infoRange.remove());

		deleteDomainObject();
	}


	// atualmente está a ser utilizado o método da AwareFactory em vez deste
	// TODO: não deveria ser == inter.getLastUsed() ??
	public InfoRange getInfoRangeByInter(ScholarInter inter) {
		return getInfoRangeSet().stream().filter(infoRange -> infoRange.getScholarInter() == inter).findFirst()
				.orElse(null);
	}

	public int getNumberOfRetweets() {
		return 0;
	}

	public LocalDateTime getFormatedDate() {
		DateTimeFormatter formater = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
		return LocalDateTime.parse(getDate(), formater);
	}

	public boolean isTwitterCitation() { return false; }
}
