package pt.ist.socialsoftware.edition.ldod.search.options;

import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.ManuscriptSource;
import pt.ist.socialsoftware.edition.ldod.domain.SourceInter;
import pt.ist.socialsoftware.edition.ldod.domain.Source.SourceType;

public abstract class AuthoralSearchOption extends SearchOption {

	private final String hasLdoD;
	private final DateSearchOption dateSearchOption;

	public AuthoralSearchOption(@JsonProperty("hasLdoDMark") String hasLdoD,
			@JsonProperty("date") DateSearchOption date) {
		this.hasLdoD = hasLdoD;
		this.dateSearchOption = date;
	}

	@Override
	public Set<FragInter> search(Set<FragInter> inters) {
		return inters.stream().filter(SourceInter.class::isInstance).map(SourceInter.class::cast)
				.filter(i -> verifiesSearchOption(i)).collect(Collectors.toSet());
	}

	private boolean verifiesSearchOption(SourceInter inter) {
		if (inter.getSource().getType().equals(SourceType.MANUSCRIPT)) {
			ManuscriptSource source = (ManuscriptSource) inter.getSource();
			if (isOfDocumentType(source) && dateSearchOption.verifiesSearchOption(inter)) {
				if (hasLdoD.equals(ALL) || (hasLdoD.equals("true") && source.getHasLdoDLabel()))
					return true;
				else if (hasLdoD.equals("false") && !source.getHasLdoDLabel())
					return true;
			}
		}
		return false;
	}

	protected abstract boolean isOfDocumentType(ManuscriptSource source);

	protected abstract String getDocumentType();

	@Override
	public String toString() {
		return getDocumentType() + ": hasLdoDMark:" + hasLdoD + "\n" + dateSearchOption;
	}

	public boolean hasDate() {
		return dateSearchOption == null ? false : dateSearchOption.hasDate();
	}

	public boolean hasLdoDMark() {
		return !hasLdoD.equals(ALL);
	}
}
