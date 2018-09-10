package pt.ist.socialsoftware.edition.text.domain;

public class Surface extends Surface_Base {

	public Surface(Facsimile facsimile, String graphic) {
		facsimile.addSurface(this);
		setGraphic(graphic);
	}

	public Surface(Facsimile facsimile, String graphic, String xmlId) {
		facsimile.addSurface(this);
		setGraphic(graphic);
		setXmlId(xmlId);
	}

	public void addSurface(Surface surface) {
		Surface next = getNext();

		if (next == null) {
			surface.setFacsimile(null);
			surface.setNext(null);
			this.setNext(surface);
		} else {
			next.addSurface(surface);
		}
	}

	public void remove() {
		setFacsimile(null);
		setPrev(null);

		if (getFacsimile() != null) {
			setFacsimile(null);
		}

		if (getNext() != null) {
			getNext().remove();
		}

		for (RefText ref : getRefTextSet()) {
			ref.setSurface(null);
		}

		for (PbText pbText : getPbTextSet()) {
			removePbText(pbText);
		}

		deleteDomainObject();
	}

}
