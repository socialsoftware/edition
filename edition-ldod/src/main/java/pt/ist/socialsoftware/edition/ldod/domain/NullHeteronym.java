package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.domain.NullHeteronym_Base;

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
	public boolean isNullHeteronym() {
		return true;
	}

	@Override
	public String getName() {
		return "não atribuído";
	}

}
