package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class AwareAnnotation extends AwareAnnotation_Base {

	public AwareAnnotation(VirtualEditionInter inter, String quote, String text, Citation citation) {
		super.init(inter, quote, text);
		this.setCitation(citation);
		this.setUser(LdoD.getInstance().getUser("Twitter"));
	}

	@Override
	@Atomic(mode = TxMode.WRITE)
	public void remove() {
		setCitation(null);

		super.remove();
	}

	@Override
	public boolean isAwareAnnotation() {
		return true;
	}

	@Override
	public boolean isHumanAnnotation() {
		return false;
	}

	public String getSourceLink() {
		return this.getCitation().getSourceLink();
	}

	public String getDate() {
		return this.getCitation().getDate().split(" ")[0];
	}

	public String getCountry() {
		return ((TwitterCitation) this.getCitation()).getCountry();
	}

	public String getProfileURL() {
		return ((TwitterCitation) this.getCitation()).getUserProfileURL();
	}
}
