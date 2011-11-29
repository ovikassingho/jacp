package org.jacp.api.util;

public enum ToolbarPosition {

	NORTH(1, "north"), WEST(2, "west"), SOUTH(3, "south"), EAST(4, "east");

	private int id;
	private String name;

	private ToolbarPosition(final int id, final String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
