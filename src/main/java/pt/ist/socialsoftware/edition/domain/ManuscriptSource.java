package pt.ist.socialsoftware.edition.domain;

public class ManuscriptSource extends ManuscriptSource_Base {

	public enum Form {
		LEAF
	};

	public enum Material {
		PAPER
	};

	public ManuscriptSource() {
		super();
	}

	@Override
	public void print() {
		System.out.print(getIdno() + ":");
	}

	@Override
	public String getName() {
		return getIdno();
	}

	@Override
	public String getMetaTextual() {
		String result = "";

		String form = getForm() == ManuscriptSource.Form.LEAF ? "Folha" : "";
		result = result + "Formato: " + form + "<br>";

		String material = getMaterial() == ManuscriptSource.Material.PAPER ? "Papel"
				: "";
		result = result + "Material: " + material + "<br>";

		String columns = getColumns() == 0 ? "" : Integer
				.toString(getColumns());
		result = result + "Colunas: " + columns + "<br>";

		result = result + "Notas: " + getNotes();

		return result;
	}

}
