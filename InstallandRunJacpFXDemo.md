#Build and compile instructions for JacpFXDemo - client

# Install JacpFX Demo project from source #

If you simply want to try the demo, run it on this site  http://developer.ahcp.de/demo/JACPFX2Demo.html. Here you can see an embedded Version and also start the jnlp version. The online demo is currently only available for windows. With the official release of JavaFX on Mac and Linux it will run on all platforms. Mac and Linux Users should download the JavaFX beta versions and build the project from source.

## Build the project from source ##
Follow this steps to compile and run the demo. All JacpFX-Projects are Maven projects and require JavaFX 2 (jfxrt.jar). Some include JavaFX 2 as system dependency but I prefer to register the jfxrt.jar in the local repository. So, similar to [JacpFX2InstallTutorial](JacpFX2InstallTutorial.md) (you should check the Prerequisites), register the jfxrt.jar and ant-javafx.jar to your local maven repository.

  * When you installed jfxrt.jar and ant-javafx.jar successfully, download and unpack the JacpFXDemo.zip
  * go to JacpFXDemo directory and run/type **`mvn package`**, this will generate a target/deploy folder where you can find the html and the jnlp file to run the demo
  * if you want to create an Eclipse project simply type:**` mvn eclipse:eclipse`**.