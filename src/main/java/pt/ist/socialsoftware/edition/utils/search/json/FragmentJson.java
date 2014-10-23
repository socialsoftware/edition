package pt.ist.socialsoftware.edition.utils.search.json;

import java.util.List;

import pt.ist.socialsoftware.edition.domain.Fragment;

public class FragmentJson {

	private final String title;
	private final String url;
	private final List<FragInterJson> inters;

	public FragmentJson(Fragment fragment, List<FragInterJson> inters) {
		this.title = fragment.getTitle();
		this.url = "/fragments/fragment/" + fragment.getExternalId();
		this.inters = inters;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public List<FragInterJson> getInters() {
		return inters;
	}
}
