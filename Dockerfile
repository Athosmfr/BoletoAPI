FROM gradle:7.6.1-jdk17 AS build
COPY --chown=gradle:gradle . .
WORKDIR .
RUN gradle build -x test --no-daemon
EXPOSE 8080
CMD ["gradle", "bootRun"]