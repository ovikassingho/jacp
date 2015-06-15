#install and usage tutorial for JacpFX.

**This Tutorial will give you a short introduction how to start development with JacpFX**



# Prerequirements #
  * `OS: windows >= xp, OSX (lion), any Linux`
  * `Java 7 update >6`
  * maven installed
  * set JAVA\_HOME, M2\_HOME


if you want to create native executables yo need:

  * on windows: http://www.jrsoftware.org/isdl.php to generate exe; http://wix.sourceforge.net/ to generate msi
  * on osx (xcode? it is always installed? at least for me ;-) )
  * on linux "rpmbuild" for rpm´s... on debain he simply creates the deb´s

# Create a (quickstart) project from archetype #
```
   mvn archetype:generate  -DarchetypeGroupId=org.jacp  -DarchetypeArtifactId=JacpFX-quickstart-archetype  -DarchetypeVersion=1.2  -DarchetypeRepository=http://developer.ahcp.de/nexus/content/repositories/jacp

```

Add a groupId (like com.my.company), an artifactId (which will be the project name  ... `MyFirstJacpFXProject`) and press enter for the rest (or change version number and package name ).


# JacpFX development #
  * The quickstart-archetype genaretes an example project including two **perspectives `(pure JavaFX and FXML)`**, two (pure) **`JavaFX` components**, two **`FXML` components**, a stateful- and a stateless-component and one simple spring bean. It includes all ressources and configurations to start real life project from scratch. the resource folder contains following folders:

  * **bundles** : contains the message.properties
  * **fxml** : contains the fxml files
  * **images** : all application images
  * **css** : all stylesheets

The pom.xml includes all Ant-Tasks from Oracle to create native executables, create binary css, executeable jar´s and jnlp files. For web deployment you should uncomment the jar signing and add you keys there.

# Build the project #
  * To build the created archetype project as it is, simply go to the `$PROJECT_HOME` `MyFirstJacpFXProject` and type: **`mvn package`**
  * When the build process is finished you find the executeables in `$PROJECT_HOME`/target/deploy ; the native executeables you find in ../target/deploy/bundles ... depending on your platform simply execute the `*.exe, double click the *.dng, or type sudo dpkg -i *.deb`. To execute the `*.jar` located in deploy folder, simply type **`java -jar project-name.jar`**

# Import to Eclipse/Netbeans/IDEA #
Maven projects can be imported directly to Eclipse, Netbeans and IDEA. If you want to create "native" projects simply execute in root folder **`mvn eclipse:eclipse, or mvn netbeans:netbeans or mvn idea:idea`**

# Integration into existing maven project #
## The repository location: ##
```
	<repositories>
                ...
		<repository>
			<id>jacp</id>
			<url>http://developer.ahcp.de/nexus/content/repositories/jacp</url>
		</repository>
	</repositories>
```

## The JacpFX dependencies: ##

```
                <dependency>
			<groupId>org.jacp.project</groupId>
			<artifactId>JACP.API</artifactId>
			<version>1.2</version>
			<scope>compile</scope>
		</dependency>
		<dependency>
			<groupId>org.jacp.project</groupId>
			<artifactId>JACP.JavaFX</artifactId>
			<version>1.2</version>
			<scope>compile</scope>
		</dependency>
		<dependency>
			<groupId>org.jacp.project</groupId>
			<artifactId>JACP.JavaFXSpring</artifactId>
			<version>1.2</version>
			<scope>compile</scope>
		</dependency>
		<dependency>
			<groupId>org.jacp.project</groupId>
			<artifactId>JACP.JavaFXControls</artifactId>
			<version>1.2</version>
			<scope>compile</scope>
		</dependency>
```

## The reference to JavaFX ##
```
		<dependency>
			<groupId>com.oracle</groupId>
			<artifactId>javafx</artifactId>
			<version>2.2</version>
			<systemPath>${java.home}/lib/jfxrt.jar</systemPath>
			<scope>system</scope>
		</dependency>
```