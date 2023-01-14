package pt.ist.socialsoftware.edition.ldod.bff.user.dtos;

import com.google.api.client.json.webtoken.JsonWebToken.Payload;

public class GoogleIdentityDto {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String socialMedia;
    private final String socialId;

    public GoogleIdentityDto(Payload payload, String provider) {
        this.email = (String) payload.get("email");
        this.lastName = (String) payload.get("family_name");
        this.firstName = (String) payload.get("given_name");
        this.socialId = payload.getSubject();
        this.socialMedia = provider;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getSocialId() {
        return socialId;
    }

    public String getSocialMedia() {
        return socialMedia;
    }
}
