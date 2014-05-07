package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.Set;

public abstract class MergeTag extends MergeTag_Base {

	public MergeTag init(FragInter inter, Category category, Set<Tag> tags) {
		super.init(inter, category);

		for (Tag tag : tags) {
			tag.setDeprecated(true);
			tag.setMergeTag(this);
		}

		return this;
	}

	@Override
	public void remove() {
		for (Tag tag : getMergedTagsSet()) {
			removeMergedTags(tag);
		}
		super.remove();
	}

	@Override
	public Set<LdoDUser> getContributorSet() {
		Set<LdoDUser> users = new HashSet<LdoDUser>();

		for (Tag tag : getMergedTagsSet()) {
			users.addAll(tag.getContributorSet());
		}

		return users;
	}

	@Override
	public int getWeight() {
		int weight = 0;
		for (Tag tag : getMergedTagsSet()) {
			weight = weight + tag.getWeight();
		}
		return weight;
	}

	@Override
	public void undo() {
		for (Tag tag : getMergedTagsSet()) {
			tag.setDeprecated(false);
		}
		super.undo();
	}

}
