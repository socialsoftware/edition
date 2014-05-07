package pt.ist.socialsoftware.edition.domain;

import java.util.HashSet;
import java.util.Set;

public class UserTagInFragInter extends UserTagInFragInter_Base {

	public UserTagInFragInter init(FragInter fragInter, Category category,
			LdoDUser user, Tag origin) {
		super.init(fragInter, category);

		setType(TagType.FRAGINTER);
		setContributor(user);
		setOriginTag(origin);

		if (origin != null) {
			origin.setDeprecated(true);
		}

		return this;
	}

	@Override
	public void remove() {
		if (getOriginTag() != null) {
			getOriginTag().setDeprecated(true);
			setOriginTag(null);
		}

		setContributor(null);

		super.remove();
	}

	// an overall policy for weights needs to be defined
	@Override
	public int getWeight() {
		return 1;
	}

	@Override
	public Set<LdoDUser> getContributorSet() {
		Set<LdoDUser> users = new HashSet<LdoDUser>();
		if (!getDeprecated()) {
			LdoDUser user = getContributor();
			users.add(user);
		}
		return users;
	}

}
