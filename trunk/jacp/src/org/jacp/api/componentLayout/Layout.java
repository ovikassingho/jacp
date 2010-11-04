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
package org.jacp.api.componentLayout;

/**
 * 
 * @author Andy Moncsek
 */
public enum Layout {

	NORTH("North"), SOUTH("South"), EAST("East"), WEST("West"), CENTER("Center"), LEFT(
			"left"), RIGHT("right"), TOP("top"), BOTTOM("bottom");

	private String layout;

	private Layout(final String layout) {
		this.layout = layout;
	}

	public String getLayout() {
		return layout;
	}

}
