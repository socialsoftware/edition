package pt.ist.socialsoftware.edition.domain;

public class ManuscriptSource extends ManuscriptSource_Base {

	public enum Form {
		LEAF
	};

	public enum Material {
		PAPER
	};

	public enum Medium {
		PEN("pen"), PENCIL("pencil"), BLUE_INK("blue-ink"), BLACK_INK(
				"black-ink");

		private final String desc;

		Medium(String desc) {
			this.desc = desc;
		}

		public String getDesc() {
			return desc;
		}
	};

	public ManuscriptSource() {
		setType(SourceType.MANUSCRIPT);
		setHasLdoDLabel(false);
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

		result = result + "LdoD: " + getHasLdoDLabel() + "<br>";

		for (HandNote handNote : getHandNoteSet()) {
			result = result + "Medium: " + handNote.getMedium().getDesc()
					+ ", Nota: " + handNote.getNote()
					+ ", Número de parágrafos referidos: "
					+ handNote.getTextPortionSet().size() + "<br>";
		}

		for (TypeNote typeNote : getTypeNoteSet()) {
			result = result + "Medium: " + typeNote.getMedium().getDesc()
					+ ", Nota: " + typeNote.getNote()
					+ ", Número de parágrafos referidos: "
					+ typeNote.getTextPortionSet().size() + "<br>";
		}

		result = result + "Notas: " + getNotes() + "<br>";

		Facsimile facs = getFacsimile();
		if (facs != null) {
			result = result + "Facsimiles: ";

			int i = 1;
			for (Surface surf : facs.getSurfaces()) {
				String suffix = facs.getSurfaces().size() == 1 ? "" : "."
						+ Integer.toString(i);
				result = result + "<a href=/facs/" + surf.getGraphic()
						+ " target=" + "\"" + "_blank" + "\"" + ">"
						+ getAltIdentifier() + suffix + "</a> ";
				i++;
			}
		}

		result = result + "<br>";

		if (getDate() != null) {
			String precision = getPrecision() != null ? " Precisão: "
					+ getPrecision().getDesc() : "";

			result = result + "Data: " + getDate().toString("dd-MM-yyyy")
					+ precision + "<br>";
		}

		return result;
	}

	@Override
	public void remove() {
		for (HandNote handNote : getHandNoteSet()) {
			handNote.remove();
		}

		for (TypeNote typeNote : getTypeNoteSet()) {
			typeNote.remove();
		}

		super.remove();
	}
}
