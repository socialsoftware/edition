package pt.ist.socialsoftware.edition.virtual.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	public boolean containsEveryCountry() {
		List<String> countriesList = new ArrayList<String>();
		countriesList.add("Portugal");
		countriesList.add("Brazil");
		countriesList.add("Spain");
		countriesList.add("United Kingdom");
		countriesList.add("United States");
		countriesList.add("Lebanon");
		countriesList.add("Angola");
		countriesList.add("Mozambique");

		boolean res = false;
		for (String country : countriesList) {
			if (this.getCountry().contains(country)) {
				res = true;
			} else {
				res = false;
				break;
			}
		}

		return res;
	}
}
