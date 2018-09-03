package pt.ist.socialsoftware.edition.ldod.domain;

public class Frequency extends Frequency_Base {
	public Frequency(VirtualEdition edition, int frequency) {
		super.init(edition, Frequency.class);
		setFrequency(frequency);
	}
}
