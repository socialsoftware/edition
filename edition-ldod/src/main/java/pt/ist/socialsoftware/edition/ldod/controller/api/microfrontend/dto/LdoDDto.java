package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;

public class LdoDDto {

	private VirtualEditionDto archiveEdition;
	private List<ExpertEditionDto> sortedExpert;

	public LdoDDto(LdoD instance, Fragment fragment, LdoDUser user, ArrayList<FragInter> inters) {
		this.setSortedExpert(instance.getSortedExpertEdition().stream().map(expertEd -> new ExpertEditionDto(expertEd, fragment)).collect(Collectors.toList()));
		if(inters.size()>0) {
			this.setArchiveEdition(new VirtualEditionDto(instance.getArchiveEdition(), fragment, user, inters.get(0)));
		}
	}

	/////////////////// VIRTUAL ////////////////////////
	public LdoDDto(LdoD instance, Fragment fragment, LdoDUser user, ArrayList<FragInter> inters, String type) {
		this.setSortedExpert(instance.getSortedExpertEdition().stream().map(expertEd -> new ExpertEditionDto(expertEd, fragment)).collect(Collectors.toList()));
		if(inters.size()>0) {
			this.setArchiveEdition(new VirtualEditionDto(instance.getArchiveEdition(), fragment, user, inters.get(0)));
		}
		else {
			this.setArchiveEdition(new VirtualEditionDto(instance.getArchiveEdition(), fragment, user, null));
		}
	}
	
	public VirtualEditionDto getArchiveEdition() {
		return archiveEdition;
	}

	public void setArchiveEdition(VirtualEditionDto archiveEdition) {
		this.archiveEdition = archiveEdition;
	}


	public List<ExpertEditionDto> getSortedExpert() {
		return sortedExpert;
	}


	public void setSortedExpert(List<ExpertEditionDto> sortedExpert) {
		this.sortedExpert = sortedExpert;
	}

}
