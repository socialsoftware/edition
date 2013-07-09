package pt.ist.socialsoftware.edition.domain;

public class PrintedSource extends PrintedSource_Base {

	public PrintedSource() {
		super();
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

		result = result + "Facsimiles: ";

		for (Surface surf : getFacsimile().getSurfaces()) {
			result = result + "<a href=/static/facs/" + surf.getGraphic() + ">"
					+ surf.getGraphic() + "</a> ";
		}

		return result;
	}

}
