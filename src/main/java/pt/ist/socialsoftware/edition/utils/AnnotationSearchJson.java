package pt.ist.socialsoftware.edition.utils;

import java.io.Serializable;
import java.util.List;

public class AnnotationSearchJson implements Serializable {
	private static final long serialVersionUID = 1L;

	private int total;
	private List<AnnotationJson> rows;

	public AnnotationSearchJson(List<AnnotationJson> annotations) {
		setTotal(annotations.size());
		setRows(annotations);
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<AnnotationJson> getRows() {
		return rows;
	}

	public void setRows(List<AnnotationJson> rows) {
		this.rows = rows;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
