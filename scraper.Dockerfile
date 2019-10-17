FROM openjdk:11-jdk-slim
WORKDIR /var/lib/app
COPY . .

# ENTRYPOINT ./scraper.jar