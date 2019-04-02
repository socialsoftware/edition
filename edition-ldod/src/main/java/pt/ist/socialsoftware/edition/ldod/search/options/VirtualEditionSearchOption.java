package pt.ist.socialsoftware.edition.ldod.search.options;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.api.ldod.LdoDInterface;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.search.SearchableElement;

public final class   VirtualEditionSearchOption extends SearchOption {

	private final String virtualEdition;
	private final boolean inclusion;

	public VirtualEditionSearchOption(@JsonProperty("inclusion") String inclusion,
			@JsonProperty("edition") String virtualEdition) {
		if (inclusion.equals("in"))
			this.inclusion = true;
		else
			this.inclusion = false;
		this.virtualEdition = virtualEdition;
	}

	@Override
	public Stream<SearchableElement> search(Stream<SearchableElement> inters) {
		return inters.filter(searchableElement -> searchableElement.getType() == SearchableElement.Type.VIRTUAL_INTER)
				.filter(i -> verifiesSearchOption(i));
	}

	public boolean verifiesSearchOption(SearchableElement inter) {
		LdoDInterface ldoDInterface = new LdoDInterface();
		Set<String> virtualEditions = LdoD.getInstance().getVirtualEditionsSet().stream().filter(virtualEdition -> virtualEdition.getPub())
				.map(VirtualEdition::getAcronym).collect(Collectors.toSet());
		if (LdoDUser.getAuthenticatedUser() != null) {
			virtualEditions.addAll(LdoDUser.getAuthenticatedUser().getSelectedVirtualEditionsSet().stream()
					.map(VirtualEdition::getAcronym).collect(Collectors.toSet()));
		}
		if (this.inclusion) {
			if (virtualEdition.equals(ALL) && virtualEditions.stream().anyMatch(virtualEdition -> ldoDInterface.isInterInVirtualEdition(inter.getXmlId(),virtualEdition))) {
				return true;
			}
			if (ldoDInterface.getAcronymWithVeiId(inter.getXmlId()).equals(virtualEdition)
					&& virtualEditions.stream().anyMatch(virtualEdition -> ldoDInterface.isInterInVirtualEdition(inter.getXmlId(),virtualEdition))) {
				return true;
			}
		} else {
			if (virtualEdition.equals(ALL)) {
				return false;
			}
			if (!ldoDInterface.getAcronymWithVeiId(inter.getXmlId()).equals(virtualEdition)
					&& virtualEditions.stream().anyMatch(virtualEdition -> ldoDInterface.isInterInVirtualEdition(inter.getXmlId(),virtualEdition))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return null;
	}

}
