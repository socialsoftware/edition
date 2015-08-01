package pt.ist.socialsoftware.edition.search.options;

import java.util.Arrays;
import java.util.Set;

import org.codehaus.jackson.annotate.JsonProperty;

import pt.ist.socialsoftware.edition.domain.Tag;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

public class TaxonomySearchOption extends SearchOption {

	private String[] tags;

	public TaxonomySearchOption(@JsonProperty("tags") String tags) {
		this.tags = tags.split(" ");
	}

	@Override
	public boolean visit(VirtualEditionInter inter) {
		boolean add = true;
		boolean guess;
		int len = tags.length;
		Set<Tag> tagSet = inter.getTagSet();
		for(int i = 0; i<len && add == true; i++ ) {
			guess = false;
			for(Tag tag : tagSet) {
				if(!tag.getDeprecated() && tag.getCategory().getName().equals(tags[i])) {
					guess = true;
					break;
				}
			}
			add = add && guess;
		}

		return add;
	}

	@Override
	public String toString() {
		return "Taxonomy :" + Arrays.toString(tags);
	}

}
