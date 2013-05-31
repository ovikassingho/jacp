package it.pkg.workbench;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import org.jacp.api.action.IAction;
import org.jacp.api.componentLayout.IWorkbenchLayout;
import org.jacp.api.util.ToolbarPosition;
import org.jacp.javafx2.rcp.componentLayout.FX2ComponentLayout;
import org.jacp.javafx2.rcp.components.optionPane.JACPDialogButton;
import org.jacp.javafx2.rcp.components.optionPane.JACPDialogUtil;
import org.jacp.javafx2.rcp.components.optionPane.JACPModalDialog;
import org.jacp.javafx2.rcp.components.optionPane.JACPOptionPane;
import org.jacp.javafx2.rcp.workbench.AFX2Workbench;

/**
 * A simple workbench, The workbench defines basic layout properties like
 * "workbench size" and defines a menu entry "help" with an JacpFX option pane.
 * 
 * @author Andy Moncsek
 * 
 */
public class Workbench extends AFX2Workbench {

	@Override
	public void handleInitialLayout(IAction<Event, Object> action,
			IWorkbenchLayout<Node> layout, Stage stage) {
		layout.setWorkbenchXYSize(1024, 768);
		layout.registerToolBar(ToolbarPosition.NORTH);
		layout.setStyle(StageStyle.DECORATED);
		layout.setMenuEnabled(true);

	}

	@Override
	public void postHandle(FX2ComponentLayout layout) {
		final MenuBar menu = layout.getMenu();
		final Menu menuFile = new Menu("File");
		final MenuItem itemHelp = new MenuItem("Help");
		itemHelp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				// show overlay dialog
				JACPOptionPane dialog = JACPDialogUtil.createOptionPane("Help",
						"Add some help text ");
				dialog.setDefaultButton(JACPDialogButton.NO);
				dialog.setDefaultCloseButtonOrientation(Pos.CENTER_RIGHT);
				dialog.setOnYesAction(new EventHandler<ActionEvent>() {

					@Override
					public void handle(ActionEvent arg0) {
						JACPModalDialog.getInstance().hideModalMessage();
					}
				});
				dialog.showDialog();

			}
		});
		menuFile.getItems().add(itemHelp);
		menu.getMenus().addAll(menuFile);
	}

}