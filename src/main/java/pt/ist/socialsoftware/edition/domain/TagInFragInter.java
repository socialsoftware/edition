package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.Set;

public class TagInFragInter extends TagInFragInter_Base {

	public TagInFragInter init(FragInter fragInter, Category category,
			int percentage) {
		super.init();

		setType(TagType.FRAGINTER);
		setFragInter(fragInter);
		setCategory(category);
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
