package pt.ist.socialsoftware.edition.domain;

public class Surface extends Surface_Base {

	public Surface(Facsimile facsimile, String graphic) {
		facsimile.addSurface(this);
		setGraphic(graphic);
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

		deleteDomainObject();
	}

}
