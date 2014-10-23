package pt.ist.socialsoftware.edition.utils.search.json;

import pt.ist.socialsoftware.edition.domain.FragInter;

public class FragInterJson {

	private final String title;
	private final String url;

	public FragInterJson(FragInter fragInter) {
		this.title = fragInter.getTitle();
		this.url =  "/fragments/fragment/inter/" + fragInter.getExternalId();
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}
}
