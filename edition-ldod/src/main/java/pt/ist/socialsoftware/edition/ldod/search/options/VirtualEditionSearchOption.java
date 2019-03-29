package pt.ist.socialsoftware.edition.ldod.search.options;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.api.ldod.LdoDInterface;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;
import pt.ist.socialsoftware.edition.ldod.search.SearchableElement;

public final class VirtualEditionSearchOption extends SearchOption {

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

		if (this.inclusion) {
			if (!virtualEdition.equals(ALL) && !(ldoDInterface.isInterInVirtualEdition(inter.getXmlId(),this.virtualEdition)
					&& ldoDInterface.isVirtualEditionInUserSelectionSet(inter.getXmlId()))) {
				return false;
			} else {
			}
		} else {
			if (!(!virtualEdition.equals(ALL) && !(ldoDInterface.isInterInVirtualEdition(inter.getXmlId(),this.virtualEdition)
					&& ldoDInterface.isVirtualEditionInUserSelectionSet(inter.getXmlId())))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return null;
	}

}
