package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.Arrays;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class GeographicLocation extends GeographicLocation_Base {
	// never used because "location" on Twitter can be anything weirdly written
	public GeographicLocation(VirtualEdition virtualEdition, String country, String location) {
		super.init(virtualEdition, GeographicLocation.class);
		setCountry(country);
		setLocation(location);
	}

	public GeographicLocation(VirtualEdition virtualEdition, String country) {
		super.init(virtualEdition, GeographicLocation.class);
		setCountry(country);
	}

	@Atomic(mode = TxMode.WRITE)
	public void edit(String country) {
		setCountry(country);
	}

	public boolean containsCountry(String country) {
		return Arrays.stream(this.getCountry().split(",")).anyMatch(country::equals);
	}
}
