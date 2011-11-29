package org.jacp.api.util;

import java.util.Comparator;

public class ToolBarComparator implements Comparator<ToolbarProperty> {

	@Override
	public int compare(ToolbarProperty o1, ToolbarProperty o2) {

		int o1Priority = o1.getPriority().getId();
		int o2Priority = o2.getPriority().getId();

		return (o1Priority == o2Priority) ? 1 : (o1Priority < o2Priority) ? 1
				: -1;
	}

}
