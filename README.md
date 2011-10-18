# Sling Stanbol

An Apache Sling launcher comtaining Apache Stanbol and a servlet offering a
front-end to the Enhancer to the enhancer servlices

## Compiling

sling-stanbol is compiled using maven. Currently sevveral dependencies are not yet available in the maven central repository. So you probably need to install the locally fisrt

### Annotate.js

clone the repo:
`git clone git://github.com/IKS/annotate.js.git`

change to the annotate.js/lib directory and install with maven

    cd annotate.js/lib
    mvn install

### Stanbol

clone the repo:
`git clone https://github.com/apache/stanbol.git`

change to the stanbol directory and install with maven

    cd stanbol
    mvn install -Dmaven.test.skip=true

For some yet unclear reasons, you might have to run the last command again if the build fails.

### JAX-RS support for sling

As [SLING-2192](https://issues.apache.org/jira/browse/SLING-2192) isn't solved yet, you also have to compile this bundle

Clone the repo:
`git clone git://github.com/retobg/slingrs.git`

change to the slingrs directory and compile with maven:

    cd slingrs
    mvn install

### Sling Stanbol

Finally you can compile sling-stanbol by running `mvn install` in the directory where this readme file is located.

## Launching

- change diretcory to sling-stanbol-launcher/target
- start the laucher with `java -jar sling-stanbol-launcher-1.0-SNAPSHOT.jar`
- You may upload html document (using WebDav for example) and open them in the browser, by adding the ".stanbol" suffix to the URI you should see a page that allows enhancing and editing the resource
- To try the resource page you may go to `http://localhost:8080/turkey.html.stanbol`
- Try out the enhancer servlet at: `http://localhost:8080/stanbol/enhancer`

