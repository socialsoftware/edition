package pt.ist.socialsoftware.edition.ldod.search.json;

public class DatesJson {
	private String beginDate;
	private String endDate;
	
	public String getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(int i) {
		this.beginDate = Integer.toString(i);
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(int i) {
		this.endDate = Integer.toString(i);
	}
}
