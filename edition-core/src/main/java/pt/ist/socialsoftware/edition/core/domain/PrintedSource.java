package pt.ist.socialsoftware.edition.core.domain;

import pt.ist.socialsoftware.edition.core.domain.PrintedSource_Base;

public class PrintedSource extends PrintedSource_Base {

	public PrintedSource() {
		super();
		setType(Source.SourceType.PRINTED);
	}

}
