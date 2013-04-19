package pt.ist.socialsoftware.edition.domain;

public class PrintedSource extends PrintedSource_Base {

	public PrintedSource() {
		super();
	}

	@Override
	public void print() {
		System.out.print(getTitle() + ":");
	}

	@Override
	public String getName() {
		return getTitle();
	}

	@Override
	public String getMetaTextual() {
		String result = "";

		result = result + "Título: " + getTitle() + "<br>";

		result = result + "Local de Publicação: " + getPubPlace() + "<br>";

		result = result + "Número: " + getIssue() + "<br>";

		result = result + "Data: " + getDate() + "<br>";

		return result;
	}

}
