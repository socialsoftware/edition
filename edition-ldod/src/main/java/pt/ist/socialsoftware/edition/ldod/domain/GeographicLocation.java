package pt.ist.socialsoftware.edition.ldod.domain;

public class GeographicLocation extends GeographicLocation_Base {
	public GeographicLocation(VirtualEdition virtualEdition, String country, String location) {
		super.init(virtualEdition, GeographicLocation.class);
		setCountry(country);
		setLocation(location);
	}
}
