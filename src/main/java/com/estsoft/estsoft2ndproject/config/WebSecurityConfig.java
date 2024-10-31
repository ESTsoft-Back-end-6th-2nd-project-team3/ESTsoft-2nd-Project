package com.estsoft.estsoft2ndproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.estsoft.estsoft2ndproject.service.UserService;

@Configuration
@EnableWebSecurity
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
			)
			.logout(logout -> logout
				.logoutUrl("/member/logout")
				.logoutSuccessUrl("/")
				.invalidateHttpSession(true)
				.deleteCookies("JSESSIONID")
			)
			.csrf(custom -> custom.disable())
			.build();

	}
}
