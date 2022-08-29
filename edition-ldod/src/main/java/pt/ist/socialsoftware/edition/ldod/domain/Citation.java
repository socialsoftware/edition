package pt.ist.socialsoftware.edition.ldod.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Citation extends Citation_Base {

	public void init(Fragment fragment, String sourceLink, String date, String fragText) {
		setFragment(fragment);
		setSourceLink(sourceLink);
		setDate(date);
		setFragText(fragText);
	}

	public void remove() {
		setFragment(null);

		getAwareAnnotationSet().forEach(AwareAnnotation::remove);

		getInfoRangeSet().forEach(InfoRange::remove);

		deleteDomainObject();
	}

	public abstract long getId();

	// atualmente está a ser utilizado o método da AwareFactory em vez deste
	// TODO: não deveria ser == inter.getLastUsed() ??
	public InfoRange getInfoRangeByInter(FragInter inter) {
		return getInfoRangeSet().stream().filter(infoRange -> infoRange.getFragInter() == inter).findFirst()
				.orElse(null);
	}

	public abstract int getNumberOfRetweets();

	public LocalDateTime getFormatedDate() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss");
		return LocalDateTime.parse(getDate(), formatter);
	}
}
