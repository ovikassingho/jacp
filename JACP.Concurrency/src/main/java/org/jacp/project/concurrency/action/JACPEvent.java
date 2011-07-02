/*
 * Copyright (C) 2010,2011.
 * AHCP Project
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
 */
package org.jacp.project.concurrency.action;

import java.util.EventObject;
/**
 * JACP Event Object
 * @author Andy Moncsek
 *
 */
public class JACPEvent extends EventObject{
	private int id;
	private String command;


	public JACPEvent(Object source) {
		super(source);
	}
	public JACPEvent(Object source,String command) {
		super(source);
		this.command = command;
	}
	public JACPEvent(Object source,String command,int id) {
		super(source);
		this.command = command;
		this.id = id;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int getId() {
		return id;
	}

	public String getCommand() {
		return command;
	}
}
