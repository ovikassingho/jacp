/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jacp.api.util;

/**
 * helper class... TODO remove!!!
 * 
 * @author Andy Moncsek
 */
public class Tupel<X, Y> {

	X x;
	Y y;

	public void setX(final X x) {
		this.x = x;
	}

	public void setY(final Y y) {
		this.y = y;
	}

	public X getX() {
		return x;
	}

	public Y getY() {
		return y;
	}

}