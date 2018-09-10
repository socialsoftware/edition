package pt.ist.socialsoftware.edition.text.domain;

public class NullHeteronym extends NullHeteronym_Base {

	public static NullHeteronym getNullHeteronym() {
		for (Heteronym heteronym : CollectionManager.getInstance().getHeteronymsSet()) {
			if (heteronym instanceof NullHeteronym)
				return (NullHeteronym) heteronym;
		}
		return new NullHeteronym();
	}

	public NullHeteronym() {
		setCollectionManager(CollectionManager.getInstance());
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
