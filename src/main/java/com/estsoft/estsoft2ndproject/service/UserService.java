package com.estsoft.estsoft2ndproject.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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
	@Transactional
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
		if (userEntity == null || !userEntity.getIsActive()) {
			if (!Objects.equals(session.getAttribute("isComplete"), "true")) {
				session.setAttribute("email", email);
				session.setAttribute("nickname", nickname);
				session.setAttribute("profileImageUrl", profileImageUrl);

				throw new AdditionalInformationRequireException("추가 정보가 필요합니다.");
			}
			nickname = session.getAttribute("nickname").toString();
			profileImageUrl = session.getAttribute("profileImageUrl").toString();
			String selfIntro = session.getAttribute("selfIntro").toString();
			String snsLink = session.getAttribute("snsLink").toString();

			if (userEntity == null) {
				userEntity = User.builder()
					.email(email)
					.nickname(nickname)
					.pii(oAuth2User.getName())
					.level(authorities.get(0).getAuthority())
					.profileImageUrl(profileImageUrl)
					.selfIntro(selfIntro)
					.snsLink(snsLink)
					.build();
			} else {
				userEntity.updateBuilder()
					.nickname(nickname)
					.isActive(true)
					.level(authorities.get(0).getAuthority())
					.loginCount(0)
					.profileImageUrl(profileImageUrl)
					.activityScore(0)
					.badgeImageData(null)
					.awardedTitle(null)
					.selfIntro(selfIntro)
					.snsLink(snsLink)
					.build();
			}

			session.removeAttribute("isComplete");
			session.removeAttribute("email");
			session.removeAttribute("nickname");
			session.removeAttribute("profileImageUrl");
			session.removeAttribute("selfIntro");
			session.removeAttribute("snsLink");
		}

		userEntity.updateBuilder()
			.lastLogin(new Timestamp(System.currentTimeMillis()))
			.loginCount(userEntity.getLoginCount() + 1)
			.userAgent(userAgent)
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
			log.error("Kakao logout error: {}", e.getMessage());
		}
	}

	@Transactional
	public void deleteUser(OAuth2User oAuth2User) {
		User userEntity = userRepository.findByPii(oAuth2User.getName());

		userEntity.updateBuilder()
			.isActive(false)
			.build();

		userRepository.save(userEntity);
	}

	public Long getUserId(OAuth2User oAuth2User) {
		return userRepository.findByPii(oAuth2User.getName()).getUserId();
	}

	public User getUser(OAuth2User oAuth2User) {
		return userRepository.findByPii(oAuth2User.getName());
	}

	public Optional<User> getUserById(Long userId) {
		return userRepository.findById(userId);
	}

	public Optional<User> getUserWithChallenges(Long userId) {
		return userRepository.findById(userId);
	}

	public void updateUser(User user) {
		userRepository.save(user);
	}

	public boolean isNicknameAvailable(String nickname) {
		return !userRepository.existsByNickname(nickname);
	}

	public String saveProfileImage(Long userId, MultipartFile file) throws IOException {
		String uploadDir = "uploads/profile-images/" + userId;
		String fileName = file.getOriginalFilename();
		Path filePath = Paths.get(uploadDir, fileName);

		Files.createDirectories(filePath.getParent()); // 디렉터리 생성
		file.transferTo(filePath.toFile()); // 파일 저장

		// 이미지 URL 생성 (예: http://localhost:8080/uploads/profile-images/{userId}/{fileName})
		return "/uploads/profile-images/" + userId + "/" + fileName;
	}

	public void updateUserProfile(Long userId, String nickname, String selfIntro, String snsLink,
		String profileImageUrl) {
		Optional<User> optionalUser = userRepository.findById(userId);

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setNickname(nickname);
			user.setSelfIntro(selfIntro);
			user.setSnsLink(snsLink);
			user.setProfileImageUrl(profileImageUrl);

			userRepository.save(user); // 업데이트된 사용자 정보 저장
		} else {
			throw new RuntimeException("User not found with ID: " + userId);
		}
	}

}
