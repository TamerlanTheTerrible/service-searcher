# select image
FROM amazoncorretto:17 as service-searcher-prod

# set deployment directory
#WORKDIR /app

ARG JAR_FILE=*.jar
COPY ${JAR_FILE} service-search-telegram-bot-0.0.1-SNAPSHOT.jar ./

# set the startup command to run your binary
CMD ["java", "-jar", "./service-search-telegram-bot-0.0.1-SNAPSHOT.jar"]