# Sling-Stanbol

An Apache Sling launcher containing Apache Stanbol and a servlet offering a
front-end to the Enhancer to the enhancer services

## Compiling

Sling-Stanbol is compiled using maven version 3 or higher. You can download maven from <http://maven.apache.org/download.html>.

As some of he dependecies are not (yet) available in maven central repository their 
source is included as git submodules. They are built with the top level maven build.

make sure you have the submodules installed with

git submodule init
git submodule update

You can compile sling-stanbol by running `mvn install` in the directory where this readme file is located.

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
