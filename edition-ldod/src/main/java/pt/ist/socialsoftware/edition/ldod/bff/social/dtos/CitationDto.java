package pt.ist.socialsoftware.edition.ldod.bff.social.dtos;

import pt.ist.socialsoftware.edition.ldod.domain.Citation;
import pt.ist.socialsoftware.edition.ldod.domain.TwitterCitation;

import java.time.LocalDateTime;

public class CitationDto {
	
	private LocalDateTime formattedDate;
	private String xmlId;
	private String title;
	private String sourceLink;
	private String tweetText;
	private String location;
	private String username;
	private String country;

	public CitationDto(Citation citation) {
		this.setFormattedDate(citation.getFormatedDate());
		this.setXmlId(citation.getFragment().getXmlId());
		this.setTitle(citation.getFragment().getTitle());
		this.setSourceLink(citation.getSourceLink());
		
		TwitterCitation tCitation = (TwitterCitation) citation;
		
		this.setTweetText(tCitation.getTweetText());
		if(!tCitation.getLocation().equals("unknown")) {
			this.setLocation(tCitation.getLocation());
		}
		if(!tCitation.getCountry().equals("unknown")) {
			this.setCountry(tCitation.getCountry());
		}
		this.setUsername(tCitation.getUsername());
	}

	public LocalDateTime getFormattedDate() {
		return formattedDate;
	}

	public void setFormattedDate(LocalDateTime formattedDate) {
		this.formattedDate = formattedDate;
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

	public String getTweetText() {
		return tweetText;
	}

	public void setTweetText(String tweetText) {
		this.tweetText = tweetText;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
