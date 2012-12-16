/************************************************************************
 * 
 * Copyright (C) 2010 - 2012
 *
 * [StatelessCallback.java]
 * AHCP Project (http://jacp.googlecode.com)
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either 
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 *
 *
 ************************************************************************/
package org.jacp.callbacks;

import java.util.logging.Logger;

import javafx.event.Event;

import org.jacp.api.action.IAction;
import org.jacp.api.annotations.CallbackComponent;
import org.jacp.api.annotations.OnStart;
import org.jacp.api.annotations.OnTearDown;
import org.jacp.javafx.rcp.component.AStatelessCallbackComponent;
import org.jacp.spring.services.SimpleSpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * A stateless JacpFX component.
 * 
 * @author <a href="mailto:amo.ahcp@gmail.com"> Andy Moncsek</a>
 * 
 */
@CallbackComponent(id = "id004", name = "statelessCallback", active = false)
public class StatelessCallback extends AStatelessCallbackComponent {
	private final Logger log = Logger.getLogger(StatelessCallback.class
			.getName());
	@Autowired
	@Qualifier(value="simpleSpringBean")
	private SimpleSpringBean simpleSpringBean;
	
	@Override
	public Object handleAction(final IAction<Event, Object> arg0) {
		this.log.info(arg0.getLastMessage().toString());
		//System.out.println("StatelessCallback started: "+this.isStarted()+" active: "+ this.isActive()+"   : "+this);
		if(arg0.getLastMessage().toString().contains("stop")) {
			this.setActive(false);
			System.out.println("SET force shutdown : "+ this );
		}
		return "StatelessCallback - "+simpleSpringBean.sayHello();
	}
	
	
	@OnStart
	public void init() {
		System.out.println("StatelessCallback init was called:"+this);
		this.log.info("call init method on start");
	}
	
	@OnTearDown
	public void teardown() {
		System.out.println("StatelessCallback @OnTearDown was called:"+this);
		this.log.info("call @OnTearDown method");
	}

}
