package pt.ist.socialsoftware.edition.ldod.search.options;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.domain.Edition;
import pt.ist.socialsoftware.edition.ldod.domain.ExpertEditionInter;
import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.LdoDDate;
import pt.ist.socialsoftware.edition.ldod.domain.Source;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;

public final class DateSearchOption extends SearchOption {
	private static Logger logger = LoggerFactory.getLogger(DateSearchOption.class);

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
	private int begin;
	private int end;

	public DateSearchOption(@JsonProperty("option") String dated, @JsonProperty("begin") String begin,
			@JsonProperty("end") String end) {
		logger.debug("DateSearchOption dated: {}, begin:{}, end:{}", dated, begin, end);

		if (Dated.DATED.getDated().equals(dated)) {
			this.dated = Dated.DATED;
			this.begin = begin == null ? 1911 : Integer.parseInt(begin);
			this.end = end == null ? 1935 : Integer.parseInt(end);
		} else if (Dated.UNDATED.getDated().equals(dated)) {
			this.dated = Dated.UNDATED;
		} else {
			this.dated = Dated.ALL;
		}

		logger.debug("DateSearchOption dated: {}, begin:{}, end:{}", this.dated, this.begin, this.end);

	}

	@Override
	public String toString() {
		return "Date: dated:" + dated.getDated() + " begin:" + begin + " end:" + end;
	}

	@Override
	public Set<FragInter> search(Set<FragInter> inters) {
		return inters.stream().filter(ExpertEditionInter.class::isInstance).map(ExpertEditionInter.class::cast)
				.filter(i -> verifiesSearchOption(i)).collect(Collectors.toSet());
	}

	public boolean verifiesSearchOption(FragInter inter) {
		if (dated != Dated.ALL) {
			Source source;
			if (inter.getSourceType().equals(Edition.EditionType.AUTHORIAL)) {
				source = ((SourceInter) inter).getSource();
				return isInDate(source.getLdoDDate());
			} else {
				return isInDate(inter.getLdoDDate());
			}
		}
		return true;
	}

	public boolean isInDate(LdoDDate ldoDDate) {
		if (ldoDDate == null && dated.equals(Dated.UNDATED)) {
			return true;
		} else if ((ldoDDate == null && dated.equals(Dated.DATED))
				|| (ldoDDate != null && dated.equals(Dated.UNDATED))) {
			return false;
		} else if (dated.equals(Dated.DATED)
				&& (begin > ldoDDate.getDate().getYear() || end < ldoDDate.getDate().getYear())) {
			return false;
		} else {
			return true;
		}
	}

	public boolean hasDate() {
		return !dated.equals(Dated.ALL);
	}
}
