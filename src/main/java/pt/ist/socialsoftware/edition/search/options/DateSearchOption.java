package pt.ist.socialsoftware.edition.search.options;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.domain.Edition.EditionType;
import pt.ist.socialsoftware.edition.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.domain.FragInter;
import pt.ist.socialsoftware.edition.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.domain.PrintedSource;
import pt.ist.socialsoftware.edition.domain.Source;
import pt.ist.socialsoftware.edition.domain.Source.SourceType;
import pt.ist.socialsoftware.edition.domain.SourceInter;
import pt.ist.socialsoftware.edition.domain.VirtualEditionInter;

public final class DateSearchOption extends SearchOption {

	enum Dated {
		ALL("all"), DATED("dated"), UNDATED("undated");

		private final String dated;

		private Dated(String dated) {
			this.dated = dated;
		}

		public String getDated() {
			return dated;
		}
	}

	private final Dated dated;
	private final Integer begin;
	private final Integer end;

	public DateSearchOption(@JsonProperty("option") String dated, @JsonProperty("begin") String begin,
			@JsonProperty("end") String end) {
		if (Dated.DATED.getDated().equals(dated)) {
			this.dated = Dated.DATED;
		} else if (Dated.UNDATED.getDated().equals(dated)) {
			this.dated = Dated.UNDATED;
		} else {
			this.dated = Dated.ALL;
		}
		this.begin = begin == null ? null : Integer.parseInt(begin);
		this.end = end == null ? null : Integer.parseInt(end);

	}

	@Override
	public String toString() {
		return "Date: dated:" + dated.getDated() + " begin:" + begin + " end:" + end;
	}

	@Override
	public boolean visit(ExpertEditionInter inter) {
		return this.betweenDates(inter);
	}

	@Override
	public boolean visit(SourceInter inter) {
		return this.betweenDates(inter);
	}

	@Override
	public boolean visit(VirtualEditionInter inter) {
		return this.betweenDates(inter);
	}

	public boolean betweenDates(FragInter inter) {
		if (dated != Dated.ALL) {
			Source source;
			PrintedSource printed;
			ManuscriptSource manu;

			if (inter.getSourceType().equals(EditionType.AUTHORIAL)) {
				source = ((SourceInter) inter).getSource();

				if (source.getType().equals(SourceType.MANUSCRIPT)) {
					manu = (ManuscriptSource) ((SourceInter) inter).getSource();
					if (manu.getLdoDDate() == null && dated.equals(Dated.UNDATED)) {
						return true;
					} else if ((manu.getLdoDDate() == null && dated.equals(Dated.DATED))
							|| (manu.getLdoDDate() != null && dated.equals(Dated.UNDATED))) {
						return false;
					} else if (dated.equals(Dated.DATED) && (begin > manu.getLdoDDate().getDate().getYear()
							|| end < manu.getLdoDDate().getDate().getYear())) {
						return false;
					}
				} else if (source.getType().equals(SourceType.PRINTED)) {
					printed = (PrintedSource) ((SourceInter) inter).getSource();
					if (printed.getLdoDDate() == null && dated.equals(Dated.UNDATED)) {
						return true;
					} else if (printed.getLdoDDate() == null && dated.equals(Dated.DATED)
							|| printed.getLdoDDate() != null && dated.equals(Dated.UNDATED)) {
						return false;
					} else if (dated.equals(Dated.DATED) && (begin > printed.getLdoDDate().getDate().getYear()
							|| end < printed.getLdoDDate().getDate().getYear())) {
						return false;
					}
				}
			} else {
				if (inter.getLdoDDate() == null && dated.equals(Dated.UNDATED)) {
					return true;
				} else if ((inter.getLdoDDate() == null && dated.equals(Dated.DATED))
						|| (inter.getLdoDDate() != null && dated.equals(Dated.UNDATED))) {
					return false;
				} else if (dated.equals(Dated.DATED) && (begin > inter.getLdoDDate().getDate().getYear()
						|| end < inter.getLdoDDate().getDate().getYear())) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean hasDate() {
		return dated != Dated.ALL;
	}
}
