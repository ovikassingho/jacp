package org.jacp.api.util;

public enum ToolbarPriority {

	ONE(1), TWO(2), THREE(3), FOUR(4);

	private int id;

	private ToolbarPriority(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
