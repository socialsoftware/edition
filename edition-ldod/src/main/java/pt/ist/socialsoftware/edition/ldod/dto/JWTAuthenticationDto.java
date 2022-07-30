package pt.ist.socialsoftware.edition.ldod.dto;

import pt.ist.socialsoftware.edition.ldod.utils.PropertiesManager;

public class JWTAuthenticationDto {
	private String accessToken;
	private String tokenType = PropertiesManager.getProperties().getProperty("spring.security.jwt.token.prefix");

	public JWTAuthenticationDto(){}
	public JWTAuthenticationDto(String accessToken) {
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
