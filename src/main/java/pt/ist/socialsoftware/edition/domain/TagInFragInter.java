package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.Set;

public class TagInFragInter extends TagInFragInter_Base {

	public TagInFragInter(FragInter fragInter, Category category, int percentage) {
		setFragInter(fragInter);
		setCategory(category);
		setPercentage(percentage);
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
