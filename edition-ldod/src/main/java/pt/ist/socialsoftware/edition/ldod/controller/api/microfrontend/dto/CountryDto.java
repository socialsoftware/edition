package pt.ist.socialsoftware.edition.ldod.controller.api.microfrontend.dto;

public class CountryDto {
	
	private String country;
	private boolean contains;

	public CountryDto(String country, boolean b) {
		this.setCountry(country);
		this.setContains(b);
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public boolean isContains() {
		return contains;
	}

	public void setContains(boolean contains) {
		this.contains = contains;
	}
}
