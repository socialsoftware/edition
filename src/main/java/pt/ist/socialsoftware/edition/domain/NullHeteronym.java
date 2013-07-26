package pt.ist.socialsoftware.edition.domain;

public class NullHeteronym extends NullHeteronym_Base {

	public NullHeteronym() {
		setLdoD(LdoD.getInstance());
	}

	@Override
	public String getName() {
		return "Não atribuído";
	}

}
