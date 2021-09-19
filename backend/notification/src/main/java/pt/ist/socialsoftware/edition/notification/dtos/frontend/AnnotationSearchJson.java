package pt.ist.socialsoftware.edition.notification.dtos.frontend;//package pt.ist.socialsoftware.edition.ldod.frontend.utils;

import java.io.Serializable;
import java.util.List;

public class AnnotationSearchJson implements Serializable {
	private static final long serialVersionUID = 1L;

	private int total;
	private List<AnnotationDTO> rows;

	public AnnotationSearchJson(List<AnnotationDTO> annotations) {
		setTotal(annotations.size());
		setRows(annotations);
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<AnnotationDTO> getRows() {
		return rows;
	}

	public void setRows(List<AnnotationDTO> rows) {
		this.rows = rows;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
