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

}
