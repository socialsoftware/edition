package pt.ist.socialsoftware.edition.core.domain;

public class MediaSource extends MediaSource_Base {
	public MediaSource(VirtualEdition edition, String name) {
		super.init(edition, MediaSource.class);
		setName(name);
	}
}
