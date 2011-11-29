package org.jacp.api.util;

public class ToolbarProperty implements Comparable<ToolbarProperty> {

	private ToolbarPosition position;

	private ToolbarPriority priority;

	int test;

	public ToolbarPriority getPriority() {
		return priority;
	}

	public void setPriority(ToolbarPriority priority) {
		this.priority = priority;
	}

	public ToolbarPosition getPosition() {
		return position;
	}

	public void setPosition(ToolbarPosition position) {
		this.position = position;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + position.getId();
		result = prime * result + priority.getId();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ToolbarProperty other = (ToolbarProperty) obj;
		if (getPosition().getId() != other.getPosition().getId())
			return false;
		if (getPriority().getId() != other.getPriority().getId())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ToolbarProperty [getPriority()=" + getPriority().getId()
				+ ", getPosition()=" + getPosition().getName() + "]";
	}

	@Override
	public int compareTo(ToolbarProperty o) {
		int o1Priority = o.getPriority().getId() * 10 + o.getPosition().getId();
		int o2Priority = this.getPriority().getId() * 10
				+ this.getPosition().getId();
		return (o1Priority == o2Priority) ? 0 : (o1Priority < o2Priority) ? 1
				: -1;
	}
}
