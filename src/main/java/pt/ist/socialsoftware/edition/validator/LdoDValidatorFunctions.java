package pt.ist.socialsoftware.edition.validator;

public class LdoDValidatorFunctions {
	static public Boolean emptyOrWhitespaceString(String value) {
		return value.trim().equals("") ? true : false;
	}

	public static boolean hasBlanks(String value) {
		return value.split("\\s+").length != 1;
	}

}
