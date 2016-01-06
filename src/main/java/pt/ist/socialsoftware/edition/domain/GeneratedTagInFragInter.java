package pt.ist.socialsoftware.edition.domain;

public class GeneratedTagInFragInter extends GeneratedTagInFragInter_Base {

	public GeneratedTagInFragInter init(VirtualEditionInter inter, Category category, LdoDUser user, int percentage) {
		setInter(inter);
		setCategory(category);
		setAnnotation(null);
		setContributor(user);
		setPercentage(percentage);

		return this;
	}

	@Override
	public int getWeight() {
		return getPercentage();
	}

}
