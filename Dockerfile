FROM maven:3-eclipse-temurin-8 as builder

COPY ./pom.xml ./pom.xml
COPY ./src ./src

RUN mvn -B dependency:go-offline
RUN mvn -B package -DskipTests


FROM eclipse-temurin:8-jre

LABEL vendor="GeoCat B.V."


# Check the file application.properties for a description of the environment variables that can be customized.
# The property names can be translated to environment varibles passing them to upper case and replacing the dots
# with underscores. For example harvester.jdbc.url -> HARVESTER_JDBC_URL

RUN mkdir -p /opt/full-orchestrator
COPY --from=builder target/*.jar /opt/full-orchestrator/full-orchestrator.jar
WORKDIR /opt/full-orchestrator

EXPOSE 9999
CMD [ "java", "-jar", "full-orchestrator.jar" ]
#ENTRYPOINT exec java $JAVA_OPTS -jar ingester.jar