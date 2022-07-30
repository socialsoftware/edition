package pt.ist.socialsoftware.edition.ldod.bff.dtos;

import com.google.api.client.json.webtoken.JsonWebToken.Payload;

public class GoogleIdentityDto {

    private final String firstname;
    private final String lastname;
    private final String email;
    private final String socialMedia;
    private final String socialId;

    public GoogleIdentityDto(Payload payload, String provider) {
        this.email = (String) payload.get("email");
        this.lastname = (String) payload.get("family_name");
        this.firstname = (String) payload.get("given_name");
        this.socialId = payload.getSubject();
        this.socialMedia = provider;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
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
