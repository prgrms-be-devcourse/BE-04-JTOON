package com.devtoon.jtoon.security.filter;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import com.devtoon.jtoon.security.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final HandlerExceptionResolver handlerExceptionResolver;
	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		@NotNull HttpServletResponse response,
		@NotNull FilterChain filterChain
	) throws ServletException, IOException {
		String header = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (header != null && header.startsWith("Bearer")) {
			try {
				String token = header.split(" ")[1];
				jwtProvider.validateToken(token);
				Authentication auth = jwtProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(auth);
			} catch (RuntimeException e) {
				log.error("Invalid Token", e);
				handlerExceptionResolver.resolveException(request, response, null, e);
				return;
			}
		}
		filterChain.doFilter(request, response);
	}
}
