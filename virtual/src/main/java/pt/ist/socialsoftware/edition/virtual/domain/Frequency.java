package pt.ist.socialsoftware.edition.virtual.domain;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class Frequency extends Frequency_Base {
	public Frequency(VirtualEdition edition, int frequency) {
		super.init(edition, Frequency.class);
		setFrequency(frequency);
	}

	@Atomic(mode = TxMode.WRITE)
	public void edit(int frequency) {
		setFrequency(frequency);
	}
}
