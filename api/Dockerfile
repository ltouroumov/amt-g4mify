FROM openjdk:8-jdk

# Copy gradle
COPY ./gradle /app/gradle
COPY gradlew /app/
COPY *.gradle /app/

# Copy app
COPY ./res /app/res
COPY ./src /app/src

WORKDIR /app
RUN ./gradlew build -x test

ENV JAVA_OPTS=""
CMD java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/build/libs/amt-g4mify.jar