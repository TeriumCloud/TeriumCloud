FROM openjdk:17-alpine

RUN mkdir /app
WORKDIR /app
EXPOSE 3000-5000

ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=50", "-XX:CompileThreshold=100", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCompressedOops", "-Xmx512m", "-Xms256m", "-jar", "terium-OXYGEN.jar"]