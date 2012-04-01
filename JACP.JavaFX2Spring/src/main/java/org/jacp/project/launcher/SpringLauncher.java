package org.jacp.project.launcher;

import org.jacp.api.launcher.Launcher;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * The SpringLaucher class resolves the spring.xml file and handles access to
 * beans
 * 
 * @author Andy Moncsek
 * 
 */
public class SpringLauncher implements Launcher<ClassPathXmlApplicationContext> {
	private final ClassPathXmlApplicationContext context;
	private final ConfigurableListableBeanFactory factory;
	private final String BASIC_CONFIG_BEANS = "basic.xml";

	public SpringLauncher(final String resource) {
		this.context = new ClassPathXmlApplicationContext(new String[] {
				resource, this.BASIC_CONFIG_BEANS });
		this.factory = this.context.getBeanFactory();
	}

	@Override
	public ClassPathXmlApplicationContext getContext() {
		return this.context;
	}

	@Override
	public synchronized <E> E getBean(final Class<E> clazz) {
		return this.factory.getBean(clazz);
	}

}
