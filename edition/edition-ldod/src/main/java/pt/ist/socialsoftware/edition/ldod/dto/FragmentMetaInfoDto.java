package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.*;

public class FragmentMetaInfoDto {
	private String title;
	private String heteronym;
	private String date;
	private boolean hasLdoDLabel;
	private List<String> categories = new ArrayList<>();

	public FragmentMetaInfoDto(FragInter inter) {
		this.title = inter.getFragment().getTitle();

		if (inter.getLdoDDate() != null) {
			this.date = inter.getLdoDDate().getDate().toString();
		}

		if (inter.getHeteronym() != null && !inter.getHeteronym().isNullHeteronym()) {
			this.heteronym = inter.getHeteronym().getName();
		}

		if (inter.getSourceType() == Edition.EditionType.AUTHORIAL) {
			SourceInter sourceInter = (SourceInter) inter;
			if (sourceInter.getSource().getType() == Source.SourceType.MANUSCRIPT) {
				this.hasLdoDLabel = ((ManuscriptSource) sourceInter.getSource()).getHasLdoDLabel();
			}
		}
	}

	public FragmentMetaInfoDto(VirtualEditionInter inter) {
		this(inter.getLastUsed());

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
