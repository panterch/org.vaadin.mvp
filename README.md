# MVP for Vaadin

### Getting started

Probably the best way to get started is to just clone this repository and check
out the example application.

There's also some documentation in form of a Maven site, though the site is not
published and you have to build it yourself running:

 `mvn site`
 
The generated site index is located in `REPO_ROOT/target/site/index.html`.

### Release builds

Releases are not published to Maven Central, however builds are available from 
this Maven repository:

 https://java.panter.ch/nexus/content/repositories/oss-releases/

Add this repository to your projects pom.xml using a `repositories`-section:

    <repositories>
      <repository>
        <id>vaadin-mvp-releases</id>
        <url>https://java.panter.ch/nexus/content/repositories/oss-releases/</url>
      </repository>
    </repositories>

If you're using a repository manager (Nexus, Archiva, ...) then add this repository
to its configuration.
