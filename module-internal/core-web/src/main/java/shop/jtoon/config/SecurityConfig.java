package shop.jtoon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import lombok.RequiredArgsConstructor;
import shop.jtoon.security.filter.JwtAuthenticationFilter;
import shop.jtoon.security.handler.OAuth2FailureHandler;
import shop.jtoon.security.handler.OAuth2SuccessHandler;
import shop.jtoon.security.service.CustomOAuth2UserService;
import shop.jtoon.security.service.JwtInternalService;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final JwtInternalService jwtInternalService;
	private final HandlerExceptionResolver handlerExceptionResolver;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OAuth2SuccessHandler oAuth2SuccessHandler;
	private final OAuth2FailureHandler oAuth2FailureHandler;

	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring()
			.requestMatchers("/members/sign-up")
			.requestMatchers("/members/email-authorization")
			.requestMatchers("/members/local-login");
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests(request -> request
				.requestMatchers("/members").permitAll()
				.requestMatchers("/members/**").hasAuthority("USER")
				.anyRequest().permitAll())
			.csrf(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.addFilterBefore(new JwtAuthenticationFilter(handlerExceptionResolver, jwtInternalService),
				UsernamePasswordAuthenticationFilter.class)
			.oauth2Login(login -> login
				.userInfoEndpoint(endpoint -> endpoint.userService(customOAuth2UserService))
				.successHandler(oAuth2SuccessHandler)
				.failureHandler(oAuth2FailureHandler))
		;
		return http.build();
	}
}
