package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeneratedCategory extends GeneratedCategory_Base {

	@Override
	public GeneratedCategory init(Taxonomy taxonomy) {
		super.init(taxonomy);
		setType(CategoryType.GENERATED);

		return this;
	}

	@Override
	public GeneratedCategory init(Taxonomy taxonomy, String name) {
		super.init(taxonomy, name);
		setType(CategoryType.GENERATED);

		return this;
	}

	@Override
	public List<Tag> getSortedTags() {
		List<Tag> tags = new ArrayList<Tag>(getTagSet());

		Collections.sort(tags);

		return tags;
	}

}
