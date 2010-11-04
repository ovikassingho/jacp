package org.jacp.swing.demo1.editors;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import org.jacp.api.action.IAction;
import org.jacp.api.action.IActionListener;
import org.jacp.swing.rcp.component.AStatelessComponent;
import org.springframework.jmx.export.annotation.ManagedResource;
@ManagedResource(objectName = "org.jacp:name=StatelessDemoConsumer", description = "a state ful swing component")
public class StatelessDemoConsumer extends AStatelessComponent{
    
    
    private TestBean testBean;

    @Override
    public Object handleAction(IAction<ActionEvent, Object> action) {
	if (action.getMessage() instanceof Integer) {

	
	    int j = 0;
	    while (j < 100000) {
		j++;
		Math.abs(Math.abs(Math.abs(Double.valueOf(j+"")*Math.PI)*Math.PI)*Math.PI);
		//System.out.println(j);

	    }
	    final IActionListener<ActionListener, ActionEvent, Object> listener = getActionListener();
	    listener.getAction().setMessage("id09", j);
	    listener.getListener().actionPerformed(
		    listener.getAction().getActionEvent());
	}
	System.out.println("Hello state less world : "+this + " "+testBean.getTest() );
	return null;
    }

    public TestBean getTestBean() {
        return testBean;
    }

    public void setTestBean(TestBean testBean) {
        this.testBean = testBean;
    }

}
