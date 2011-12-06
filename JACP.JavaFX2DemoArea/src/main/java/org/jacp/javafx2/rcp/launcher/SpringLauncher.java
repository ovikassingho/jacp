package org.jacp.javafx2.rcp.launcher;

import org.jacp.api.launcher.Launcher;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringLauncher  implements Launcher<ClassPathXmlApplicationContext> {
    private final ClassPathXmlApplicationContext context;

    public SpringLauncher(final String resource) {
	context = new ClassPathXmlApplicationContext(new String[] { resource });
    }

    @Override
    public ClassPathXmlApplicationContext getContext() {
	return context;
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized <E> E getBean(final Class<E> clazz) {
	final String[] name = context.getBeanNamesForType(clazz);
	return (E) context.getBean(name[0]);
    }

}