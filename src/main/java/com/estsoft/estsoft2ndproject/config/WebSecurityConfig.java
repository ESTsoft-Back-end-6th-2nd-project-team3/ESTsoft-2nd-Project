package com.estsoft.estsoft2ndproject.config;

import java.io.PrintWriter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.estsoft.estsoft2ndproject.custonException.AdditionalInformationRequireException;
import com.estsoft.estsoft2ndproject.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {
	@Bean
	public SecurityFilterChain sercurityFilterChain(HttpSecurity http, UserService userService) throws Exception {
		return http.authorizeHttpRequests(custom -> custom
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
	public AuthenticationSuccessHandler successHandler() {
		return ((request, response, authentication) -> {
			DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User)authentication.getPrincipal();

			String id = defaultOAuth2User.getAttributes().get("id").toString();
			String body = """
				{"id":"%s"}
				""".formatted(id);

			log.error("body: {}", body);

			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setCharacterEncoding("UTF-8");

			PrintWriter writer = response.getWriter();
			writer.println(body);
			writer.flush();
		});
	}
}
