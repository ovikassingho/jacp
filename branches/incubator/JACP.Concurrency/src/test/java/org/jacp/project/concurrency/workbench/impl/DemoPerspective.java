package org.jacp.project.concurrency.workbench.impl;

import org.jacp.api.action.IAction;
import org.jacp.project.concurrency.action.Event;
import org.jacp.project.concurrency.workbench.AHeadlessPerspective;
/**
 * Unit test implementation of a perspective, perspectives ca be notified but not riunning in thread 
 * @author Andy Moncsek
 *
 */
public class DemoPerspective extends AHeadlessPerspective {

	@Override
	public void handlePerspective(IAction<Event, Object> action) {
		System.out.println("ACTION PERSPECTIVE1"+action);
		
	}

	

}
