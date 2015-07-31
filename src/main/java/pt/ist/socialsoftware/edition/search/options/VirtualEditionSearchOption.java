package pt.ist.socialsoftware.edition.search.options;

import org.codehaus.jackson.annotate.JsonProperty;

import pt.ist.socialsoftware.edition.domain.LdoDUser;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

public final class VirtualEditionSearchOption extends SearchOption {

	private final String virtualEdition;
	private final boolean inclusion;

	public VirtualEditionSearchOption(@JsonProperty("inclusion") String inclusion,
			@JsonProperty("edition") String virtualEdition) {
		if(inclusion.equals("in"))
			this.inclusion = true;
		else
			this.inclusion = false;
		this.virtualEdition = virtualEdition;
	}

	@Override
	public boolean visit(VirtualEditionInter inter) {
		if(this.inclusion) {
			if(!virtualEdition.equals(ALL)
					&& !(inter.getVirtualEdition().getAcronym().equals(virtualEdition) && LdoDUser.getUser()
							.getSelectedVirtualEditionsSet().contains(inter.getVirtualEdition()))) {
				return false;
			} else {
			}
		} else {
			if(!(!virtualEdition.equals(ALL) && !(inter.getVirtualEdition().getAcronym().equals(virtualEdition) && LdoDUser
					.getUser().getSelectedVirtualEditionsSet().contains(inter.getVirtualEdition())))) {
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
