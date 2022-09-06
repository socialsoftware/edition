package pt.ist.socialsoftware.edition.ldod.validator;

import org.springframework.stereotype.Component;
import pt.ist.socialsoftware.edition.ldod.bff.user.dtos.SignupDto;
import pt.ist.socialsoftware.edition.ldod.shared.exception.LdoDException;

@Component
public class SignupValidator {

    private final static String ALPHABETIC_REGEX = "^[-'a-zA-ZÀ-ÖØ-öø-ÿ ]+$";
    private final static String ALPHANUMERIC_REGEX = "^[0-9a-zA-Z]+$";
    private final static String PASSWORD_REGEX = "[0-9a-zA-Z]{6,}";
    private final static String EMAIL_REGEX = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

    public void validate(SignupDto signupDto) {
        if (test(signupDto.getFirstName(), ALPHABETIC_REGEX)) throw new LdoDException("Bad firstname");
        if (test(signupDto.getLastName(), ALPHABETIC_REGEX)) throw new LdoDException("Bad lastname");
        if (test(signupDto.getUsername(), ALPHANUMERIC_REGEX)) throw new LdoDException("Bad username");
        if (test(signupDto.getEmail(), EMAIL_REGEX)) throw new LdoDException("Bad email");
        if (test(signupDto.getPassword(), PASSWORD_REGEX)) throw new LdoDException("Bad password");
        if (!signupDto.isConduct()) throw new LdoDException("Conduct not checked");
    }

    private static boolean test(String string, String regex) {
        return !string.matches(regex);
    }


}
