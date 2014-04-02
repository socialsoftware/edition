package pt.ist.socialsoftware.edition.domain;

public class CategoryInFragInter extends CategoryInFragInter_Base implements
		Comparable<CategoryInFragInter> {

	public CategoryInFragInter(FragInter fragInter, Category category,
			int percentage) {
		setFragInter(fragInter);
		setCategory(category);
		setPercentage(percentage);
	}

	public void remove() {
		setCategory(null);
		setFragInter(null);

		deleteDomainObject();
	}

	@Override
	public int compareTo(CategoryInFragInter o) {
		if (this.getPercentage() < o.getPercentage())
			return 1;
		else if (this.getPercentage() > o.getPercentage())
			return -1;
		else
			return 0;
	}

}
