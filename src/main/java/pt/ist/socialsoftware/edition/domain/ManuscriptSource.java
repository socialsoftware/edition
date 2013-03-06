package pt.ist.socialsoftware.edition.domain;

public class ManuscriptSource extends ManuscriptSource_Base {

	public ManuscriptSource() {
		super();
	}

	@Override
	public void print() {
		System.out.print(getIdno() + ":");
	}

	@Override
	public String getName() {
		return getIdno();
	}

}
