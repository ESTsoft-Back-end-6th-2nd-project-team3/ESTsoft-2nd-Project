package com.estsoft.estsoft2ndproject.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.estsoft.estsoft2ndproject.custonException.AdditionalInformationRequireException;
import com.estsoft.estsoft2ndproject.domain.User;
import com.estsoft.estsoft2ndproject.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService extends DefaultOAuth2UserService {
	private final UserRepository userRepository;
	private final HttpServletRequest request;

	public UserService(UserRepository userRepository, HttpServletRequest request) {
		this.userRepository = userRepository;
		this.request = request;
	}

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		OAuth2User oAuth2User = super.loadUser(userRequest);
		HttpSession session = request.getSession();

		String accessToken = userRequest.getAccessToken().getTokenValue();
		session.setAttribute("accessToken", accessToken);

		List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("관리자");

		String usernameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();

		// DB 저장 로직
		String userAgent = request.getHeader("User-Agent");
		Map<String, Object> kakaoAccount = (Map<String, Object>)oAuth2User.getAttributes().get("kakao_account");
		Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");

		String email = kakaoAccount.get("email").toString();
		String nickname = profile.get("nickname").toString();
		String profileImageUrl = profile.get("profile_image_url").toString();

		User userEntity = userRepository.findByPii(oAuth2User.getName());
		if (userEntity == null) {
			if (!Objects.equals(session.getAttribute("isComplete"), "true")) {
				log.error("추가 정보가 필요합니다.");
				session.setAttribute("email", email);
				session.setAttribute("nickname", nickname);
				session.setAttribute("profileImageUrl", profileImageUrl);

				throw new AdditionalInformationRequireException("추가 정보가 필요합니다.");
			}
			nickname = session.getAttribute("nickname").toString();
			profileImageUrl = session.getAttribute("profileImageUrl").toString();
			String selfIntro = session.getAttribute("selfIntro").toString();
			String snsLink = session.getAttribute("snsLink").toString();

			userEntity = User.builder()
				.email(email)
				.nickname(nickname)
				.pii(oAuth2User.getName())
				.level(authorities.get(0).getAuthority())
				.userAgent(userAgent)
				.profileImageUrl(profileImageUrl)
				.selfIntro(selfIntro)
				.snsLink(snsLink)
				.build();

			session.removeAttribute("isComplete");
			session.removeAttribute("email");
			session.removeAttribute("nickname");
			session.removeAttribute("profileImageUrl");
			session.removeAttribute("selfIntro");
			session.removeAttribute("snsLink");
		}

		userEntity.updateBuilder()
			.lastLogin(new Timestamp(System.currentTimeMillis()))
			.build();

		userRepository.save(userEntity);

		return new DefaultOAuth2User(authorities, oAuth2User.getAttributes(), usernameAttributeName);
	}

	public void logoutFromKakao(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + accessToken);

		HttpEntity<String> request = new HttpEntity<>(headers);
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.postForEntity("https://kapi.kakao.com/v1/user/logout", request, String.class);
		} catch (Exception e) {
			log.error("카카오 로그아웃 실패");
		}
	}
}
