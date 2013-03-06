package pt.ist.socialsoftware.edition.domain;

public class SourceInter extends SourceInter_Base {

	public SourceInter() {
		super();
	}

	@Override
	public void print() {
		getSource().print();

	}

	@Override
	public String getName() {
		return getSource().getName();
	}

}
