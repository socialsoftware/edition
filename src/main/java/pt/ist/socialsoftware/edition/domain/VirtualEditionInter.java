package pt.ist.socialsoftware.edition.domain;

public class VirtualEditionInter extends VirtualEditionInter_Base {

	@Override
	public String getShortName() {
		return getVirtualEdition().getAcronym();
	}

	@Override
	public String getTitle() {
		// TODO: it should be the title of the fragment it uses
		return getFragment().getTitle();
	}

	@Override
	public SourceType getSourceType() {
		return SourceType.VIRTUAL;
	}

	public int compareVirtualEditionInter(VirtualEditionInter other) {
		String myAcronym = getVirtualEdition().getAcronym();
		String otherAcronym = other.getVirtualEdition().getAcronym();

		return myAcronym.compareTo(otherAcronym);
	}

	@Override
	public String getMetaTextual() {
		String result = "";

		result = result + "Edição Virtual: " + getVirtualEdition().getTitle();

		result = result + "Título: " + getTitle();

		result = result + "Heterónimo: " + getHeteronym().getName() + "<br>";

		result = result + "Data: " + getDate() + "<br>";

		return result;
	}

	@Override
	public void remove() {
		super.remove();

		setVirtualEdition(null);

		deleteDomainObject();
	}

	@Override
	public int getNumber() {
		return 0;
	}

}
