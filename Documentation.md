# JacpFX  documentation #

The JacpFX Project is an API to create **Rich Clients** in **MVC** style with **JavaFX**, **Spring** (or other DI frameworks) and an Actor like component approach. It provides a simple API to create a workspace, perspectives and components; to communicate with all parts trough messages and to compose your Client application easily. All components triggered by **messages** and have a defined life cycle; a component `handle()` method is always executed in a **separate thread**, so developers should have less effort on threading topics nor be afraid of locking and unresponsive UI. The API uses dependency injection to initialize all parts of your client; the currently used DI implementation is Spring. You can inject spring beans to your components and use the complete Spring stack in your JACP Application.



# The Application Launcher #

The “Application Launcher” is the entry point where you define the spring xml and where the complete stack of Spring, JavaFX and JacpFX  is started. The `postInit(Stage stage)` method gives you the ability to make custom changes on the JavaFX Stage instance. You have to define a `main` method on your own and start the JavaFX context by using the default `JavaFX Application` class.

**Example Application Launcher**
```
public class ApplicationLauncher extends AFXSpringLauncher {
	public ApplicationLauncher() {
		// define the spring xml
		super("main.xml");
	}

	@Override
	public void postInit(Stage stage) {
               //add application icons
               stage.getIcons().add(new Image("images/icons/JACP_512_512.png"));
	       // add style sheet
	       scene.getStylesheets().addAll(
				ApplicationLauncher.class.getResource("/styles/style.css")
						.toExternalForm());
	}
 
	public static void main(String[] args) {
	    // start the JavaFX2 context
		Application.launch(args);

	}

}
```

The structure of a JacpFX application looks like this:
<img src='http://jacp.googlecode.com/svn/wiki/JACP_Overview_v1.png' />

Below you find an example xml of a simple JacpFX project:

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">

	<bean id="workbench" class="org.jacp.javafx.rcp.workbench.Workbench">
		<property name="perspectives">
			<list>
				<ref bean="perspectiveOne" />
			</list>
		</property>
	</bean>

	<bean id="perspectiveOne" class="org.jacp.javafx.rcp.perspectives.PerspectiveOne">
		<property name="subcomponents">
			<list>
				<ref bean="demoFXComponentOne" />
				<ref bean="demoFXMLComponentOne" />
			</list>
		</property>
	</bean>

	<bean id="demoFXComponentOne" class="org.jacp.javafx.rcp.components.DemoFXComponentOne"/>

	<bean id="demoFXMLComponentOne" class="org.jacp.javafx.rcp.components.DemoFXMLComponentOne"/>


</beans>
```

All meta-data configurations like **`"id"`, `"name"`,`"executionTarget"`** and so on are defined by **annotations** directly in the component (see annotations overview).

# Workbench #
The workbench is the root node of your client project, providing simple interfaces to configure the basic behavior of your client. Besides the application launcher, it is the only component where you can get direct access to the JavaFX “Stage”. Furthermore a workbench logically groups all perspectives defined in the spring - xml (one-to-many relation).  He consists of two methods you have to implement, the **`handleInitialLayout(IAction<Event, Object> action, IWorkbenchLayout<Node> layout, Stage stage)`** and **`postHandle(FXComponentLayout layout)`**. The `action`-object in handleInitialLayout will always contain the default initialization message `“init”`, this message is default for all components on application start up. The Stage reference will give you the reference to the JavaFX Stage, here you can place some custom settings for your application. The  **`IWorkbenchLayout<Node> layout`** reference is your entry to configure following thinks on your application:
  * **`WorkbenchXYSize(x,y):`** defines the initial workbench size
  * **`MenuEnabled(boolean):`** enable the main menu if needed, the entries are defined later
  * **`Style(StageStyle):`** here you define the default `StageStyle` (decorated/undecorated), on OSX only decorated is available
  * **`registerToolBar(ToolbarPosition):`** enable tool bars in NORTH,SOUTH, EAST, WEST; those registered `ToolBars` are accessible in all perspective and components, but you are free to define custom toolbars in your component which are not accessible for all others.

The **`postHandle(FXComponentLayout layout)`** method is executed when “handleInitialLayout” execution is finished and gives you access to the initialized menu and tool bars (if you defined them).
  * **`JACPMenuBar menu = layout.getMenu()`** will give you the reference to the main menu instance
  * **`JACPToolBar toolbarNorth = layout.getRegisteredToolBar(ToolbarPosition.NORTH)`** returns the reference to the “north” `ToolBar` if defined in “handleInitialLayout” before

A workbench can handle many perspectives, but only one can be displayed at once (window mode is planned in later versions). The current active perspective is always the one where the last message appear (or a component of this perspective). All other running perspectives are hidden in background. A perspective provides the typical layout of your application, **not** the workbench. You can consider the workbench as a container for perspectives (micro applications) who coordinates all children and gives you the ability to define some basic settings like declaring toolbars and a menu and initial application size.

**Example workbench**:
```
public class Workbench extends AFXWorkbench {

	@Override
	public void handleInitialLayout(final IAction<Event, Object> action,
			final IWorkbenchLayout<Node> layout, final Stage stage) {
		layout.setWorkbenchXYSize(1024, 768);
		layout.registerToolBar(ToolbarPosition.NORTH);
		layout.setStyle(StageStyle.DECORATED);
		layout.setMenuEnabled(true);
	}

	@Override
	public void postHandle(FXComponentLayout layout) {
                final JACPMenuBar menu = layout.getMenu();
		final JACPToolBar topBar = layout
				.getRegisteredToolBar(ToolbarPosition.NORTH);
		Button close = new Button("close");
		topBar.getItems().add(close);
		final Menu menuFile = new Menu("File");
                menu.getMenus().addAll(menuFile);
	}

}
```


# Perspective #
A perspective is a (UI-) container like all other components (it’s children) and behave quite similar to components. It has a handle (handlePerspective) method and can be triggered by messages from other perspectives and components. Different to components is, that a perspective will **always run on `FX Application thread`**, so this is NOT the place where you put some complex code or long running tasks. The **real task** of a perspective is to define the **layout** of your current view. The ability to trigger perspectives by messages gives you the freedom to change the layout at runtime and to adopt the view for your current needs.<br />
A typical UI application has a root node and a large tree of components which represents your UI structure. The leaf nodes of such a component tree are your user-nodes containing `Buttons`, `TextFields` and so on.
The root node in JacpFX is provided by the workbench; a perspective represents your (complex) UI tree and defines place-holders for your components (see picture 1). So, for a typical business application you can create a `SplitPane` in your perspective, which is the root node (or the root of this subtree), place a `GridPane` on the left and on the right and register those `GridPanes` as “Targets” for your components.

<img src='http://jacp.googlecode.com/svn/wiki/JACP_NodeTree_View.png' />

This means, that you define the basic layout of your current view on perspective layer, but the `Buttons`, `TextFields` and so on are implemented in your components later. The **UI subtree** created here can be **as complex as you want** but you will always have to register the **root** and the **leaf (targets)** of this complex UI. The ability to register the root and the leafs is provided by the `IPerspectiveLayout` interface, the **`handlePerspective`** method gives you a reference to the perspective specific instance of this interface.
  * `layout.registerRootComponent(node):` register the root node of your perspective UI
  * `layout.registerTargetLayoutComponent("top", nodeTop)` register a leaf component (a target where your components can register at)

> You can register as many targets as you want, they are just a place-holder were an UI subtree of your components can be linked in. Like all other components a perspective has a defined life cycle. JacpFX 1.1 introduces FXML capabilities, so you can use FXML to define the perspective UI. The process of target registration did not changed and will be shown in an example below.

## The perspective life cycle ##
When a perspective is activated the method annotated with `“@OnStart”` is executed, followed by `“handlePerspective”` and when the perspective is disabled the method annotated with `“@OnTearDown”` is executed.
The purpose of `“@OnStart”` annotation is, to allow initialization code and to add Menu and `ToolBar` entries to the Client, while in the `“@OnTearDown”` method you can do clean up work.

<img src='http://jacp.googlecode.com/svn/wiki/JACP_Perspective_Lifecycle.png' />

## Perspective types ##
Since JacpFX 1.1 you can define declarative (with FXML) and programmatic (plain Java/JavaFX) Perspectives. For both types you create a Perspective class (**`AFXPerspective`**) and declare the targets inside. The type of a Perspective is controlled by annotations.

### Programmatic Perspectives ###
This type is defined by the `@Perspective` annotation with following attributes:
  * `id (mandatory)`, the Perspective id
  * `name (mandatory)`, the Perspective name
  * `resourceBundleLocation`, the bundle location for localization
  * `localeID`, the default locale


**Example Perspective**
```
@Perspective(id = "id001", name = "perspectiveOne", resourceBundleLocation = "bundles.languageBundle", localeID = "en_US")
public class PerspectiveOne extends AFXPerspective {
 
        @Override
        public void handlePerspective(IAction<Event, Object> action,
                        PerspectiveLayout perspectiveLayout) {
			// declare the root
			SplitPane root = new SplitPane();
		
			// declare the targets
			ScrollPane scrollPaneLeft = new ScrollPane();
			ScrollPane scrollPaneRight = new ScrollPane();
		
			root.getItems().addAll(scrollPaneLeft, scrollPaneRight);
			
			// register the root
			perspectiveLayout.registerRootComponent(root);
		
			// register the targets
			perspectiveLayout.registerTargetLayoutComponent("leftTarget", scrollPaneLeft);
			perspectiveLayout.registerTargetLayoutComponent("rightTarget", scrollPaneRight);

		} 

        @OnStart
	public void onStartPerspective(final FXComponentLayout layout,
			final ResourceBundle resourceBundle) {
		// define toolbars and menu entries
	}

	@OnTearDown
	public void onTearDownPerspective(final FXComponentLayout arg0) {
		// remove toolbars and menu entries when close perspective

	}

}

```

### Declarative Perspectives ###
This type is also defined by the `@Perspective` annotation and extends the default perspective annotation attributes with:
  * **viewLocation**, the URI to the FXML file



**Example (Declarative) Perspective**
```
@Perspective(id = "id02", name = "perspectiveOne", viewLocation = "/fxml/perspectiveOne.fxml", 
	resourceBundleLocation = "bundles.languageBundle", localeID = "en_US")
public class PerspectiveOne extends AFXPerspective { 
        @FXML
	private GridPane gridPaneLeft;
	@FXML
	private GridPane gridPaneRight;

	@Override
	public void handlePerspective(final IAction<Event, Object> action,
			final PerspectiveLayout perspectiveLayout) {
		if (action.getLastMessage().equals(MessageUtil.INIT)) {
			// register left panel
			perspectiveLayout.registerTargetLayoutComponent("Pleft",
					this.gridPaneLeft);
			// register main panel
			perspectiveLayout.registerTargetLayoutComponent("PMain",
					this.gridPaneRight);
		}

	}

	@OnStart
	public void onStartPerspective(final FXComponentLayout layout,
			final ResourceBundle resourceBundle) {
		// define toolbars and menu entries
	}

	@OnTearDown
	public void onTearDownPerspective(final FXComponentLayout arg0) {
		// remove toolbars and menu entries when close perspective

	}

}

```

**Example FXML**
```
<?xml version="1.0" encoding="UTF-8"?>

<?import java.lang.*?>
<?import java.util.*?>
<?import javafx.scene.control.*?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.paint.*?>

<BorderPane id="BorderPane" xmlns:fx="http://javafx.com/fxml">
  <center>
    <SplitPane id="splitPaneHorizontal1" cache="true" dividerPositions="0.20" minHeight="100.0" minWidth="200.0" styleClass="hsplitpane">
      <items>
        <GridPane fx:id="gridPaneLeft">
          <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0" />
          </columnConstraints>
          <rowConstraints>
            <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES" />
          </rowConstraints>
        </GridPane>
          <GridPane fx:id="gridPaneRight">
	      <columnConstraints>
	          <ColumnConstraints hgrow="SOMETIMES" minWidth="10.0" prefWidth="100.0" />
	      </columnConstraints>
	      <rowConstraints>
	        <RowConstraints minHeight="10.0" prefHeight="30.0" vgrow="SOMETIMES" />
	      </rowConstraints>
	  </GridPane>
      </items>
    </SplitPane>
  </center>
</BorderPane>

```



# Components #
Components are basically distinguished in UI- and NonUI-Components; UIComponents are the place where you put all the complex UI code (`TextFields`, `Buttons`, etc.) in. NonUIComponents are services for long running tasks. All components have an input and an output. The input is always an `Action` and the output is either a view (in case you use an UIcomponent) or any type of object (in case of services). Currently you can use four types of components: `AFXComponent` are UI components, they can either be plain `JavaFX` components or FXML components (depends on annotation ). `AStatefulCallbackComponent and AStatelessCallbackComponent` are NonUIcomponents. All components in common is, that they have a `“handle”` method that is **running outside the FX application thread**, so the execution of this method will not block the rest of your UI.

## `AFXComponent` ##
This type of component is used to create UI parts in `JavaFX` or FXML (similar to views or editors in other RCP frameworks). While you created your basic layout on perspective level, `AFXComponent`´s are used to fill your UI with elements and components like `Forms`, `Tables` and so on. **`AFXComponent`´s** are **controller** classes. While `JavaFX` components returning a **View-element** (Node) to the parent perspective, the XML-document of a FXML component is representing the view. The created **View-element** (`JavaFX` or FXML) will be integrated at a defined target of your perspective.
The interface of AFXComponent defines following two methods to implement:
  * **`Node handleAction(IAction<Event, Object> action):`** Handle any complex operations like DB access, service calls or create new UI components. Unless you are not modify existing UI elements, you are free to create any new UI-components. This method will be executed in a separate Thread **(Worker Thread)** and will not block the FX application thread from execution. You can use the handle method to create initially a large and complex UI tree, but you should avoid modifications of existing trees (it will throw an `UnsupportedOperationException` exception). You are also free to return a null value and to create the View-element in the postHandle method. On component start-up the action contains an "init" message like all other components, later the "action" parameter contains the specific message to this component.
  * **`Node postHandleAction(Node node, IAction<Event, Object> action):`** This method is the place where you create and **modify** the UI View-element. The Node parameter is the object you created and passed in your **handle** method before. In case of `JavaFX` components the return value (a Node element) will be submitted to the defined target in perspective. In case of FXML components you should not return any Node (in will throw an `UnsupportedOperationException`), here the associated FXML document is the Node that is passed to the target in corresponding perspective.

Some methods defined by the interface are used to modify the control flow of your component:
  * **`setActive(boolean active):`** active / disable the current component. You can modify the value in the `handle` and `postHandle` method
  * **`setExecutionTarget(String targetId):`** modify the `executionTarget` of your component. You can change the target at runtime, the notation is "perspectiveId.targetId".

**Method level annotations**
  * **`@OnStart:`** methods annotated with `OnStart` will be executed on activation. This can either be on application startup or when the component is re-/activated. The method signature can have no parameters, the `FXComponentLayout layout` parameter and/or the reference to the `ResourceBundle resourceBundle`. With the `FXComponentLayout layout` reference you can define `Menu-` and `ToolBar-entries` in your component.
  * **`@OnTearDown:`** methods annotated with `OnTearDown` will be executed on application shutdown or when the component is deactivated. It has the same method signature like `OnStart`.

### The `@Component` class level annotation ###
`@Component` contains all meta data related to the component
  * `defaultExecutionTarget (mandatory)`: defines an execution target provided by the perspective
  * `id (mandatory)`: the component id
  * `name (mandatory)`: the component name
  * `active`: the default active state on application startup, the default is "true"; when a component is set to false it will not be activated by the applications "init" message but by any other message
  * `resourceBundleLocation`: the name of your resource bundle
  * `localeID`: the default localeId (`http://www.oracle.com/technetwork/java/javase/locales-137662.html`), if not set the system default will be used

### The `@DeclarativeComponent` class level annotation ###
Declarative components are also `AFXComponents` but annotated with `@DeclarativeComponent`. This annotation has one additional attribute compared to `@Component`:
  * `viewLocation (mandatory)`: defines the location of the FXML document describing the components layout
Declarative components using the same interface like programmatic (JavaFX) components and behave quite similar with one exception: You `postHandleAction` method should not return any "Node", because the FXML document referenced in the `viewLocation` attribute is already defining the components view.


### `Example Programmatic component` ###
```
@Component(defaultExecutionTarget = "Pleft", id = "id001", name = "componentOne", 
	active = true, resourceBundleLocation = "bundles.languageBundle", localeID = "en_US")
public class DemoFXComponentOne extends AFXComponent {

	private GridPane grid;
	private Text category;
	private Button button;
	private MenuItem item;

	@Override
	public Node handleAction(IAction<Event, Object> action) {		
		if (action.getLastMessage().equals(MessageUtil.INIT)) {
		    // create UI Nodes on component start
			this.grid = new GridPane();
			this.grid.setHgap(10);
			this.grid.setVgap(10);
			this.grid.setPadding(new Insets(0, 0, 0, 10));
			this.category = new Text("Hello:");
			this.category.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
			this.grid.add(category, 1, 0); 
			return this.grid;
		}
		return null;
	}	

	
	@Override
	public Node postHandleAction(Node node, IAction<Event, Object> action) {
		if (!action.getLastMessage().equals(MessageUtil.INIT)) {
			// update UI tree on message
			this.category = new Text("World:");
			this.category.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
		}
		return this.grid;
	}
	
	@OnStart
	public void onStart(final FXComponentLayout layout,
			final ResourceBundle resourceBundle) {	
		JACPToolBar north = layout.getRegisteredToolBar(ToolbarPosition.SOUTH);
		JACPMenuBar menu = layout.getMenu();
		// create button in tool bar
		button= new Button("Button 1");
		north.getItems().add(button);	

		/// add menu entries
	}
	
	@OnTearDown
	public void onTearDownComponent(final FXComponentLayout layout) {
		JACPToolBar north = layout.getRegisteredToolBar(ToolbarPosition.SOUTH);
		JACPMenuBar menu = layout.getMenu();
		
		//remove button from tool bar
		north.getItems().remove(button);
		
		/// remove menu entries
	}
}
```


### `Example Declarative component` ###
```
@DeclarativeComponent(defaultExecutionTarget = "PBottom", id = "id005", name = "componentBottom", active = true, 
	viewLocation = "/fxml/DemoFXMLComponentOneFXML.fxml", resourceBundleLocation = "bundles.languageBundle", localeID = "en_US")
public class DemoFXMLComponentOne extends AFXComponent {
	@FXML
	private TextField textField;

	@Override
	public Node handleAction(final IAction<Event, Object> action) {
		// runs in worker thread
		return null;
	}

	@Override
	public Node postHandleAction(final Node arg0,
			final IAction<Event, Object> action) {
		// runs in FX application thread
		if (!action.getLastMessage().equals(MessageUtil.INIT)) {
			this.textField.setText(action.getLastMessage().toString());
		}
		return null;
	}

	@OnStart
	public void onStartComponent(final FXComponentLayout arg0,
			final ResourceBundle resourceBundle) {	}

	@OnTearDown
	public void onTearDownComponent(final FXComponentLayout arg0) {	}

	@FXML
	private void handleSend(final ActionEvent event) {
		this.getActionListener("id01.id004", "hello component")
				.performAction(event);
	}

}

```

**The FXML view**
```
<?xml version="1.0" encoding="UTF-8"?>

<?import java.lang.*?>
<?import java.net.*?>
<?import java.util.*?>
<?import javafx.scene.control.*?>
<?import javafx.scene.image.*?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.paint.*?>
<?import javafx.scene.shape.*?>
<?import javafx.scene.text.*?>

<BorderPane id="BorderPane" xmlns:fx="http://javafx.com/fxml" >
  <center>
    <AnchorPane fx:id="details">
      <children>
        <Button id="button1" fx:id="sendButton" alignment="BOTTOM_LEFT" layoutY="120" onAction="#handleSend" text="%send"  />
        <Label id="Label" styleClass="propLabel" text="%fxmlCompBottom" />
        <TextField id="synopsis" fx:id="textField"  styleClass="propTextField" text="" />
      </children>
    </AnchorPane>
  </center>
</BorderPane>
```



### **`AFXComponent` life cycle** ###
<img src='http://jacp.googlecode.com/svn/wiki/JACP_UI-Component_Lifecycle.png' />

## `AStatefulCallbackComponent` ##
An `AStatefulCallbackComponent` is a stateful non UI component. In terms of JEE it is more an singleton component, because only one instance of each `AStatefulCallbackComponent` exists at runtime. While JEE singletons must be synchronized (Container- or Bean- managed concurrency), JacpFX components never accessed directly (only trough messages) and must not be synchronized. The container queues all messages and is aware of correct message delivering (similar to a MDB running on one thread). Like all JacpFX components it has a `handle` method that is executed in a separate Thread **(Worker Thread)**. Use this type of component to handle long running tasks or service calls and when you need a **conversational state**. The result of your task will be send to the message caller by default. This type of component has one method you have to implement:
  * **`Object handleAction(IAction<Event, Object> action)`**: The input parameter "action" contains the specific message to this component. You can modify the state of your component by checking the content of your action message. The output (return value) of an `AStatefulCallbackComponent` can be any kind of object that will be delivered back to the calling component (request/response semantic). You are free to redirect the return value to any component you want.

Some methods defined by the interface modify the control flow of your component:
  * **`setActive(boolean active):`** active / disable the current component. You can modify the value in the `handle` and `postHandle` method
  * **`setExecutionTarget(String targetId):`** modify the `executionTarget` of your component. You can change the target at runtime, the notation is "perspectiveId". The `executionTarget` in this context means "the runtime perspective". You can move the component to any perspective you want. Moving a component to a specific perspective can speed up the message calls in one perspective context. Locale message calls are executed faster.
  * **`setHandleTarget(String targetId):`**  modify the target of the return value; by default the return value is always send to the caller component but you can "redirect" it to any component you want.

**The `@CallbackComponent` class level annotation**

The `@CallbackComponent` defines the components meta-data:
  * `id (mandatory)`: the component id
  * `name (mandatory)`: the component name
  * `active`: the component active state on startup (default is true)

**Example `AStatefulCallbackComponent`**
```
@CallbackComponent(id = "id003", name = "statefulCallback", active = false)
public class StatefulCallback extends AStatefulCallbackComponent{
	private int c = 0;
	@Override
	public Object handleAction(IAction<Event, Object> action) {
		c = c++;
		if(action.getLastMessage().equals("ping")) return "pong";
		return "ping";
	}

}
```

### **`AStatefulCallbackComponent` life cycle** ###

<img src='http://jacp.googlecode.com/svn/wiki/JACP_Stateful-Component.png' />

## `AStatelessCallbackComponent` ##
An `AStatelessCallbackComponent` is a stateless non UI component.  Like all JacpFX components it has a `handle` method that is executed in a separate Thread **( Worker Thread)**. Similar to an `AStatefulCallbackComponent` it is a callback component, but in contrast it can have more than one running instance (**no conversational state**). The purpose of this component is to scale workload on different instances and threads. When many messages are send to an `AStatelessCallbackComponent` the scheduler creates a defined amount (based on CPU count) of instances and threads to work off all the messages in parallel. The result will be send to message caller exactly like an `AStatefulCallbackComponent` (request/response).

This component type has the same method signature like an `AStatefulCallbackComponent` also the `@CallbackComponent` annotation is the same.

**Example `AStatelessCallbackComponent`**

```
@CallbackComponent(id = "id004", name = "statelessCallback", active = false)
public class StatelessCallback extends AStatelessCallbackComponent{
	@Override
	public Object handleAction(IAction<Event, Object> action) {
		if(action.getLastMessage() instanceof File){
			File folder = (File) action.getLastMessage();
			if(folder.isDirectory()) {
				for(String file: folder.list()){
					System.out.println(file);
				}
			}
		}
		return " ";
	}

}
```

### **`AStatelessCallbackComponents` life cycle** ###
<img src='http://jacp.googlecode.com/svn/wiki/JACP_Stateless-Component.png' />

# JacpFX Messaging #
The JacpFX Framework is all about messaging. While you are free to use the default `JavaFX EventHandler` concepts in your `View/UI`, you can use the JacpFX listeners to trigger messages to other JacpFX components or to the calling component itself.
Messaging is the basic concept to change the state of an JacpFX component (and perspective too) and to exchange data between components. When a component receives a message it will execute the `handle(...) method`, which is executed in a separate thread and will not block the **`FX application thread`**.
The JacpFX listener concept fits well to the default `JavaFX EventHandler` (a JacpFX listener implements an `JavaFX  EventHandler`) so you can add an JacpFX listener to any JavaFX Node to handle it's mouse-, keyboard- and other events. An JacpFX listener encapsulates the `JavaFX ` source-event, the message and the target component-id in an `Action` object. This atomic `Action` will be send to the receiver and can be handled by message-content or event-type in the target-component `handle(...) method`. Each JacpFX Component has it's own message queue (postbox) containing all messages to this component, the scheduler providing the messages to the component handle method.
<img src='http://jacp.googlecode.com/svn/wiki/JACP_ComponentMessage_View.png' />

The `getActionListener` interface defines two methods:

## **JacpFX local messages** ##
`getActionListener(Object)` fires a **local messages** where the calling component is also the target component.
```
Button button = new Button("click me");
button.setOnMouseClicked(getActionListener("hello").getListener());
```

## **JacpFX global messages** ##
The `getActionListener(string, object)` providing an interface to send **global messages**, the method has a (string) targetId and an object representing a message. The targetId signature is: `perspectiveId.componentId` or simply `componentId` when the target is in the same perspective.
```
Button button = new Button("click me");
button.setOnMouseClicked(getActionListener("id01.id001", "hello").getListener());
```

## **Fire message event manually** ##
You can also fire messages directly from your component. Simply call the `performAction(null)` method and the message will be send.
```
getActionListener("id01.id001", "hello").performAction(null); // or any event instead of null
```

## **The message address scheme** ##

The address scheme is pretty simple, it is based on the Java package naming convention.
Components are always located in a perspective so "id01" in the current example is the id of a perspective and "id001" the id of the target component. So by typing "id01.id001" you address the component "id001" in perspective "id01".
You can also use simple naming like "id001" (without dots), this would mean that the target is either a perspective or a component inside the current perspective. The framework will try to resolve the correct target for you.

# Modal Dialogs #

## JacpFX `OptionPane` ##

JacpFX provides a default optionpane, which can be displayed as a modal dialog.

The optionpane holds
**a title**a message
**four buttons (Ok, Canel, Yes, No)**closeButton

To create an optionpane, you can use the **`createOptionPane`** method of the **`JACPDialogUtil`**.
While the title and the message will be taken as parameter of the create method, you can set the Eventhandlers for all four buttons individually. Every button has its own “setOnAction” method.

  * `setOnOkAction(final EventHandler<ActionEvent> onOk)`
  * `setOnCancelAction(final EventHandler<ActionEvent> onCancel)`
  * `setOnYesAction(final EventHandler<ActionEvent> onYes)`
  * `setOnNoAction(final EventHandler<ActionEvent> onNo)`

By calling one of those four, the corresponding button will be added to the pane. There is no other way to add buttons to the default optionpane. One of your buttons can be choosen as default button. The default-button will have the focus, after the optionpane shows up.

If the same “setOnAction” method will be called several times, only the last Eventhandler will be used. After clicking a button, the optionpane will hide automatically.

Beside of the four buttons, there is also an option to show a close-button on the optionpane.
That button will simply close the optionpane without triggering any other event or Eventhandler.

The options for the close-button are:
  * orientation
  * visability


```
JACPOptionPane pane = JACPDialogUtil.createOptionPane("JACP OptionPane", "This is the default JACP OptionPane!");
pane.setOnOkAction(new EventHandler<ActionEvent>() {
	public void handle(ActionEvent arg0) {
		// OK action
		}
	});
pane.setOnCancelAction(new EventHandler<ActionEvent>() {
	public void handle(ActionEvent arg0) {
		// Cancel action
		}
	});
pane.setDefaultButton(JACPDialogButton.CANCEL);´
pane.setDefaultCloseButtonVisible(true);
pane.setDefaultCloseButtonOrientation(Pos.CENTER_RIGHT);
```

<img src='http://jacp.googlecode.com/svn/wiki/option-pane/JACP_Std_OptionPane.PNG' />

Showing and hiding the optionpane is managed by the **`JACPModalDialog`**.

To show a dialog you have to provide a Node, that should be displayed (e.g. **`JACPOptionPane`** or a Custom Node).
Call **`hideModalMessage()`** will hide the dialog, which is currently shown.

```
JACPModalDialog.getInstance().showModalMessage(new CustomOptionPane());
```


```
JACPModalDialog.getInstance().hideModalMessage();
```


<img src='http://jacp.googlecode.com/svn/wiki/option-pane/JACP_Custom_OptionPane.PNG' />


## Styling the JacpFX `OptionPane` ##

The `JACPOptionPane` comes with a default theme, which can be overridden using a custom stylesheet. Every part of the pane has a corresponding css-class.


<img src='http://jacp.googlecode.com/svn/wiki/option-pane/JACP_CSS_OptionPane.png' />

**Attention:**
The message part uses the JavaFX `Text` class. So ensure to use `-fx-fill` to apply a color to the message text. All other parts will use `-fx-text-fill`.

With a custom stylesheet, using the mentioned classes, you’re able to style the option pane to your specific needs.

<img src='http://jacp.googlecode.com/svn/wiki/option-pane/JACP_Styled_OptionPane.JPG' />



# JACPToolBar and JACPMenuBar #

## JACPToolBar ##
## usage ##

JacpFX uses an extended Toolbar, which allows to add Buttons to either end.

You can apply Nodes to the **`JACPToolbar`** by using the methods:
  * `add(Node node)`
  * `addOnEnd(Node node)`

The end portion of a toolbar will be the right hand side, if the toolbar has a horizontal orientation or the bottom end, if the toolbar is oriented vertically.

Added nodes on the toolbar, can be removed by using the `remove` method.

<img src='http://jacp.googlecode.com/svn/wiki/toolbar/JACP_Std_Toolbar.JPG' />

## styling ##

Due to the fact that the **`JACPToolbar`** only uses two style classes, where one of them is only for very special styling needs, the styling is pretty easy.

The two classes for styling are:
  * **`.jacp-tool-bar`**
  * **`.jacp-button-bars`** (defines the container for the added nodes on either end)

<img src='http://jacp.googlecode.com/svn/wiki/toolbar/JACP-ToolBar.png' />

Every Node, you will add, has to be styled separately.

After styling the **`JACPToolbar`** and the added nodes (buttons in this case) the Toolbar could look like this:

<img src='http://jacp.googlecode.com/svn/wiki/toolbar/JACP_Styled_Toolbar.JPG' />

## JACPMenuBar ##

## usage ##

The **`JACPMenuBar`** is registered to the Workbench. It is an extended `MenuBar`, which will take custom nodes, windowbuttons, and (of course) `MenuItems`. Just setup your menu within the postHandle-method of your workspace. Basically the menubar will work, as known from the JavaFX **`menuBar`**, for adding `MenuItems`.
Windowbuttons (minimize, resize, close) have to be registered if needed. The Buttons will only show up, if the Stage is set to `UNDECORATED`.
For MAC OS X users: Hence to the option of using the system-menu on the very top of the screen, the stage will always be `DECORATED`, thus the buttons will never show up.

```
public void postHandle(final FXComponentLayout layout) {
        final JACPMenuBar menu = layout.getMenu();
        final Menu menuFile = new Menu("File");
        menuFile.getItems().addAll(this.createExitEntry(), this.createInfoEntry());
        menu.getMenus().addAll(menuFile);
        menu.registerWindowButtons();
    }
```

<img src='http://jacp.googlecode.com/svn/wiki/menubar/JACP_Std_Menubar.JPG' />

Using the **`addNode(final JACPMenuBarButtonOrientation orientation, final Node... node) `** method, you can add custom nodes to the menubar, either on the left- or right hand side.

<img src='http://jacp.googlecode.com/svn/wiki/menubar/JACP_Std_Menubar_ext.JPG' />

## styling ##

Since the **`JACPMenuBar`**  uses a JavaFX **`MenuBar`** , for holding **`MenuItems`** , you can use the std. **`MenuBar`**  classes for styling.
The windowbuttons, are styled by using a set of classes and ids.

<img src='http://jacp.googlecode.com/svn/wiki/menubar/JACP_MenuBar.png' />

Styles Windowbuttons can look like this.

<img src='http://jacp.googlecode.com/svn/wiki/menubar/JACP_Styled_Menubar_ext.JPG' />




# Annotations overview #

## Class-level annotation ##
### `@Component` ###
Defines UI components and their meta-data. Possible attributes:
  * `defaultExecutionTarget (mandatory)`: defines an execution target provided by the perspective
  * `id (mandatory)`: the component id
  * `name (mandatory)`: the component name
  * `active`: the default active state on application startup, the default is "true"; when a component is set to false it will not be activated by the applications "init" message but by any other message
  * `resourceBundleLocation`: the name of your resource bundle
  * `localeID`: the default localeId (`http://www.oracle.com/technetwork/java/javase/locales-137662.html`), if not set the system default will be used

### `@DeclarativeComponent` ###
Defines FXMLcomponents and their meta-data. Possible attributes:
  * `defaultExecutionTarget (mandatory)`: defines an execution target provided by the perspective
  * `id (mandatory)`: the component id
  * `name (mandatory)`: the component name
  * `active`: the default active state on application startup, the default is "true"; when a component is set to false it will not be activated by the applications "init" message but by any other message
  * `resourceBundleLocation`: the name of your resource bundle
  * `localeID`: the default localeId (`http://www.oracle.com/technetwork/java/javase/locales-137662.html`), if not set the system default will be used
  * `viewLocation (mandatory)`: defines the location of the FXML document describing the components layout

### `@CallbackComponent` ###
Defines non UI service components and their meta-data:
  * `id (mandatory)`: the component id
  * `name (mandatory)`: the component name
  * `active`: the component active state on startup (default is true)

### `@Perspective` ###
Defines perspectives and their meta-data:
  * `id (mandatory)`, the Perspective id
  * `name (mandatory)`, the Perspective name
  * `resourceBundleLocation`, the bundle location for localization
  * `localeID`, the default locale

## Method-level annotation ##
### `@OnStart` ###
Lifecycle annotation, method marked with this are executed on component startup (also when component is reactivated). It ss applicable for all component types and perspectives. Annotated methods MUST NOT throw a checked exception.
Following method signature is applicable:
  * method with no parameters
  * with FXComponentLayout layout
  * with FXComponentLayout layout, URL url (in case of FXML components)
  * with FXComponentLayout layout, URL url (in case of FXML components) , ResourceBundle resourceBundle

### `@OnTearDown` ###
Lifecycle annotation, method marked with this are executed on component shutdown. It is applicable for all component types and perspectives. Annotated methods MUST NOT throw a checked exception.
Following method signature is applicable:
  * method with no parameters
  * with FXComponentLayout layout
  * with FXComponentLayout layout, URL url (in case of FXML components)
  * with FXComponentLayout layout, URL url (in case of FXML components) , ResourceBundle resourceBundle

# localisation and internationalization #
`@Component`, `@DeclarativeComponent` and `@Perspective` annotation allows declaration of a resource bundle and a default localeID. If no localeID is declared the system default is assumed. Set the relative resourceBundleLocation in URL (in resource) like `"bundles.languageBundle"` and create in resources/bundles a file `languageBundle_en.properties` for further informations on resource bundles see: `http://docs.oracle.com/javase/7/docs/api/java/util/ResourceBundle.html`.
## Get access to defines resource bundle: ##
  * a.) use the `@OnStart` annotation and creates a method with a ResourceBundle resourceBundle parameter.
  * b.) this.getResourceBundle()
# resources #
The default project layout provides following structure for resources:

src/main/resources:
  * bundles: resource bundle files
  * fxml: all fxml files
  * images: application images
  * styles: css files

# The usage of Spring-framework #
JacpFX uses Spring for dependency injection, this includes that the complete Spring stack is usable in JacpFX context. By default Spring annotations are not activated, JacpFX components wired declarative. To active annotation configuration add to global JacpFX-spring file (located in src/main/resources/main.xml):
```
 <context:component-scan base-package="my.beans"/>
 <context:annotation-config/>
```