package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import pt.ist.socialsoftware.edition.ldod.domain.VirtualEdition;
import pt.ist.socialsoftware.edition.ldod.domain.Category;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;


import java.util.List;
import java.util.stream.Collectors;

import bsh.This;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDUser;

public class FragInterDto {
	
	private String xmlId;
	private String urlId;
	private String title;
	private String date;
	private String heteronym;
	private int number;
	private List<UsedDto> usedList;
	private List<CategoryDto> categoryList;
	private String acronym;
	private String reference;
	private int startPage;
	private String volume;
	private String editionTitle;
	private List<UserDto> userDtoList;

	public FragInterDto(FragInter fragInter) {
		this.setXmlId(fragInter.getFragment().getXmlId());
		this.setUrlId(fragInter.getUrlId());
		if(fragInter.getTitle() != null) {
			this.setTitle(fragInter.getTitle());
		}
		if(fragInter.getLdoDDate() != null) {
			this.setDate(fragInter.getLdoDDate().print());
		}
		
		if(fragInter.getHeteronym()!=null) {
			if(fragInter.getHeteronym().getName()!=null) {
				this.setHeteronym(fragInter.getHeteronym().getName());
			}
			
		}
		
		this.setNumber(fragInter.getNumber());
		this.setAcronym(fragInter.getEdition().getAcronym());
		this.setReference(fragInter.getEdition().getReference());
		
		this.setUsedList(fragInter.getListUsed().stream()
				.map(UsedDto::new)
				.collect(Collectors.toList()));
		
		if(fragInter instanceof ExpertEditionInter) {
			this.setStartPage(((ExpertEditionInter) fragInter).getStartPage());
			
			if(((ExpertEditionInter) fragInter).getVolume()!= null) {
				this.setVolume(((ExpertEditionInter) fragInter).getVolume());
			}
		}
		
		

		
	}
	
	public FragInterDto(FragInter fragInter, VirtualEdition edition) {
		this.setXmlId(fragInter.getFragment().getXmlId());
		this.setUrlId(fragInter.getUrlId());
		if(fragInter.getTitle() != null) {
			this.setTitle(fragInter.getTitle());
		}
		if(fragInter.getLdoDDate() != null) {
			this.setDate(fragInter.getLdoDDate().print());
		}
		
		if(fragInter.getHeteronym()!=null) {
			if(fragInter.getHeteronym().getName()!=null) {
				this.setHeteronym(fragInter.getHeteronym().getName());
			}
			
		}
		
		this.setNumber(fragInter.getNumber());
		this.setUsedList(fragInter.getListUsed().stream()
							.map(UsedDto::new)
							.collect(Collectors.toList()));
		

		this.setCategoryList(((VirtualEditionInter) fragInter).getAssignedCategories().stream()
					.map(category -> new CategoryDto(category, edition))
					.collect(Collectors.toList()));
		
	}
	
	public FragInterDto(FragInter fragInter, LdoDUser user) {
		this.setXmlId(fragInter.getFragment().getXmlId());
		this.setUrlId(fragInter.getUrlId());
		if(fragInter.getTitle() != null) {
			this.setTitle(fragInter.getTitle());
		}
		this.setReference(fragInter.getEdition().getReference());
		this.setAcronym(fragInter.getEdition().getAcronym());
		
		
		this.setCategoryList(((VirtualEditionInter) fragInter).getAssignedCategories(user).stream()
				.map(category -> new CategoryDto(category, (VirtualEdition) fragInter.getEdition()))
				.collect(Collectors.toList()));
		
		this.setUsedList(fragInter.getListUsed().stream()
							.map(UsedDto::new)
							.collect(Collectors.toList()));
		
		
	}
	
	public FragInterDto(FragInter fragInter, Category category) {
		this.setXmlId(fragInter.getFragment().getXmlId());
		this.setUrlId(fragInter.getUrlId());
		if(fragInter.getTitle() != null) {
			this.setTitle(fragInter.getTitle());
		}
		this.setAcronym(fragInter.getEdition().getAcronym());
		this.setEditionTitle(((VirtualEditionInter) fragInter).getEdition().getTitle());
		this.setUsedList(fragInter.getListUsed().stream()
				.map(UsedDto::new)
				.collect(Collectors.toList()));
		
		this.setUserDtoList(((VirtualEditionInter) fragInter).getContributorSet(category).stream()
										.map(UserDto::new)
										.collect(Collectors.toList()));
	}

	public String getXmlId() {
		return xmlId;
	}

	public void setXmlId(String xmlId) {
		this.xmlId = xmlId;
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}



	public String getHeteronym() {
		return heteronym;
	}

	public void setHeteronym(String heteronym) {
		this.heteronym = heteronym;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public List<UsedDto> getUsedList() {
		return usedList;
	}

	public void setUsedList(List<UsedDto> usedList) {
		this.usedList = usedList;
	}

	public List<CategoryDto> getCategoryList() {
		return categoryList;
	}

	public void setCategoryList(List<CategoryDto> categoryList) {
		this.categoryList = categoryList;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getEditionTitle() {
		return editionTitle;
	}

	public void setEditionTitle(String editionTitle) {
		this.editionTitle = editionTitle;
	}

	public List<UserDto> getUserDtoList() {
		return userDtoList;
	}

	public void setUserDtoList(List<UserDto> userDtoList) {
		this.userDtoList = userDtoList;
	}

}
