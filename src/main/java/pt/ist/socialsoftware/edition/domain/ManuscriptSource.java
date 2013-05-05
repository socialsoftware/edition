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
	public String getName() {
		return getAltIdentifier();
	}

	@Override
	public String getMetaTextual() {
		String result = "";

		result = result + "Identificação: " + getIdno() + "<br>";

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

	@Override
	public void remove() {
		super.remove();

		deleteDomainObject();
	}

}
