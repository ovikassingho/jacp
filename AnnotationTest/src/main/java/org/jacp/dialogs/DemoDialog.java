package org.jacp.dialogs;

import java.util.ResourceBundle;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import org.jacp.api.annotations.Dialog;
import org.jacp.api.annotations.Resource;
import org.jacp.components.ComponentTop;
import org.jacp.javafx.rcp.component.AFXComponent;

@Dialog(id = "123",resourceBundleLocation = "bundles.languageBundle")
public class DemoDialog extends HBox {
	
	private @Resource
	ResourceBundle bundle;
	@Resource//(parentId="id006")
	AFXComponent parent;
	
	public DemoDialog() {
		Label myLabel = new Label("hello");
		this.getChildren().add(myLabel);
	}
	
	public void sayHello() {
		System.out.println("sdfs");
	}
}
