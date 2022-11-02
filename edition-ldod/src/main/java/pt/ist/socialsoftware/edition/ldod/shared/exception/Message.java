package pt.ist.socialsoftware.edition.ldod.shared.exception;

public enum Message {

    DUPLICATE_ACRONYM("Duplicate edition acronym: '%s'"),
    EMPTY_ACRONYM("Invalid edition acronym: empty"),
    EMPTY_TITLE("Invalid edition title: empty"),
    ACRONYM_BLANKS("Invalid acronym '%s': contains white spaces"),
    ACRONYM_ALPHANUMERIC("Invalid acronym '%s': contains alphanumeric characters"),
    ACRONYM_LENGTH_EXCEED("Invalid acronym '%s': exceeds 10 characters length"),
    INVALID_FILE("Invalid file '%s'"),

    VIRTUAL_EDITION_FRAGS_IMPORTED("%s virtual fragments imported"),
    TEI_CORPUS_LOADED("TEI Corpus file '%s' imported"),
    VIRTUAL_CORPUS_LOADED("Virtual Corpus file '%s' imported"),

    VIRTUAL_EDITION_INVALID("Virtual edition with id '%s' invalid"),
    VIRTUAL_FRAGMENT_ALREADY_IMPORTED("Virtual fragment '%s' already uploaded"),
    TAXONOMY_NOT_FOUND("Taxonomy not found"),
    CATEGORY_NOT_FOUND("Category not found"),
    VIRTUAL_EDITION_NOT_FOUND("Virtual edition with acronym '%s' not found"),

    ELEMENT_NOT_FOUND("Element not found"),
    USER_NOT_FOUND("User not found"),
    USERNAME_NOT_FOUND("User with username '%s' not found"),
    MEMBER_PARTICIPATION_SUBMITTED("Member participation submitted"),
    OPERATION_NOT_AUTHORIZED("Operation not authorized"),

    VIRTUAL_INTER_NOT_FOUND("Virtual fragment interpretation not found"),
    GAME_NOT_FOUND("Classification Game not found"),
    EMPTY_DESCRIPTION("Invalid description: empty"),
    GAME_CAN_NOT_BE_REMOVED("Game can not be removed"),

    PASSWORD_CHANGED("Password successfully changed"),
    TOKEN_AUTHORIZED("User registration has been authorized"),
    TOKEN_NOT_AUTHORIZED("User registration not authorized"),
    TOKEN_CONFIRMED("User registration has been confirmed"),
    FRAGMENT_NOT_ASSOCIATED_OR_CORPUS_NOT_LOADED("No fragment is associated to edition %s or corpus has to be generated"),
    DUPLICATE_CATEGORY_NAME("Duplicate Category name: '%s'"),
    BAD_CREDENTIALS("Credentials not valid");


    private final String label;

    public String getLabel() {
        return label;
    }

    Message(String label) {
        this.label = label;
    }


}
