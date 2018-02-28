package pt.ist.socialsoftware.edition.core.dto;

import java.util.ArrayList;
import java.util.List;

public class EditionFragmentsDTO {
	List<FragmentDTO> fragments = new ArrayList<>();

	public EditionFragmentsDTO() {
	}

	public List<FragmentDTO> getFragments() {
		return this.fragments;
	}

	public void setFragments(List<FragmentDTO> fragments) {
		this.fragments = fragments;
	}

}
