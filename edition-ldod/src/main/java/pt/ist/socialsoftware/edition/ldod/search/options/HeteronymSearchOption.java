package pt.ist.socialsoftware.edition.ldod.search.options;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import pt.ist.socialsoftware.edition.ldod.domain.FragInter;
import pt.ist.socialsoftware.edition.ldod.domain.VirtualEditionInter;

public final class HeteronymSearchOption extends SearchOption {
	private static Logger logger = LoggerFactory.getLogger(HeteronymSearchOption.class);

	private final String xmlId4Heteronym;

	public HeteronymSearchOption(@JsonProperty("heteronym") String xmlId) {
		logger.debug("HeteronymSearchOption xmlId: {}", xmlId);
		this.xmlId4Heteronym = xmlId.equals("null") ? null : xmlId;
	}

	@Override
	public String toString() {
		return "heteronym:" + xmlId4Heteronym;
	}

	@Override
	public Set<FragInter> search(Set<FragInter> inters) {
		return inters.stream().filter(i -> !(i instanceof VirtualEditionInter) && verifiesSearchOption(i))
				.collect(Collectors.toSet());
	}

	public boolean verifiesSearchOption(FragInter inter) {
		if (ALL.equals(xmlId4Heteronym)) {
			// all are selected
			return true;
		} else if (xmlId4Heteronym == null && inter.getHeteronym().getXmlId() == null) {
			// Searching for fragments with no authors and fragment has no
			// author
			return true;
		} else if ((xmlId4Heteronym != null && inter.getHeteronym().getXmlId() == null)
				|| (xmlId4Heteronym == null && inter.getHeteronym().getXmlId() != null)) {
			// Searching for fragment with author and fragment has no author or
			// searching for fragment with no author and fragment has author and
			return false;
		} else {
			// the interpretation has the expected correct heteronym assignment
			return xmlId4Heteronym.equals(inter.getHeteronym().getXmlId());
		}
	}

	public boolean hasHeteronym() {
		return xmlId4Heteronym != null && !xmlId4Heteronym.equals(ALL);
	}
}
