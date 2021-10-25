FROM maven:3-adoptopenjdk-8 as builder

COPY ./pom.xml ./pom.xml
COPY ./src ./src

RUN mvn -B dependency:go-offline
RUN mvn -B package -DskipTests


FROM adoptopenjdk:8-jre-hotspot

LABEL vendor="GeoCat B.V."
LABEL org.opencontainers.image.source https://github.com/GeoCat/csw-ingester


# Check the file application.properties for a description of the environment variables that can be customized.
# The property names can be translated to environment varibles passing them to upper case and replacing the dots
# with underscores. For example harvester.jdbc.url -> HARVESTER_JDBC_URL

RUN mkdir -p /opt/jrc-ingester
COPY --from=builder target/*.jar /opt/jrc-ingester/ingester.jar
WORKDIR /opt/jrc-ingester

EXPOSE 9999
CMD [ "java", "-jar", "ingester.jar" ]
#ENTRYPOINT exec java $JAVA_OPTS -jar ingester.jar