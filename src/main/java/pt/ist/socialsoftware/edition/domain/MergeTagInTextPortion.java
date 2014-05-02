package pt.ist.socialsoftware.edition.domain;

import java.util.Set;

public class MergeTagInTextPortion extends MergeTagInTextPortion_Base {

	@Override
	public MergeTagInTextPortion init(FragInter inter, Category category,
			Set<Tag> tags) {
		super.init(inter, category, tags);
		setType(TagType.TEXTPORTION);
		return this;
	}

	@Override
	public int getWeight() {
		int weight = 0;
		for (Tag tag : getMergedTagsSet()) {
			weight = weight + tag.getWeight();
		}
		return weight;
	}

}
