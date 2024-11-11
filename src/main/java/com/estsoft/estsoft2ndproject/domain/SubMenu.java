package com.estsoft.estsoft2ndproject.domain;

import java.util.List;

import lombok.Getter;

@Getter
public class SubMenu {
	private String title;
	private List<String> items;
	private String url;
	private List<String> itemUrls;

	public SubMenu(String title, List<String> items, String url, List<String> itemUrls) {
		this.title = title;
		this.items = items;
		this.url = url;
		this.itemUrls = itemUrls;
	}

	@Override
	public String toString() {
		return "SubMenu{" +
			"title='" + title + '\'' +
			", items=" + items +
			", url='" + url + '\'' +
			", itemUrls=" + itemUrls +
			'}';
	}
}
