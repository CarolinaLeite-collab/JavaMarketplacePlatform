ARG APP_VERSION=local
ARG VCS_REF=unknown
ARG BUILD_DATE=unknown

FROM maven:3.9-eclipse-temurin-21-alpine@sha256:c3b70520630a94abc4bb9d87bb3c6a0bb44f936e0ef1035233727411c7b5b854 AS builder

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine@sha256:704db3c40204a44f471191446ddd9cda5d60dab40f0e15c6507b815ed897238b

WORKDIR /app

ARG APP_VERSION
ARG VCS_REF
ARG BUILD_DATE

LABEL org.opencontainers.image.title="mitelovers-backend" \
      org.opencontainers.image.version="${APP_VERSION}" \
      org.opencontainers.image.revision="${VCS_REF}" \
      org.opencontainers.image.created="${BUILD_DATE}"

RUN apk update && apk upgrade --no-cache

RUN addgroup -S appgroup && adduser -S appuser -G appgroup
RUN mkdir -p /app/data && chown -R appuser:appgroup /app/data

COPY --from=builder --chown=appuser:appgroup /app/target/*.jar app.jar

USER appuser

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
