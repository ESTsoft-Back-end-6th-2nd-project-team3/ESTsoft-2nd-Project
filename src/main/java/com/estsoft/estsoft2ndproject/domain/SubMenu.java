package com.estsoft.estsoft2ndproject.domain;

import java.util.List;

public class SubMenu {
	private String title;
	private List<String> items;

	public SubMenu(String title, List<String> items) {
		this.title = title;
		this.items = items;
	}

	public String getTitle() {
		return title;
	}

	public List<String> getItems() {
		return items;
	}

	@Override
	public String toString() {
		return "SubMenu{" +
			"title='" + title + '\'' +
			", items=" + items +
			'}';
	}
}
