package pt.ist.socialsoftware.edition.ldod.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ist.socialsoftware.edition.ldod.domain.Heteronym_Base;

public class Heteronym extends Heteronym_Base implements Comparable<Heteronym> {
	public static Logger logger = LoggerFactory.getLogger(Heteronym.class);

	public Heteronym() {
		super();
	}

	public Heteronym(Text text, String name) {
		setText(text);
		setName(name);
	}

	public void remove() {
		setText(null);

		getSourceSet().stream().forEach(s -> removeSource(s));
		getFragInterSet().stream().forEach(i -> removeFragInter(i));

		deleteDomainObject();
	}

	@Override
	public int compareTo(Heteronym o) {
		return this.getXmlId().compareTo(o.getXmlId());
	}

	public boolean isNullHeteronym() {
		return false;
	}

}
