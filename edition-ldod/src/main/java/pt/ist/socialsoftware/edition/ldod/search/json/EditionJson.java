package pt.ist.socialsoftware.edition.ldod.search.json;

import java.util.Map;

public class EditionJson {

	private String editor;
	private final String acronym;
	private Map<String,String> heteronyms;
	private String beginDate;
	private String endDate;

	public EditionJson(String acronym) {
		this.acronym = acronym;
	}

	public EditionJson(String acronym, String editor) {
		this.acronym = acronym;
		this.editor = editor;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getAcronym() {
		return acronym;
	}

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

	public Map<String,String> getHeteronyms() {
		return heteronyms;
	}

	public void setHeteronyms(Map<String,String> heteronyms) {
		this.heteronyms = heteronyms;
	}
}
