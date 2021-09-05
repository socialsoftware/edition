package pt.ist.socialsoftware.edition.virtual.domain;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class MediaSource extends MediaSource_Base {
	public MediaSource(VirtualEdition edition, String name) {
		super.init(edition, MediaSource.class);
		setName(name);
	}

	@Atomic(mode = TxMode.WRITE)
	public void edit(String name) {
		this.setName(name);
	}
}
