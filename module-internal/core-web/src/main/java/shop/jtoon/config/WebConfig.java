// package shop.jtoon.config;
//
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
// import com.devtoon.jtoon.global.util.RegExp;
// import com.devtoon.jtoon.security.interceptor.MemberInterceptor;
//
// @Configuration
// public class WebConfig implements WebMvcConfigurer {
//
// 	private static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";
//
// 	@Override
// 	public void addInterceptors(InterceptorRegistry registry) {
// 		registry.addInterceptor(new MemberInterceptor())
// 			.addPathPatterns("/**");
// 	}
//
// 	@Override
// 	public void addCorsMappings(final CorsRegistry registry) {
// 		registry.addMapping("/**")
// 			.allowedOriginPatterns(RegExp.ALLOW_ORIGIN_PATTERN)
// 			.allowedMethods(ALLOWED_METHOD_NAMES.split(","))
// 			.allowedHeaders("*")
// 			.allowCredentials(true)
// 			.maxAge(3600);
// 	}
// }
