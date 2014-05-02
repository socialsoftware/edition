package pt.ist.socialsoftware.edition.domain;

import java.util.Set;

public class MergeTagInFragInter extends MergeTagInFragInter_Base {

	@Override
	public MergeTagInFragInter init(FragInter inter, Category category,
			Set<Tag> tags) {
		super.init(inter, category, tags);
		setType(TagType.FRAGINTER);
		return this;
	}

	@Override
	public int getWeight() {
		int weight = 0;
		int count = 0;
		for (Tag tag : getMergedTagsSet()) {
			weight = weight + tag.getWeight();
			count++;
		}
		return weight / count;
	}

}
