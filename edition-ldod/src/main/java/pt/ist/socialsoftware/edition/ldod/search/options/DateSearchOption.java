package pt.ist.socialsoftware.edition.ldod.search.options;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.api.text.TextInterface;
import pt.ist.socialsoftware.edition.ldod.domain.*;
import pt.ist.socialsoftware.edition.ldod.search.SearchableElement;

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
	public Stream<SearchableElement> search(Stream<SearchableElement> inters) {
		return inters.filter(inter -> inter.getType() == SearchableElement.Type.SCHOLAR_INTER)
				.filter(i -> verifiesSearchOption(i));
	}

	public boolean verifiesSearchOption(SearchableElement inter) {
		TextInterface textInterface = new TextInterface();

		if (dated != Dated.ALL) {
			Source source;
			if (textInterface.isSourceInter(inter.getXmlId())) {
				//source = ((SourceInter) inter).getSource();
				return isInDate(textInterface.getSourceOfInter(inter.getXmlId()).getLdoDDate());
			} else {
				return isInDate(textInterface.getScholarInterDate(inter.getXmlId()));
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
