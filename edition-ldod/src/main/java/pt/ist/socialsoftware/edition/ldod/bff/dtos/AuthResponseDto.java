package pt.ist.socialsoftware.edition.ldod.bff.dtos;

public class AuthResponseDto {

    private final String message;
    private final boolean ok;


    public AuthResponseDto(boolean ok, String message) {
        this.message = message;
        this.ok = ok;
    }

    public AuthResponseDto(AuthResponseDtoBuilder builder) {
        this.message = builder.message;
        this.ok = builder.ok;
    }

    public String getMessage() {
        return message;
    }

    public boolean getOk() {
        return ok;
    }

    public static class AuthResponseDtoBuilder {
        private String message;

        private final boolean ok;

        public AuthResponseDtoBuilder(boolean ok) {
            this.ok = ok;
        }

        public AuthResponseDtoBuilder message(String message) {
            this.message = message;
            return this;
        }

        public AuthResponseDto build() {
            return new AuthResponseDto(this);
        }


    }
}
