package org.jacp.swing.demo1.editors;

import java.awt.event.ActionEvent;


import org.jacp.api.action.IAction;
import org.jacp.swing.rcp.component.AStatelessComponent;
import org.springframework.jmx.export.annotation.ManagedResource;
@ManagedResource(objectName = "org.jacp:name=StatelessDemoConsumer", description = "a state ful swing component")
public class StatelessDemoConsumer extends AStatelessComponent{
    
    private String test;
    
    private TestBean testBean;

    @Override
    public Object handleAction(IAction<ActionEvent, Object> action) {
	System.out.println("Hello state less world : "+this + " "+testBean.getTest() );
	test="dfgfg";
	Long j = 0L;

	while (j < 100000000L) {
		j++;
		// System.out.print("test"+j+" ");

	}
	return null;
    }

    public TestBean getTestBean() {
        return testBean;
    }

    public void setTestBean(TestBean testBean) {
        this.testBean = testBean;
    }

}
