package org.jacp.spring.services;

import org.springframework.stereotype.Service;

@Service("simpleSpringBean")
/**
 * This is a simple Spring service to show JacpFX - Spring integration. 
 * @author <a href="mailto:amo.ahcp@gmail.com"> Andy Moncsek</a>
 *
 */
public class SimpleSpringBean {
	public String sayHello() {
		return "hello";
	}
}
