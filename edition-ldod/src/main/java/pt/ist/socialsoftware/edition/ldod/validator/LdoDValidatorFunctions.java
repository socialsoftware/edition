package pt.ist.socialsoftware.edition.ldod.validator;

public class LdoDValidatorFunctions {
	static public Boolean emptyOrWhitespaceString(String value) {
		return value.trim().equals("");
	}

	public static boolean hasBlanks(String value) {
		return value.split("\\s+").length != 1;
	}

	public static boolean hasDots(String value) {
		return value.contains(".");
	}

	public static boolean lengthLimit(String value, int length) {
		return value.length() > length;
	}

}
