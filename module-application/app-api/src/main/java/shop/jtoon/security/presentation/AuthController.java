package shop.jtoon.security.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthController {

	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public String healthCheck() {
		return "Health Check Success";
	}
}
