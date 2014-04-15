package pt.ist.socialsoftware.edition.domain;

public class Surface extends Surface_Base {

	public Surface(Facsimile facsimile, String graphic) {
		facsimile.addSurface(this);
		setGraphic(graphic);
		setPbText(null);
	}

	public Surface(Facsimile facsimile, String graphic, String xmlId) {
		facsimile.addSurface(this);
		setGraphic(graphic);
		setXmlId(xmlId);
		setPbText(null);
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
		setPbText(null);

		if (getFacsimile() != null) {
			setFacsimile(null);
		}

		if (getNext() != null) {
			getNext().remove();
		}

		for (RefText ref : getRefTextSet()) {
			ref.setSurface(null);
		}

		deleteDomainObject();
	}

}
