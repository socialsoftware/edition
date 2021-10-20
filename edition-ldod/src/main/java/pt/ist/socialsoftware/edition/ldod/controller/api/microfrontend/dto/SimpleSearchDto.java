package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;


public class SimpleSearchDto {
	
	private int fragCount;
	private int interCount;
	private List<FragInterDto> listFragments;

	public SimpleSearchDto(Map<Fragment, List<FragInter>> results, int interCount) {
		this.setFragCount(results.size());
		this.setInterCount(interCount);
		List<FragInterDto> listFragmentAux = new ArrayList<FragInterDto>();
		for (Map.Entry<Fragment,List<FragInter>> entry : results.entrySet()) {
			for(FragInter frag : entry.getValue()) {
				listFragmentAux.add(new FragInterDto(frag, entry.getKey()));
			}
		}
		this.setListFragments(listFragmentAux);
            
	}

	public int getFragCount() {
		return fragCount;
	}

	public void setFragCount(int fragCount) {
		this.fragCount = fragCount;
	}

	public int getInterCount() {
		return interCount;
	}

	public void setInterCount(int interCount) {
		this.interCount = interCount;
	}


	public List<FragInterDto> getListFragments() {
		return listFragments;
	}

	public void setListFragments(List<FragInterDto> listFragments) {
		this.listFragments = listFragments;
	}

}
