package pt.ist.socialsoftware.edition.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.socialsoftware.edition.domain.Tag.TagType;

public class MergeCategory extends MergeCategory_Base {

	public MergeCategory init(Taxonomy taxonomy, List<Category> categories) {
		super.init(taxonomy);

		setType(CategoryType.MERGED);

		String name = "";
		for (Category category : categories) {
			name = name + category.getName() + ";";
			category.setDeprecated(true);
			addMergedCategories(category);
		}

		setName(name);

		mergeCommonTags(categories);

		return this;
	}

	private void mergeCommonTags(List<Category> categories) {
		Map<FragInter, Set<Tag>> mapTextPortion = new HashMap<FragInter, Set<Tag>>();
		Map<FragInter, Set<Tag>> mapFragInter = new HashMap<FragInter, Set<Tag>>();
		for (Category category : categories) {
			for (Tag tag : category.getTagSet()) {
				if (tag.getType() == TagType.TEXTPORTION) {
					Set<Tag> tags = mapTextPortion.get(tag.getFragInter());
					if (tags == null) {
						tags = new HashSet<Tag>();
						mapTextPortion.put(tag.getFragInter(), tags);
					}
					tags.add(tag);
				} else if (tag.getType() == TagType.FRAGINTER) {
					Set<Tag> tags = mapFragInter.get(tag.getFragInter());
					if (tags == null) {
						tags = new HashSet<Tag>();
						mapFragInter.put(tag.getFragInter(), tags);
					}
					tags.add(tag);
				}
			}
		}

		for (FragInter inter : mapTextPortion.keySet()) {
			if (mapTextPortion.get(inter).size() > 1) {
				new MergeTagInTextPortion().init(inter, this,
						mapTextPortion.get(inter));
			}
		}

		for (FragInter inter : mapFragInter.keySet()) {
			if (mapFragInter.get(inter).size() > 1) {
				new MergeTagInFragInter().init(inter, this,
						mapFragInter.get(inter));
			}
		}
	}

	@Override
	public void remove() {
		for (Category category : getMergedCategoriesSet()) {
			removeMergedCategories(category);
		}

		super.remove();
	}

	@Override
	public List<Tag> getSortedTags() {
		List<Tag> tags = new ArrayList<Tag>(getTagSet());

		for (Category category : getMergedCategoriesSet()) {
			tags.addAll(category.getSortedActiveTags());
		}

		Collections.sort(tags);

		return tags;
	}

	@Override
	public List<Tag> getSortedActiveTags() {
		List<Tag> tags = new ArrayList<Tag>(getActiveTags());

		for (Category category : getMergedCategoriesSet()) {
			tags.addAll(category.getSortedActiveTags());
		}

		Collections.sort(tags);

		return tags;
	}

	@Atomic(mode = TxMode.WRITE)
	public void undo() {
		for (Tag tag : getActiveTags()) {
			tag.undo();
		}

		for (Category category : getMergedCategoriesSet()) {
			category.setDeprecated(false);
		}

		this.remove();
	}

}
