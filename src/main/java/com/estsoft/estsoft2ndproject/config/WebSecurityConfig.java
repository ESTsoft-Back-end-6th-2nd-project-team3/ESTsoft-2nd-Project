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

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {
	@Bean
	public SecurityFilterChain sercurityFilterChain(HttpSecurity http, UserService userService) throws Exception {
		return http
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.authorizeHttpRequests(custom -> custom
				.requestMatchers("/**").permitAll()
				.requestMatchers("/admin/**").hasAuthority("관리자").anyRequest().authenticated()
			)
			.oauth2Login(oauth2 -> oauth2
				.loginPage("/member/login")
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
