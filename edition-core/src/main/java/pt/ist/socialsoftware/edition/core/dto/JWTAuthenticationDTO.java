package pt.ist.socialsoftware.edition.core.dto;

import pt.ist.socialsoftware.edition.core.security.SecurityConstants;

public class JWTAuthenticationDTO {
	private String accessToken;
	private String tokenType = SecurityConstants.TOKEN_PREFIX;

	public JWTAuthenticationDTO(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return this.tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
}
