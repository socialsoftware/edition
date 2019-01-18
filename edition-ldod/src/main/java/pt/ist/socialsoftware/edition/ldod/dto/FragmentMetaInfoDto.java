package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public class FragmentMetaInfoDto {
	private String title;
	private String heteronym;
	private String date;
	private boolean hasLdoDLabel;
	private List<String> categories;

	public FragmentMetaInfoDto() {
	}

	public FragmentMetaInfoDto(VirtualEditionInter inter) {
		this.title = inter.getFragment().getTitle();

		FragInter lastUsed = inter.getLastUsed();

		if (lastUsed.getLdoDDate() != null) {
			this.date = lastUsed.getLdoDDate().getDate().toString();
		}

		if (lastUsed.getHeteronym() != null && !lastUsed.getHeteronym().isNullHeteronym()) {
			this.heteronym = lastUsed.getHeteronym().getName();
		}

		if (lastUsed.getSourceType() == Edition.EditionType.AUTHORIAL) {
			SourceInter sourceInter = (SourceInter) lastUsed;
			if (sourceInter.getSource().getType() == Source.SourceType.MANUSCRIPT) {
				this.hasLdoDLabel = ((ManuscriptSource) sourceInter.getSource()).getHasLdoDLabel();
			}
		}

		this.categories = inter.getCategories().stream().map(c -> c.getName()).sorted().collect(Collectors.toList());

	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHeteronym() {
		return this.heteronym;
	}

	public void setHeteronym(String heteronym) {
		this.heteronym = heteronym;
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isHasLdoDLabel() {
		return this.hasLdoDLabel;
	}

	public void setHasLdoDLabel(boolean hasLdoDLabel) {
		this.hasLdoDLabel = hasLdoDLabel;
	}

	public List<String> getCategories() {
		return this.categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

}
