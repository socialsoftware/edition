package pt.ist.socialsoftware.edition.ldod.domain;

import org.joda.time.LocalDate;

public class TimeWindow extends TimeWindow_Base {
	public TimeWindow(VirtualEdition edition, LocalDate beginDate, LocalDate endDate) {
		super.init(edition, TimeWindow.class);
		setBeginDate(beginDate);
		setEndDate(endDate);
	}
}
