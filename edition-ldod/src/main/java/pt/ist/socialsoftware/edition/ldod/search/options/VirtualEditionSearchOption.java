package pt.ist.socialsoftware.edition.ldod.search.options;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

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
	public Set<FragInter> search(Set<FragInter> inters) {
		return inters.stream().filter(VirtualEditionInter.class::isInstance).map(VirtualEditionInter.class::cast)
				.filter(i -> verifiesSearchOption(i)).collect(Collectors.toSet());
	}

	public boolean verifiesSearchOption(VirtualEditionInter inter) {
		if (this.inclusion) {
			if (!virtualEdition.equals(ALL) && !(inter.getVirtualEdition().getAcronym().equals(virtualEdition)
					&& LdoDUser.getAuthenticatedUser().getSelectedVirtualEditionsSet()
							.contains(inter.getVirtualEdition()))) {
				return false;
			} else {
			}
		} else {
			if (!(!virtualEdition.equals(ALL) && !(inter.getVirtualEdition().getAcronym().equals(virtualEdition)
					&& LdoDUser.getAuthenticatedUser().getSelectedVirtualEditionsSet()
							.contains(inter.getVirtualEdition())))) {
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
