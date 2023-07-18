FROM --platform=$BUILDPLATFORM maven:3-eclipse-temurin-8 AS builder

ARG MODULE
WORKDIR /sources
COPY ./ /sources

# store maven dependencies so next build doesn't have to download them again
RUN --mount=type=cache,target=/root/.m2/repository \
	mvn -B dependency:go-offline 



RUN --mount=type=cache,target=/root/.m2/repository \
	mvn -B package -DskipTests
RUN mkdir /application && \
	cp $MODULE/target/*.jar /application/application.jar

WORKDIR /application

# Extract spring boot JAR layers
RUN --mount=type=cache,target=/root/.m2/repository \
	java -Djarmode=layertools -jar application.jar extract


FROM eclipse-temurin:8-jre AS finalImage

LABEL vendor="GeoNetwork"
ARG MODULE


# Check the file application.properties for a description of the environment variables that can be customized.
# The property names can be translated to environment varibles passing them to upper case and replacing the dots
# with underscores. For example harvester.jdbc.url -> HARVESTER_JDBC_URL
COPY $MODULE/extra/certs/ /certs
COPY $MODULE/extra/bin /usr/local/bin

RUN chmod -R +x /usr/local/bin && \
	import_certs.sh
WORKDIR /opt/$MODULE

COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/spring-boot-loader ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/application/ ./

EXPOSE 9999
CMD [ "java", "org.springframework.boot.loader.JarLauncher" ]
#ENTRYPOINT exec java $JAVA_OPTS -jar ingester.jar
