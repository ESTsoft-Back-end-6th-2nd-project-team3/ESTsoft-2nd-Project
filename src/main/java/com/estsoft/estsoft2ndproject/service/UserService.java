package com.estsoft.estsoft2ndproject.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.estsoft.estsoft2ndproject.custonException.AdditionalInformationRequireException;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final HttpSession session;

	public UserService(UserRepository userRepository, HttpServletRequest request, HttpSession session, HttpServletResponse response) {
		this.userRepository = userRepository;
		this.request = request;
		this.session = session;
		this.response = response;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);

		List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("관리자");

		String usernameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();

		// DB 저장 로직
		String userAgent = request.getHeader("User-Agent");
		Map<String, Object> kakaoAccount = (Map<String, Object>)oAuth2User.getAttributes().get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");

		User userEntity = userRepository.findByPii(oAuth2User.getName());
		if (userEntity == null) {
			String email = kakaoAccount.get("email").toString();
			String nickname = profile.get("nickname").toString();
			String profileImageUrl = profile.get("profile_image_url").toString();

			if (!Objects.equals(session.getAttribute("isComplete"), "true")) {
				log.error("추가 정보가 필요합니다.");
				session.setAttribute("email", email);
				session.setAttribute("nickname", nickname);
				session.setAttribute("profileImageUrl", profileImageUrl);

				throw new AdditionalInformationRequireException("추가 정보가 필요합니다.");

				/*try {
					response.sendRedirect("/member/register");
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
				return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), usernameAttributeName);*/
			}

			userEntity = User.builder()
				.email(email)
				.nickname(nickname)
				.pii(oAuth2User.getName())
				.level(authorities.get(0).getAuthority())
				.userAgent(userAgent)
				.profileImageUrl(profileImageUrl)
				.build();

			session.removeAttribute("isComplete");
			session.removeAttribute("email");
			session.removeAttribute("nickname");
			session.removeAttribute("profileImageUrl");
			userRepository.save(userEntity);
		}

		return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), usernameAttributeName);
	}
}
