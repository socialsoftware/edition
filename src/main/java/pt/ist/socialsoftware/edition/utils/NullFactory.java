package pt.ist.socialsoftware.edition.utils;

import pt.ist.socialsoftware.edition.domain.NullEdition;

public class NullFactory {

	private static NullEdition nullEdition = new NullEdition();

	public static NullEdition getNullEditionInstance() {
		return nullEdition;
	}

}
