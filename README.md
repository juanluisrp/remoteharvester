# JRC Ingester

## Configuration

The configuration is applied in the file `src/main/resources/application.properties`. The following properties should be configured:

- `geonetwork.baseUrl`: Base url of the GeoNetwork application
- `geonetwork.username`: GeoNetwork username for an administration user.
- `geonetwork.password`: GeoNetwork password for an administration user.
- `harvester.jdbc.url`: JDBC connection string for the harvester database.
- `harvester.jdbc.user`: Database username for the harvester database.
- `harvester.jdbc.pass`: Database user password for the harvester database.
- `ingester.jdbc.url`: JDBC connection string for the ingester database.
- `ingester.jdbc.user`: Database username for the ingester database.
- `ingester.jdbc.pass`: Database user password for the ingester database.
- `metadata.jdbc.url`: JDBC connection string for the GeoNetwork database.
- `metadata.jdbc.user`: Database username for the metadata database.
- `metadata.jdbc.pass`: Database user password for the metadata database.
- `activemq.url`: ActiveMQ url.
- `app.jettyHost`: Jetty host.
- `app.jettyPort`: Jetty port.

Example:

```
geonetwork.baseUrl=http://localhost:9090/geonetwork
geonetwork.username=admin
geonetwork.password=admin

jdbc.driverClassName=org.postgresql.Driver
harvester.jdbc.url=jdbc:postgresql://localhost:5432/INSPIRE-HARVEST
harvester.jdbc.user=postgres
harvester.jdbc.pass=postgres

ingester.jdbc.url=jdbc:postgresql://localhost:5432/INSPIRE-INGEST
ingester.jdbc.user=postgres
ingester.jdbc.pass=postgres

metadata.jdbc.url=jdbc:postgresql://localhost:5432/GN4-JRC
metadata.jdbc.user=postgres
metadata.jdbc.pass=postgres

hibernate.hbm2ddl.auto=none
hibernate.physical_naming_strategy=com.geocat.ingester.config.CustomPhysicalNamingStrategy
hibernate.dialect=org.hibernate.dialect.PostgreSQL10Dialect

activemq.url=tcp://localhost:61616?jms.prefetchPolicy.queuePrefetch=1
app.jettyHost=0.0.0.0
app.jettyPort=9999
```

## Build

The build process creates a `jar` file with the application and an embedded Tomcat. To build the application:

```
mvn package
```
