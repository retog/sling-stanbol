# Sling Stanbol

An Apache Sling launcher comtaining Apache Stanbol and a servlet offering a
front-end to the Enhancer to the enhancer servlices

## Usage

- Compile using mvn
- change diretcory to sling-stanbol-launcher/target
- start the laucher with `java -jar sling-stanbol-launcher-1.0-SNAPSHOT.jar`
- Try out the enhancer servlet at: `http://localhost:8080/stanbol/enhancer`

## Important note

The dependecies are not yet in the maven central repository soyou might need to install them locally first (using `mvn install`):

- Apache Stanbol
- Apache Clerezza
- VIE (run `mvn install` in the maven directory)
