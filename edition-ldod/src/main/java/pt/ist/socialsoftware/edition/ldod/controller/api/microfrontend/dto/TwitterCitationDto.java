package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

import java.util.List;
import java.util.stream.Collectors;

import pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation;

public class TwitterCitationDto {
	private String data;
	private String xmlId;
	private String title;
	private String sourceLink;
	private List<InfoRangeDto> rangeList;
	private int awareSetSize;
	private int numberOfRetweets;
	private String location;
	private String country;
	private String username;
	private String userProfileURL;
	private String userImageURL;

	public TwitterCitationDto(TwitterCitation citation) {
		this.setData(citation.getDate());
		this.setXmlId(citation.getFragment().getXmlId());
		this.setTitle(citation.getFragment().getTitle());
		this.setSourceLink(citation.getSourceLink());
		this.setRangeList(citation.getInfoRangeSet().stream().map(InfoRangeDto::new).collect(Collectors.toList()));
		this.setAwareSetSize(citation.getAwareAnnotationSet().size());
		this.setNumberOfRetweets(citation.getNumberOfRetweets());
		this.setLocation(citation.getLocation());
		this.setCountry(citation.getCountry());
		this.setUsername(citation.getUsername());
		this.setUserProfileURL(citation.getUserProfileURL());
		this.setUserImageURL(citation.getUserImageURL());
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getXmlId() {
		return xmlId;
	}

	public void setXmlId(String xmlId) {
		this.xmlId = xmlId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSourceLink() {
		return sourceLink;
	}

	public void setSourceLink(String sourceLink) {
		this.sourceLink = sourceLink;
	}

	public List<InfoRangeDto> getRangeList() {
		return rangeList;
	}

	public void setRangeList(List<InfoRangeDto> rangeList) {
		this.rangeList = rangeList;
	}

	public int getAwareSetSize() {
		return awareSetSize;
	}

	public void setAwareSetSize(int awareSetSize) {
		this.awareSetSize = awareSetSize;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getNumberOfRetweets() {
		return numberOfRetweets;
	}

	public void setNumberOfRetweets(int numberOfRetweets) {
		this.numberOfRetweets = numberOfRetweets;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserProfileURL() {
		return userProfileURL;
	}

	public void setUserProfileURL(String userProfileURL) {
		this.userProfileURL = userProfileURL;
	}

	public String getUserImageURL() {
		return userImageURL;
	}

	public void setUserImageURL(String userImageURL) {
		this.userImageURL = userImageURL;
	}
}
