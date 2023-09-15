package shop.jtoon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerExceptionResolver;


import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

	private final HandlerExceptionResolver handlerExceptionResolver;
	// private final JwtService jwtService;
	// private final CustomOAuth2UserService customOAuth2UserService;
	// private final OAuth2SuccessHandler oAuth2SuccessHandler;
	// private final OAuth2FailureHandler oAuth2FailureHandler;

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	// @Bean
	// public WebSecurityCustomizer webSecurityCustomizer() {
	// 	return web -> web.ignoring()
	// 		.requestMatchers("/members/sign-up")
	// 		.requestMatchers("/local-login");
	// }
	//
	// @Bean
	// public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	// 	http
	// 		.authorizeHttpRequests(request -> request
	// 			.requestMatchers("/members").permitAll()
	// 			.requestMatchers("/members/email-authorization").permitAll()
	// 			.requestMatchers("/members/**").hasAuthority("USER")
	// 			.anyRequest().permitAll())
	// 		.csrf(AbstractHttpConfigurer::disable)
	// 		.httpBasic(AbstractHttpConfigurer::disable)
	// 		.formLogin(AbstractHttpConfigurer::disable)
	// 		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	// 		.addFilterBefore(new JwtAuthenticationFilter(handlerExceptionResolver, jwtService),
	// 			UsernamePasswordAuthenticationFilter.class)
	// 		.oauth2Login(login -> login
	// 			.userInfoEndpoint(endpoint -> endpoint.userService(customOAuth2UserService))
	// 			.successHandler(oAuth2SuccessHandler)
	// 			.failureHandler(oAuth2FailureHandler))
	// 	;
	// 	return http.build();
	// }
}
