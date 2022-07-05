package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.domain.LdoDDate_Base;
import pt.ist.socialsoftware.edition.ldod.domain.Fragment.PrecisionType;
import pt.ist.socialsoftware.edition.ldod.utils.DateUtils;

public class LdoDDate extends LdoDDate_Base {
	public static enum DateType {
		YEAR, MONTH, DAY
	}

	public LdoDDate(String value, PrecisionType precision) {
		setDate(DateUtils.convertDate(value));
		if (value.length() == 4) {
			setType(DateType.YEAR);
		} else if (value.length() == 7) {
			setType(DateType.MONTH);
		} else if (value.length() == 10) {
			setType(DateType.DAY);
		} else {
			setType(null);
		}
		setPrecision(precision);
	}

	public void remove() {
		setFragInter(null);
		setSource(null);
	}

	public String print() {
		switch (getType()) {
		case YEAR:
			return getDate().toString("yyyy");
		case DAY:
			return getDate().toString("dd-MM-yyyy");
		case MONTH:
			return getDate().toString("MM-yyyy");
		default:
			return "";
		}
	}

}
