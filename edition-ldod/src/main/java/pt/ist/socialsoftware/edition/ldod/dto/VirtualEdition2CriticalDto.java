package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.ArrayList;
import java.util.List;

public class VirtualEdition2CriticalDto {
	List<Fragment2CriticalDto> fragments = new ArrayList<>();

	public VirtualEdition2CriticalDto() {
	}

	public List<Fragment2CriticalDto> getFragments() {
		return this.fragments;
	}

	public void setFragments(List<Fragment2CriticalDto> fragments) {
		this.fragments = fragments;
	}

}
