package pt.ist.socialsoftware.edition.domain;

public class SplitTagInTextPortion extends SplitTagInTextPortion_Base {

	// TODO
	@Override
	public SplitTagInTextPortion init(SplitCategory splitCategory, Tag tag) {
		super.init(splitCategory, tag);

		setType(TagType.TEXTPORTION);

		return this;
	}

}
