#Features planned for a first alpha release

# Release/Feature plan #

## Version 1.0 (Feb. 2012) ##
  * implement `JacpFX OptionPane` components DONE
  * refractor project and extract DI (Spring) bundle DONE
  * maven support DONE
  * messaging between components in different perspectives DONE
  * background component messaging implemented DONE
  * switch target environment from one perspective to an other DONE
  * switch target in perspective DONE
  * define perspective layout: register root node and all leaves in your layout representing targets in perspective DONE
  * create workbench, perspectives, ui components, background components DONE
  * implement scheduler with `JavaFX`  tasks and services
  * define messaging capabilities and component handling DONE
  * create initial `JavaFX 2`  implementation to define workbench, perspectives, components DONE

## Version 1.1 (Okt. 2012) ##
  * avoid manual declaration of scope=prototype when use spring and stateless components -- DONE
  * annotation support: make meta tags like id, name, execution target as annotations available -- DONE
  * annotation support: provide access to "onStartup, onTeardown" by annotations and remove abstract methods from API -- DONE
  * official `FXML support / FXMLComponents and FXMLPerspectives` --DONE
  * cleanup and refractor component API -- DONE
  * define custom `Menu/ToolBar components` --DONE
  * extract UI component package to make UI components such as `OptionPane, custom- Menu/Toolbar ` optional in API  --DONE
  * provide a nexus repository for easy integration of JacpFX in maven projects -- DONE
  * create archetypes (simple and extended) for easy start up with JacpFX  --DONE


### Fixed Bugs ###
  * fixed bug where a message to a component right after / while initialization can be dropped because it is added to message queue and the initWorker won't receive messages from this queue
  * fixed missing content if no toolbars were defined
  * fixed Nullpointer when no subcomponents defined in perspective
  * fixed bug with reassignment of components in perspective when switching between perspectives

## Version 1.2 (April. 2013) ##
  * overall performance optimizations
  * `fx:controller` definition in fxml is obsolete now

### Fixed Bugs ###
  * fix bug with incorrect `@OnStartup` and `@OnTearDown` in non UI components
  * fix bug with incorrect application close when messages in queue
  * `FXComponentReplaceWorker`, Active(false) was only evaluated in handle method but never in postHandle method
  * `TearDownHandler`, update to support teardown of `StatelessComponent` instances
  * `AFXComponentWorker`: removed global lockQueue and add `invokeOnFXThreadAndWait` method, removed lock and release methods
  * `JACPOptionPane`: auto-hide not mandatory - available as option -> avoid disappearing dialogs, if they are chained
  * `LayoutUtil`: added some more util-functionalities to `CSS-` and `LayoutUtil`


## Version 2 (September. 2013) ##
  * introduce managed dialogs, a managed dialog is a child of an `UIComonent` and has access to DI container
  * introduce `JACPContext` and `@Ressource` annotation, this context object will contain access to `DialogHandlers`, menue and toolBars
  * switch to `Java8/JavaFX8`

