package pt.ist.socialsoftware.edition.ldod.domain;

import pt.ist.socialsoftware.edition.ldod.domain.PrintedSource_Base;

public class PrintedSource extends PrintedSource_Base {

	public PrintedSource() {
		super();
		setType(Source.SourceType.PRINTED);
	}

}
