/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jacp.api.componentLayout;

/**
 * 
 * @author amo
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
