package com.estsoft.estsoft2ndproject.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.estsoft.estsoft2ndproject.custonException.AdditionalInformationRequireException;
import com.estsoft.estsoft2ndproject.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Bean
	public SecurityFilterChain sercurityFilterChain(HttpSecurity http, UserService userService) throws Exception {
		return http
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.authorizeHttpRequests(custom -> custom
				.requestMatchers("/").permitAll()
				.requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.*").permitAll()
				.requestMatchers("/admin/**").hasAuthority("관리자")
				.requestMatchers("/member/login").permitAll()
				.requestMatchers("/**").hasAnyAuthority("씨앗", "새싹", "묘목", "성목", "고목", "관리자")
				.anyRequest().permitAll()
			)
			.oauth2Login(oauth2 -> oauth2
				.loginPage("/")
				.defaultSuccessUrl("/", true)
				.userInfoEndpoint(endpoint -> endpoint.userService(userService))
				.failureHandler(((request, response, exception) -> {
					if (exception instanceof AdditionalInformationRequireException) {
						response.sendRedirect("/member/register");
					}
				}))
			)
			.logout(logout -> logout
				.logoutUrl("/member/logout")
				.addLogoutHandler((request, response, authentication) -> {
					String accessToken = (String)request.getSession().getAttribute("accessToken");
					if (accessToken != null) {
						userService.logoutFromKakao(accessToken);
						request.getSession().removeAttribute("accessToken");
					}
				})
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
			)
			.csrf(csrf -> csrf.disable())
			.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "https://kauth.kakao.com", "https://accounts.kakao.com"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		configuration.setExposedHeaders(Arrays.asList("Location"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
