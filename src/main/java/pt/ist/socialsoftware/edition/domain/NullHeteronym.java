package pt.ist.socialsoftware.edition.domain;

public class NullHeteronym extends NullHeteronym_Base {

	public static NullHeteronym getNullHeteronym() {
		for (Heteronym heteronym : LdoD.getInstance().getHeteronymsSet()) {
			if (heteronym instanceof NullHeteronym)
				return (NullHeteronym) heteronym;
		}
		return new NullHeteronym();
	}

	public NullHeteronym() {
		setLdoD(LdoD.getInstance());
	}

	@Override
	public String getName() {
		return "não atribuído";
	}

}
