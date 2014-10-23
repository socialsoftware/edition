package pt.ist.socialsoftware.edition.utils.search.options;

import org.codehaus.jackson.annotate.JsonProperty;

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
		ALL("all"), DATED("dated"), NONDATED("non-dated");

		private final String dated;

		private Dated(String dated) {
			this.dated = dated;
		}

		public String getDated() {
			return dated;
		}
	}

	private final String dated;
	private final Integer begin;
	private final Integer end;

	public DateSearchOption(@JsonProperty("option") String dated,
			@JsonProperty("begin") String begin, @JsonProperty("end") String end) {
		this.dated = dated;
		this.begin = begin == null ? null : Integer.parseInt(begin);
		this.end = end == null ? null : Integer.parseInt(end);

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
		if(!dated.equals(Dated.ALL.getDated())) {
			Source source;
			PrintedSource printed;
			ManuscriptSource manu;

			if(inter.getSourceType().equals(EditionType.AUTHORIAL)) {
				source = ((SourceInter) inter).getSource();

				if(source.getType().equals(SourceType.MANUSCRIPT)) {
					manu = (ManuscriptSource) ((SourceInter) inter).getSource();
					if(manu.getDate() == null && dated.equals(Dated.NONDATED.getDated())) {
						return true;
					} else if((manu.getDate() == null && dated.equals(Dated.DATED.getDated()))
							|| (manu.getDate() != null && dated.equals(Dated.NONDATED.getDated()))) {
						return false;
					} else if(dated.equals(Dated.DATED.getDated()) && (begin > manu.getDate().getYear() || end < manu.getDate().getYear())) {
						return false;
					}
				} else if(source.getType().equals(SourceType.PRINTED)) {
					printed = (PrintedSource) ((SourceInter) inter).getSource();
					if(printed.getDate() == null && dated.equals(Dated.NONDATED.getDated())) {
						return true;
					} else if(printed.getDate() == null && dated.equals(Dated.DATED.getDated()) || printed.getDate() != null
							&& dated.equals(Dated.NONDATED.getDated())) {
						return false;
					} else if(dated.equals(Dated.DATED.getDated()) && (begin > printed.getDate().getYear() || end < printed.getDate().getYear())) {
						return false;
					}
				}
			} else {
				if(inter.getDate() == null && dated.equals(Dated.NONDATED.getDated())) {
					return true;
				} else if((inter.getDate() == null && dated.equals(Dated.DATED.getDated()))
						|| (inter.getDate() != null && dated.equals(Dated.NONDATED.getDated()))) {
					return false;
				} else if(dated.equals(Dated.DATED.getDated()) && (begin > inter.getDate().getYear() || end < inter.getDate().getYear())) {
					return false;
				}
			}
		}
		return true;
	}
}
