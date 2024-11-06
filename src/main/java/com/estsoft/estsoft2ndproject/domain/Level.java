package com.estsoft.estsoft2ndproject.domain;

import lombok.Getter;

@Getter
public enum Level {
	LV1("씨앗"),
	LV2("새싹"),
	LV3("묘목"),
	LV4("성목"),
	LV5("고목"),
	ADMIN("관리자");

	private final String koreanName;

	Level(String koreanName) {
		this.koreanName = koreanName;
	}
}
