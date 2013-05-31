package pt.ist.socialsoftware.edition.domain;

public class VirtualEditionInter extends VirtualEditionInter_Base {

	public VirtualEditionInter() {
		super();
	}

	@Override
	public String getShortName() {
		return getVirtualEdition().getAcronym();
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

		result = result + "Heterónimo: " + getHeteronym().getName() + "<br>";

		result = result + "Data: " + getDate() + "<br>";

		return result;
	}

	@Override
	public void remove() {
		super.remove();

		removeVirtualEdition();

		deleteDomainObject();
	}

}
