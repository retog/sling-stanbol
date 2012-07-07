# Sling-Stanbol

An Apache Sling launcher containing Apache Stanbol and a servlet offering a
front-end to the Enhancer to the enhancer services

## Compiling

Sling-Stanbol is compiled using maven version 3 or higher. You can download maven from <http://maven.apache.org/download.html>.

As several dependencies are not yet available in the maven central repository, we first compile these dependencies locally.

### Annotate.js

clone the repo:
`git clone git://github.com/IKS/annotate.js.git`

change to the annotate.js/lib directory and install with maven

    cd annotate.js/lib
    mvn install

### Stanbol

Sling-Stanbol is based on the latest released version of Apache Stanbol. Unfortunately not all of Stanbol is in maven central.

You need to manually install the Stanbol third party dependencies as this is described in:

https://svn.apache.org/repos/asf/incubator/stanbol/tags/0.9.0-incubating/README.md

Once you have installed the thrird party dependencies you can compile the Stanbol.

check out Stnabol data
`svn co https://svn.apache.org/repos/asf/incubator/stanbol/tags/0.9.0-incubating/data stanbol-data


change to the stanbol-data directory and install with maven

    cd stanbol-data
    mvn install -Dmaven.test.skip=true

### JAX-RS support for sling

As [SLING-2192](https://issues.apache.org/jira/browse/SLING-2192) isn't solved yet, you also have to compile this bundle

Clone the repo:
`git clone git://github.com/retobg/slingrs.git`

change to the slingrs directory and compile with maven:

    cd slingrs
    mvn install

### Sling Stanbol

Finally you can compile sling-stanbol by running `mvn install` in the directory where this readme file is located.

## Important note regarding Webdav
Webdav is broken with the lastest Sling release, so you need to manually downgrade a bundle:

See https://issues.apache.org/jira/browse/SLING-2443, you need to revert
org.apache.sling.servlets.resolver to 2.1.0 .

## Launching

- change diretcory to sling-stanbol-launcher/target
- start the laucher with `java -jar sling-stanbol-launcher-1.0-SNAPSHOT.jar`
- You may upload html document (using WebDav for example) and open them in the browser, by adding the ".stanbol" suffix to the URI you should see a page that allows enhancing and editing the resource
- To try the resource page you may go to `http://localhost:8080/slingstanbol/turkey.html.stanbol`
- Try out the enhancer servlet at: `http://localhost:8080/stanbol/enhancer`

## Further Information

- Enhancing documents demo: <http://vimeo.com/31509786>
- Facetted browsing demo: <http://blog.iks-project.eu/sling-stanbol-cms-adapte/>
