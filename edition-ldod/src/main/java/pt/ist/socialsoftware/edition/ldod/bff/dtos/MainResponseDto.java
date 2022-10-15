package pt.ist.socialsoftware.edition.ldod.bff.dtos;

public class MainResponseDto {

    private final String message;
    private final boolean ok;


    public MainResponseDto(boolean ok, String message) {
        this.message = message;
        this.ok = ok;
    }

    public MainResponseDto(AuthResponseDtoBuilder builder) {
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


        public MainResponseDto build() {
            return new MainResponseDto(this);
        }


    }
}
