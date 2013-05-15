package pt.ist.socialsoftware.edition.domain;

public class VirtualEditionInter extends VirtualEditionInter_Base {

	public VirtualEditionInter() {
		super();
	}

	@Override
	public String getShortName() {
		return getVirtualEdition().getName();
	}

	@Override
	public SourceType getSourceType() {
		return SourceType.VIRTUAL;
	}

	public int compareVirtualEditionInter(VirtualEditionInter other) {
		String myName = getVirtualEdition().getName();
		String otherName = other.getVirtualEdition().getName();

		return myName.compareTo(otherName);
	}

	@Override
	public String getMetaTextual() {
		String result = "";

		result = result + "Edição Virtual: " + getVirtualEdition().getName();

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
