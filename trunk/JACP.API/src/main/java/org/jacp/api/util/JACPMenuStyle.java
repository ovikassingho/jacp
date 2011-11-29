package org.jacp.api.util;

public enum JACPMenuStyle {

	DECORATED(1, "decorated"), UNDECORATED(2, "undecorated"), CUSTOM(3,
			"custom");

	private int id;

	private String description;

	private JACPMenuStyle(int id, String description) {

		this.id = id;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

}
