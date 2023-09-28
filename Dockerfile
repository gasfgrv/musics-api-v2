FROM openjdk:17-alpine
RUN addgroup --system spring && adduser --system spring spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh","-c","java -Daws.signingRegion=${AWS_SIGNINGREGION} -Daws.accessKey=${AWS_ACCESSKEY} -Daws.secretKey=${AWS_SECRETKEY} -Dspotify.clientId=${SPOTIFY_CLIENTID} -Dspotify.clientSecret=${SPOTIFY_CLIENTSECRET} -jar /app.jar"]