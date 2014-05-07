package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.Set;

public class GeneratedTagInFragInter extends GeneratedTagInFragInter_Base {

	public GeneratedTagInFragInter init(FragInter fragInter, Category category,
			int percentage) {
		super.init(fragInter, category);

		setType(TagType.FRAGINTER);
		setPercentage(percentage);

		return this;
	}

	@Override
	public int getWeight() {
		return getPercentage();
	}

	@Override
	public Set<LdoDUser> getContributorSet() {
		return new HashSet<LdoDUser>();
	}

}
