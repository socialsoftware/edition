package pt.ist.socialsoftware.edition.ldod.dto;

import java.util.ArrayList;
import java.util.List;

public class EditionFragmentsDto {
	List<FragmentDto> fragments = new ArrayList<>();

	public EditionFragmentsDto() {
	}

	public List<FragmentDto> getFragments() {
		return this.fragments;
	}

	public void setFragments(List<FragmentDto> fragments) {
		this.fragments = fragments;
	}

}
