package pt.ist.socialsoftware.edition.virtual.domain;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class TimeWindow extends TimeWindow_Base {
	public TimeWindow(VirtualEdition edition, LocalDate beginDate, LocalDate endDate) {
		super.init(edition, TimeWindow.class);
		setBeginDate(beginDate);
		setEndDate(endDate);
	}

	@Atomic(mode = TxMode.WRITE)
	public void edit(LocalDate beginDate, LocalDate endDate) {
		setBeginDate(beginDate);
		setEndDate(endDate);
	}
}
