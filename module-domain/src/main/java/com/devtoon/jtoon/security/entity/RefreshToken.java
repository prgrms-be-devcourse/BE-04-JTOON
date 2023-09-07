package com.devtoon.jtoon.security.entity;

import static java.util.Objects.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "refresh_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

	@Id
	private String email;

	@Column(name = "refresh_token", nullable = false)
	private String refreshToken;

	@Builder
	private RefreshToken(String email, String refreshToken) {
		this.email = requireNonNull(email);
		this.refreshToken = requireNonNull(refreshToken);
	}

	public void updateToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public boolean matches(String refreshToken) {
		return this.refreshToken.equals(refreshToken);
	}
}
