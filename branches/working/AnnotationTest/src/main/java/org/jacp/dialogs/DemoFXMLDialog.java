package org.jacp.dialogs;

import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import org.jacp.api.annotations.Dialog;
import org.jacp.api.annotations.Resource;
import org.jacp.api.dialog.Scope;
import org.jacp.components.ComponentTop;
import org.jacp.spring.services.SimpleSpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Dialog(id = "456", viewLocation = "/fxml/DialogTest.fxml", resourceBundleLocation = "bundles.languageBundle", localeID = "en_US", scope = Scope.PROTOTYPE)
public class DemoFXMLDialog {

	private @Resource
	ResourceBundle bundle;
	@Resource(parentId="id006")
	ComponentTop parent;
	
	@Autowired
	@Qualifier(value = "simpleSpringBean")
	private SimpleSpringBean simpleSpringBean;
	@FXML
	private Label myLabel;

	public void sayHallo() {
		System.out.println("hello");
		parent.getActionListener("hello from Dialog").performAction(null);
		myLabel.setText(simpleSpringBean.sayHello() + " from bean");
	}
}
