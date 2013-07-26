package pt.ist.socialsoftware.edition.domain;

import java.util.List;

import pt.ist.socialsoftware.edition.domain.Edition.SourceType;

public class VirtualEditionInter extends VirtualEditionInter_Base {

	public VirtualEditionInter(VirtualEdition virtualEdition, FragInter inter) {
		setFragment(inter.getFragment());
		setHeteronym(null);
		setDate(null);
		setVirtualEdition(virtualEdition);
		setNumber(virtualEdition.generateNextInterNumber());
		setUses(inter);
	}

	@Override
	public void remove() {
		setVirtualEdition(null);

		if (getUses() != null) {
			getUses().remove();
		}

		super.remove();
	}

	@Override
	public String getShortName() {
		return getVirtualEdition().getAcronym();
	}

	@Override
	public String getTitle() {
		return getUses().getTitle();
	}

	@Override
	public SourceType getSourceType() {
		return SourceType.VIRTUAL;
	}

	public int compareVirtualEditionInter(VirtualEditionInter other) {
		int diff = getNumber() - other.getNumber();
		int result = diff > 0 ? 1 : (diff < 0) ? -1 : 0;
		if (result != 0) {
			return result;
		} else {
			String myTitle = getTitle();
			String otherTitle = other.getTitle();
			return myTitle.compareTo(otherTitle);
		}
	}

	@Override
	public String getMetaTextual() {
		String result = "";

		result = result + "Edição Virtual: " + getVirtualEdition().getTitle()
				+ "(" + getVirtualEdition().getAcronym() + ")" + "<br>";

		result = result + "Edição Base: " + getLastUsed().getShortName()
				+ "<br>";

		result = result + "Título: " + getTitle() + "<br>";

		if (getHeteronym() != null) {
			result = result + "Heterónimo: " + getHeteronym().getName()
					+ "<br>";
		}

		result = result + "Data: " + getDate() + "<br>";

		return result;
	}

	@Override
	public boolean belongs2Edition(Edition edition) {
		return getVirtualEdition() == edition;
	}

	@Override
	public FragInter getLastUsed() {
		return getUses().getLastUsed();
	}

	@Override
	public Edition getEdition() {
		return getVirtualEdition();
	}

	@Override
	public List<FragInter> getListUsed() {
		List<FragInter> listUses = getUses().getListUsed();
		listUses.add(0, getUses());
		return listUses;
	}

}
