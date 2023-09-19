package shop.jtoon.security.service;

import org.springframework.security.core.Authentication;

public interface AuthenticationService {
	Authentication getAuthentication(String claimsEmail);
}
