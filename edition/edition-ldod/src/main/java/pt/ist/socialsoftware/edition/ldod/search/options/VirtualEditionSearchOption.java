package pt.ist.socialsoftware.edition.ldod.search.options;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.domain.*;

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
	public Set<FragInter> search(Set<FragInter> inters) {
		return inters.stream().filter(VirtualEditionInter.class::isInstance).map(VirtualEditionInter.class::cast)
				.filter(i -> verifiesSearchOption(i)).collect(Collectors.toSet());
	}

	public boolean verifiesSearchOption(VirtualEditionInter inter) {
		Set<VirtualEdition> virtualEditions = LdoD.getInstance().getVirtualEditionsSet().stream().filter(virtualEdition -> virtualEdition.getPub()).collect(Collectors.toSet());
		if (LdoDUser.getAuthenticatedUser() != null) {
			virtualEditions.addAll(LdoDUser.getAuthenticatedUser().getSelectedVirtualEditionsSet());
		}
		if (this.inclusion) {
			if (virtualEdition.equals(ALL) && virtualEditions.contains(inter.getVirtualEdition())) {
				return true;
			}
			if (inter.getVirtualEdition().getAcronym().equals(virtualEdition)
					&& virtualEditions.contains(inter.getVirtualEdition())) {
				return true;
			}
		} else {
			if (virtualEdition.equals(ALL)) {
				return false;
			}
			if (!inter.getVirtualEdition().getAcronym().equals(virtualEdition)
					&& virtualEditions.contains(inter.getVirtualEdition())) {
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
