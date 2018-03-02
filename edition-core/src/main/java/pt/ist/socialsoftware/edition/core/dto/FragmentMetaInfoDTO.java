package pt.ist.socialsoftware.edition.core.dto;

import java.util.Set;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.core.domain.*;
import pt.ist.socialsoftware.edition.core.domain.FragInter;
import pt.ist.socialsoftware.edition.core.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.core.domain.SourceInter;

public class FragmentMetaInfoDTO {
	private String title;
	private Set<String> heteronyms;
	private Set<String> dates;
	private boolean hasLdoDLabel;

	public FragmentMetaInfoDTO() {
	}

	public FragmentMetaInfoDTO(FragInter lastInter) {
		this.title = lastInter.getFragment().getTitle();

		this.setDates(
				lastInter.getFragment().getFragmentInterSet().stream().filter(inter -> inter.getLdoDDate() != null)
						.map(inter -> inter.getLdoDDate().getDate().toString()).distinct().collect(Collectors.toSet()));

		this.setHeteronyms(lastInter.getFragment().getFragmentInterSet().stream()
				.filter(inter -> inter.getHeteronym() != null && !inter.getHeteronym().isNullHeteronym())
				.map(inter -> inter.getHeteronym().getName()).distinct().collect(Collectors.toSet()));

		setHasLdoDLabel(lastInter.getFragment().getFragmentInterSet().stream()
				.filter(inter -> inter.getSourceType() == Edition.EditionType.AUTHORIAL).map(SourceInter.class::cast)
				.map(inter -> inter.getSource()).filter(source -> source.getType() == Source.SourceType.MANUSCRIPT)
				.map(ManuscriptSource.class::cast).filter(m -> m.getHasLdoDLabel()).findAny().isPresent());

	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<String> getHeteronyms() {
		return this.heteronyms;
	}

	public void setHeteronyms(Set<String> heteronyms) {
		this.heteronyms = heteronyms;
	}

	public Set<String> getDates() {
		return this.dates;
	}

	public void setDates(Set<String> dates) {
		this.dates = dates;
	}

	public boolean isHasLdoDLabel() {
		return this.hasLdoDLabel;
	}

	public void setHasLdoDLabel(boolean hasLdoDLabel) {
		this.hasLdoDLabel = hasLdoDLabel;
	}

}
