package pt.ist.socialsoftware.edition.domain;

public class SplitTagInFragInter extends SplitTagInFragInter_Base {

	@Override
	public SplitTagInFragInter init(SplitCategory splitCategory, Tag tag) {
		super.init(splitCategory, tag);

		setType(TagType.FRAGINTER);

		return this;
	}
}
