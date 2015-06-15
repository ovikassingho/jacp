

# JacpFX 2 release #
With the new JacpFX 2 release we totally moved to github and we have a new website: `JacpFX` http://jacpfx.org

# 09.09.2013, We are moving to `GitHub` #
Development of JacpFX was moved to `GitHub` https://github.com/JacpFX
# JacpFX 1.2 release announcement #
`JacpFX`, the RCP framework on top of `JavaFX` and `Spring` reached version 1.2. This release is mainly a bugfix release and improves stability and overall performance. We decided to defer some new exiting features, because we plan to switch to `Java8/JavaFX8` with the next release of `JacpFX`.
Like before, you can create a `JacpFX` quickstart project with maven:

`mvn archetype:generate -DarchetypeGroupId=org.jacp -DarchetypeArtifactId=JacpFX-quickstart-archetype -DarchetypeVersion=1.2 -DarchetypeRepository=http://developer.ahcp.de/nexus/content/repositories/jacp`

Existing projects can simply switch to version 1.2 without any changes in Java API, only the `fx:controller` attribute in your FXML files must be removed now..

# JacpFX 1.1 release announcement #

We are proud to announce a new version of `JacpFX`, the `RCP` framework on top of `JavaFX` and Spring.  It provides a simple API to create a workspace, perspective, and components, combined with an Actor like component approach. JacpFX gives you an easy access to JavaFX development and helps you to create stunning Rich Client Applications quickly.

**Version 1.1 comes with a lot of enhancements:**
  * Official FXML support
    * Define FXML perspectives and components
    * Use FXML and (pure) JavaFX perspectives side by side
    * Mix FXML components and JavaFX components in perspectives

  * Annotation support
    * Define metadata for perspectives and components in annotations
    * Method-level annotations to control the lifecycle

  * Internationalization and localization
    * Define property files
    * Reference in component annotation
    * Accessable by calling getResourceBundle()

  * Custom component integration
    * Created custom (and customizable) components for:
      * Modal dialog
      * Menu
      * Toolbar

  * Enhanced tool chain
    * Complete maven integration
      * Nexus repo created to get an easy access to JacpFX artifacts
      * Maven quickstart-archetype created to create new projects easily
    * The new quickstart-archetype is a good starting point for you own development:
      * Defined project structure with sample css, fxml, images, resource bundles, preloader and native bundling


Updated [documentation](http://code.google.com/p/jacp/wiki/Documentation) and [quickstart-tutorial](http://code.google.com/p/jacp/wiki/JacpFXTutorial)

## Updated maven integration ##

**Create a (quickstart) project from archetype**
`mvn archetype:generate -DarchetypeGroupId=org.jacp -DarchetypeArtifactId=JacpFX-quickstart-archetype -DarchetypeVersion=1.1 -DarchetypeRepository=http://developer.ahcp.de/nexus/content/repositories/jacp`

**The repository location:**
```
        <repositories> 
                ... 
                <repository> 
                        <id>jacp</id> 
                        <url>http://developer.ahcp.de/nexus/content/repositories/jacp</url> 
                </repository> 
        </repositories>
```

**The JacpFX dependencies:**
```
<dependency> 
                        <groupId>org.jacp.project</groupId> 
                        <artifactId>JACP.API</artifactId> 
                        <version>1.1</version> 
                        <scope>compile</scope> 
                </dependency> 
                <dependency> 
                        <groupId>org.jacp.project</groupId> 
                        <artifactId>JACP.JavaFX</artifactId> 
                        <version>1.1</version> 
                        <scope>compile</scope> 
                </dependency> 
                <dependency> 
                        <groupId>org.jacp.project</groupId> 
                        <artifactId>JACP.JavaFXSpring</artifactId> 
                        <version>1.1</version> 
                        <scope>compile</scope> 
                </dependency> 
                <dependency> 
                        <groupId>org.jacp.project</groupId> 
                        <artifactId>JACP.JavaFXControls</artifactId> 
                        <version>1.1</version> 
                        <scope>compile</scope> 
                </dependency> 
```
# JacpFX #
JacpFX is an implementation of JACP in JavaFX to create Rich Clients (Desktop and/or Web) in MVC style with JavaFX, Spring and an Actor like component approach. It provides a simple API to create a workspace, perspectives, and components; to communicate with all parts and to compose your Client application easily.  JacpFX gives you an easy access to JavaFX development and helps you to create stunning Rich Client Applications quickly.
## What does JacpFX offer to you? ##
  * Easy access to JavaFX development
    * create a project from quickstart-archtype and start development in you favourite IDE
  * Implement scaling and responsive Rich Clients against a small and simple API (~120Kb) in Java
    * Fully integrated with Spring and JavaFX
  * Add/move/remove defined Components at run-time in your UI
    * Create your basic layout in Perspectives and define “placeholders” for your Component UI
  * Handle Component methods outside FX application thread
    * “handle” methods are executed in a worker thread and committing their results to a “posthandle” method that is executed in the FX application thread
  * Communicate through Asynchronous Messages.
    * Each Component can be accessed through messages. Passing parameters, results and events from one Component to an other.
  * Stateful and stateless callback non-UI components
    * Outsource long running processes and computation
  * Handle asynchronous processes easily
    * No need for explicit threading or things like Runtime.invoke()
  * No shared data between Components
  * Immutable Messages

# JACP (Java Asynchronous Client Platform) #
The goal: create lightweight frameworks to develop large scaling rich client / internet applications.  Provide simple components to create a feature rich, multithreaded UI and to integrate middleware components seamlessly.

a.) Find a simple White Label API that match following needs:
  * the API defines a minimal set on abstract components to create applications with workbench, perspectives and components
  * it clearly defines a basic message handling between components
  * it is basic enough to be implemented with different Toolkits like Swing, SWT, Vaadin or JavaFX 2.0 ...

b.) Create a „Actor Like“ approach to run/handle components
  * every component is handled on message call
  * components are handled in separate threads
  * components can be state- less/ful

c.) Create Peer-To-Peer based approach to handle component messaging on other/remote instances
  * make components executable outside message source JVM
  * create loadbalancing / scheduling to decide where to run component

---

# News section #
  * News 23. Oct. 2012 : official release of JacpFX 1.1
  * News 03. Oct. 2012 : Preparing to launch JacpFX 1.1. We spend a long time with bugfixing, infrastructure, archetype, styling and so on, now only the documentation left.
  * News 21. Jul. 2012 : JacpFX 1.1 beta release... we published an quickstart archetype today, so everyone can test the current developement (see: [JacpFXTutorial](http://code.google.com/p/jacp/wiki/JacpFXTutorial?ts=1342901117&updated=JacpFXTutorial)).
  * News 06. Jul. 2012 : sitll working on release 1.1. Code-frezze is planned for the next week, but all features and major bugs are done (still clean up left). The JacpFXDemo is nearly finished and includes examples for mix useage of JavaFX components and FXML components. Also an new archetype will be introduced. jacp-quickstart will include a JavaFX and a FXML perspective, as well as JavaFX and FXML components. So you can easilly use this application body to create your own JacpFX applications. Beside this, the maven build will support all possible packaging types from jar, jnlp, html as well as exe, msi, dmg and rpm.
  * NEWS 29. Mai. 2012 : **Implementation of FXML perspectives is finished**. Now you can use the complete JavaFX stack to create stunning RCP clients with JacpFX.
  * NEWS 21. Mai. 2012 : **Implementation of FXML components in JacpFX is finished**. Now you can mix pure Java(FX) components and FXML components in one perspective. FXML components are annotated like this: `@DeclarativeComponent( ...viewLocation = "/file.fxml")` unfortunately I had to introduce an new interface and a new abstract class because FXML components have always the same UI root element (the FXML Node) and they also act like a controller class for the FXML class so you can use @FXML annotation on members and methods as expected. Next we will implement a FXML Perspective so you can mix all types of implementation variants: (JFX Perspective -> JFX components; JFX Perspective -> FXML components; FXML Perspective -> JFX components ... and FXML Perspective  -> FXML component)
  * NEWS 30. April. 2012 : **No news means "heavy development"!** We are working hard for release 1.1. Propably we will release version 1.05 as intermediate version. All the annotations work and chages in user API (the last one... ) are quite stable. Next steps are the introduction of an UIExtra package and an Unit Test package. Also a Nexus and a hudson installation is planned and hopefully finished soon ;-)
  * NEWS 10. Mar. 2012 : **So finally and surprisingly my article was published at DZONE** (http://java.dzone.com/articles/building-rich-clients-jacpfx), I thought they forgot me... but I hope javacodegeeks will publish my article too. So like I said in Feb. now version 1 of JacpFX is official, no code changed in trunk but version 1.1 is in progress.
  * NEWS 19. Feb. 2012 : **I finally released version 1.0 of JacpFX and JACP API**. The official announcment I postpone until I finished the article. You can download all jars, demo and archetype at the Download area. I included a small Install documentation for the Demo code here: [InstallandRunJacpFXDemo](InstallandRunJacpFXDemo.md) but you can also simply run the Demo here: http://developer.ahcp.de/demo/JACPFX2Demo.html. The next days I will polish the documentation, add an officaial release plan and hopefully finish my article.

  * NEWS 02. Feb. 2012 nearly finished with the first beta release of JacpFX. Documentation is almost done and an article will be finished soon. Today we had a codefreeze so this is more or less the release date of JacpFX. I opend the download area where you can download a simple "local archetype" to create your own JacpFX project easilly. Precondition is to install JavaFX2 and the fx-ant jars in your local repository. Also the very first download of compiled jars are available.

  * NEWS 04. Jan. 2012 On my way to public the first beta release for the JavaFX implementation. I am currently working on documentation  (see Wiki->Documentation). The svn trunk was again slightly reorganized.
    * current folders in trunk:
      * `JACP` the maven root project (contains no source code)
      * `JACP.API` contains all interfaces, this project is the heart of JACP and is not depending on any UI or other technology
      * `JACP.JavaFX2` contains "black label" API for JavaFX2 implementation.
      * `JACP.JavaFX2Spring` contains all maven Spring dependencies and the Spring launcher.
      * `JACP.JavaFX2DemoArea` ... the demo area for JavaFX2 implementation.

  * NEWS 08. Nov. 2011 New install tutorial in wiki... see: JacpFX2InstallTutorial
    * svn trunk reorganized, Swing project parts moved to branch, currently only the API and JavaFX2 related projects in trunk
    * current folders in trunk:
      * `JACP` the maven root project (contains no source code)
      * `JACP.API` contains all interfaces, this project is the heart of JACP and is not depending on any UI or other technology
      * `JACP.JavaFX2` contains "black label" API for JavaFX2 implementation.
      * `JACP.JavaFX2DemoArea` ... the demo area for JavaFX2 implementation.
      * `JACP.Concurrency` .. work in progress, this project will contain all non UI components
  * NEWS 07. Nov. 2011 !!! JavaFX2 DemoArea initial commit, try out workbench and perspectives. New Profile (javafx2) created on JACP root project to build all JavaFX2 projects.
  * NEWS 30. Oct. 2011 !!! Migration of AWorkbench and APerspective almost finished. You can already create a working JavaFX2 UI and use the messaging bus to communicate between workbench and perspectives. Next steps are to migrate UIComonents wich will be the main challenge and to reimplement the non UI components (services). JavaFX2 is really nice, I like what I see.. If only a Linux implementation would be available. ;-)

  * `JACP` Project moved to maven. The project source was divided into five subprojects:
  * `JACP` the maven root project (contains no source code)
  * `JACP.API` contains all interfaces, this project is the heart of JACP and is not depending on any UI or other technology
  * `JACP.JavaFX2` contains "black label" API for JavaFX2 implementation.
  * `JACP.Swing` contains all API files of the current Swing application. If you want to develop Swing based projects this one is the right for you.
  * `JACP.Util` contains some Util classes such as the [Spring](http://www.springsource.org/) launcher, feel free to implement other launchers to use JACP with DI containers like [google guice](http://code.google.com/p/google-guice/)
  * `JACP.UnitTests` .. boring but necessary. Currently this code contains some ugly Thread.sleep statements, so on older computers this code may fail ;-( feel free to help me on testing asynchronous UI code...

Andy



---


Thank you to:

```
YourKit is kindly supporting open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of innovative and intelligent tools for profiling
Java and .NET applications. Take a look at YourKit's leading software products:
```
<a href='http://www.yourkit.com/java/profiler/index.jsp'><code>YourKit Java Profiler</code></a> and
<a href='http://www.yourkit.com/.net/profiler/index.jsp'><code>YourKit .NET Profiler</code></a>.



---


<a href='http://stan4j.com'> <img src='http://logo.stan4j.com/stan4j-132x48-t0.png' title='stan4j.com' height='48' width='132' alt='stan4j.com' border='0' /> </a>