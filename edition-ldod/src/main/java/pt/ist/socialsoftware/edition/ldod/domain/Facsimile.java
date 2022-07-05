package pt.ist.socialsoftware.edition.ldod.domain;

import java.util.ArrayList;
import java.util.List;

import pt.ist.socialsoftware.edition.ldod.domain.Facsimile_Base;

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
