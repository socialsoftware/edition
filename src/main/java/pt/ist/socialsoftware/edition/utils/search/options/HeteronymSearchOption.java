package pt.ist.socialsoftware.edition.utils.search.options;

import org.codehaus.jackson.annotate.JsonProperty;

import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

public final class HeteronymSearchOption extends SearchOption {

	private final String xmlID;

	public HeteronymSearchOption(@JsonProperty("heteronym") String xmlId) {
		this.xmlID = xmlId.equals("null") ? null : xmlId;
	}

	@Override
	public boolean visit(SourceInter inter) {
		return this.isAuthor(inter);
	}

	@Override
	public boolean visit(VirtualEditionInter inter) {
		return false;
	}

	@Override
	public boolean visit(ExpertEditionInter inter) {
		return this.isAuthor(inter);
	}

	private boolean isAuthor(FragInter inter) {
		if(xmlID == null && inter.getHeteronym().getXmlId() == null) {
			//Searching for fragments with no authors and fragment has no author
			return true;
		} else if(((xmlID != null && inter.getHeteronym().getXmlId() == null) || (xmlID == null && inter.getHeteronym().getXmlId() != null))
				&& !ALL.equals(xmlID)) {
			//Searching for fragment with author and fragment has no author or searching for fragment with no author and fragment has author and author can't be all
			return false;
		}else{
			return xmlID.equals(inter.getHeteronym().getXmlId()) || xmlID.equals(ALL);
		}
	}
}
