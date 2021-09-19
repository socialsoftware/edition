package pt.ist.socialsoftware.edition.text.domain;

import java.util.ArrayList;
import java.util.List;



public class Facsimile extends Facsimile_Base {

	public Facsimile(Source source, String xmlID) {
		setSource(source);
		setXmlId(xmlID);
	}

	public void addSurface(Surface surface) {
		Surface firstSurface = getFirstSurface();
		if (firstSurface == null) {
			setFirstSurface(surface);
			surface.setPrev(null);
			surface.setNext(null);
		} else {
			firstSurface.addSurface(surface);
		}
	}

	public List<Surface> getSurfaces() {
		List<Surface> surfaces = new ArrayList<Surface>();
		Surface surface = getFirstSurface();

		while (surface != null) {
			surfaces.add(surface);
			surface = surface.getNext();
		}

		return surfaces;
	}

	public void remove() {
		setSource(null);

		if (getFirstSurface() != null) {
			getFirstSurface().remove();
		}

		deleteDomainObject();
	}

}
