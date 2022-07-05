package pt.ist.socialsoftware.edition.ldod.search.json;


public class AuthoralJson {

	private String[] mediums;
	private DatesJson dates;

	public String[] getMediums() {
		return mediums;
	}

	public void setMediums(String[] mediums) {
		this.mediums = mediums;
	}

	public DatesJson getDates() {
		return dates;
	}

	public void setDates(DatesJson dates) {
		this.dates = dates;
	}
}
