package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.ExpertEdition;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment;
import pt.ist.socialsoftware.edition.ldod.domain.LdoD;
import pt.ist.socialsoftware.edition.ldod.generators.PlainHtmlWriter4OneInter;
import pt.ist.socialsoftware.edition.ldod.recommendation.ReadingRecommendation;

public class ReadingDto {


	private List<ExpertEditionDto> sortedExpertEditionDto;
	private ArrayList<ArrayList<ExpertEditionInterDto>> expertEditionDtoList;
	private ExpertEditionInterDto expertEditionInterDto;
	private ExpertEditionInterDto prevCom;
	private List<ExpertEditionInterDto> recommendations;
	private String transcript;
	private FragmentDto fragment;
	private ReadingRecommendationDto readingRecommendation;

	public ReadingDto(LdoD instance, ExpertEditionInter expertEditionInter, Set<ExpertEditionInter> recommendations,
			ExpertEditionInter prevRecom, PlainHtmlWriter4OneInter writer, Fragment fragment) {
		this.setSortedExpertEditionDto(instance.getSortedExpertEdition().stream().map(ExpertEditionDto::new)
											.collect(Collectors.toList()));
		
		ArrayList<ArrayList<ExpertEditionInterDto>> expertEdInterList = new ArrayList<ArrayList<ExpertEditionInterDto>>();
		for(ExpertEdition expEd : instance.getSortedExpertEdition()) {
			expertEdInterList.add((ArrayList<ExpertEditionInterDto>) expEd.getSortedInter4Frag(fragment).stream().map(ExpertEditionInterDto::new).collect(Collectors.toList()));
		}

		this.setExpertEditionDtoList(expertEdInterList);
		this.setExpertEditionInterDto(new ExpertEditionInterDto(expertEditionInter));
		if(prevRecom != null) {
			this.setPrevCom(new ExpertEditionInterDto(prevRecom));
		}
		if(recommendations != null) {
			this.setRecommendations(recommendations.stream().map(ExpertEditionInterDto::new).collect(Collectors.toList()));
		}
		if(writer != null) {
			this.setTranscript(writer.getTranscription());
		}
		this.setFragment(new FragmentDto(fragment));
		
		
	}
	
	public ReadingDto(LdoD instance, ExpertEditionInter expertEditionInter, Set<ExpertEditionInter> recommendations,
			ExpertEditionInter prevRecom, PlainHtmlWriter4OneInter writer, Fragment fragment, ReadingRecommendation jsonRecommendation) {
		this.setSortedExpertEditionDto(instance.getSortedExpertEdition().stream().map(ExpertEditionDto::new)
											.collect(Collectors.toList()));
		
		ArrayList<ArrayList<ExpertEditionInterDto>> expertEdInterList = new ArrayList<ArrayList<ExpertEditionInterDto>>();
		for(ExpertEdition expEd : instance.getSortedExpertEdition()) {
			expertEdInterList.add((ArrayList<ExpertEditionInterDto>) expEd.getSortedInter4Frag(fragment).stream().map(ExpertEditionInterDto::new).collect(Collectors.toList()));
		}

		this.setExpertEditionDtoList(expertEdInterList);
		this.setExpertEditionInterDto(new ExpertEditionInterDto(expertEditionInter));
		if(prevRecom != null) {
			this.setPrevCom(new ExpertEditionInterDto(prevRecom));
		}
		if(recommendations != null) {
			this.setRecommendations(recommendations.stream().map(ExpertEditionInterDto::new).collect(Collectors.toList()));
		}
		if(writer != null) {
			this.setTranscript(writer.getTranscription());
		}
		this.setFragment(new FragmentDto(fragment));
		this.setReadingRecommendation(new ReadingRecommendationDto(jsonRecommendation));
		
	}


	public List<ExpertEditionDto> getSortedExpertEditionDto() {
		return sortedExpertEditionDto;
	}

	public void setSortedExpertEditionDto(List<ExpertEditionDto> sortedExpertEditionDto) {
		this.sortedExpertEditionDto = sortedExpertEditionDto;
	}


	public ArrayList<ArrayList<ExpertEditionInterDto>> getExpertEditionDtoList() {
		return expertEditionDtoList;
	}


	public void setExpertEditionDtoList(ArrayList<ArrayList<ExpertEditionInterDto>> expertEdInterList) {
		this.expertEditionDtoList = expertEdInterList;
	}


	public ExpertEditionInterDto getExpertEditionInterDto() {
		return expertEditionInterDto;
	}


	public void setExpertEditionInterDto(ExpertEditionInterDto expertEditionInterDto) {
		this.expertEditionInterDto = expertEditionInterDto;
	}


	public ExpertEditionInterDto getPrevCom() {
		return prevCom;
	}


	public void setPrevCom(ExpertEditionInterDto prevCom) {
		this.prevCom = prevCom;
	}


	public List<ExpertEditionInterDto> getRecommendations() {
		return recommendations;
	}


	public void setRecommendations(List<ExpertEditionInterDto> recommendations) {
		this.recommendations = recommendations;
	}


	public String getTranscript() {
		return transcript;
	}


	public void setTranscript(String transcript) {
		this.transcript = transcript;
	}


	public FragmentDto getFragment() {
		return fragment;
	}


	public void setFragment(FragmentDto fragment) {
		this.fragment = fragment;
	}


	public ReadingRecommendationDto getReadingRecommendation() {
		return readingRecommendation;
	}


	public void setReadingRecommendation(ReadingRecommendationDto readingRecommendationDto) {
		this.readingRecommendation = readingRecommendationDto;
	}

}
