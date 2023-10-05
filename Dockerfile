FROM openjdk:17-alpine
RUN addgroup --system spring && adduser --system spring spring
USER spring:spring
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["sh","-c","java -Daws.signingRegion=${AWS_SIGNINGREGION} -Daws.accessKey=${AWS_ACCESSKEY} -Daws.secretKey=${AWS_SECRETKEY} -Daws.serviceEndpoint.dynamo=${AWS_ENDPOINT_DYNAMO} -Daws.serviceEndpoint.secretsManager=${AWS_ENDPOINT_SECRETS_MANAGER} -jar /app.jar"]