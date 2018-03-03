package pt.ist.socialsoftware.edition.core.search.json;

import pt.ist.socialsoftware.edition.core.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.core.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.core.domain.FragInter;
import pt.ist.socialsoftware.edition.core.domain.PrintedSource;
import pt.ist.socialsoftware.edition.core.domain.SourceInter;
import pt.ist.socialsoftware.edition.core.domain.Source.SourceType;

public class FragInterJson {

	private final String title;
	private final String url;
	private final String type;

	public FragInterJson(FragInter inter) {
		this.title = inter.getTitle();
		this.type = "";
		this.url = "/fragments/fragment/inter/" + inter.getExternalId();
	}

	public FragInterJson(ExpertEditionInter inter) {
		this.title = inter.getTitle();
		this.type = ((ExpertEdition) inter.getEdition()).getEditor();
		this.url = "/fragments/fragment/inter/" + inter.getExternalId();
	}

	public FragInterJson(SourceInter inter) {
		this.title = inter.getTitle();
		if(inter.getSource().getType().equals(SourceType.PRINTED)) {
			this.type =((PrintedSource) inter.getSource()).getTitle();
		}else if(inter.getSource().getType().equals(SourceType.MANUSCRIPT)){
			this.type = inter.getShortName();
		} else {
			this.type = "";
		}
		this.url = "/fragments/fragment/inter/" + inter.getExternalId();
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public String getType() {
		return type;
	}
}
