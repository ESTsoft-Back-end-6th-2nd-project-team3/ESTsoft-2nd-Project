package com.estsoft.estsoft2ndproject.domain;

public enum PostType {
	ANNOUNCEMENT("공지사항"),
	PARTICIPATION_CATEGORY("카테고리 참여"),
	GENERATION_CATEGORY("카테고리 생성"),
	PARTICIPATION_CHALLENGE("챌린지 참여"),
	GENERATION_CHALLENGE("챌린지 생성"),
	PARTICIPATION_REGION("지역 참여");

	private final String koreanName;

	PostType(String koreanName) {
		this.koreanName = koreanName;
	}

	public String getKoreanName() {
		return koreanName;
	}

	public static String getKoreanNameByString(String postType) {
		for (PostType type : PostType.values()) {
			if (type.name().equalsIgnoreCase(postType)) {
				return type.getKoreanName();
			}
		}
		return "알 수 없는 게시판";
	}
}
