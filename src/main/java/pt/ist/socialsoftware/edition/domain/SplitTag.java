package pt.ist.socialsoftware.edition.domain;

import java.util.Set;

public abstract class SplitTag extends SplitTag_Base {

	public SplitTag init(SplitCategory splitCategory, Tag tag) {
		super.init(tag.getFragInter(), splitCategory);

		tag.setDeprecated(true);
		setOriginSplitTag(tag);

		return this;
	}

	@Override
	public void remove() {
		getOriginSplitTag().setDeprecated(false);
		setOriginSplitTag(null);

		super.remove();
	}

	@Override
	public int getWeight() {
		return getOriginSplitTag().getWeight();
	}

	@Override
	public Set<LdoDUser> getContributorSet() {
		return getOriginSplitTag().getContributorSet();
	}

}
