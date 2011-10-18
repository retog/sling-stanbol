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
`https://github.com/apache/stanbol.git`

change to the stanbol directory and install with maven

    cd stanbol
    mvn install

### JAX-RS support for sling

TBD

### Sling Stanbol

Finally you can compile sling-stanbol by running `mvn install` in the directory where this readme file is located.

## Launching

- change diretcory to sling-stanbol-launcher/target
- start the laucher with `java -jar sling-stanbol-launcher-1.0-SNAPSHOT.jar`
- Try out the enhancer servlet at: `http://localhost:8080/stanbol/enhancer`

