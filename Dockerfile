FROM openjdk:11-jdk-slim

WORKDIR /app

RUN apt-get update && apt-get install -y curl gnupg && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list && \
    curl -sL "https://keybase.io/sbt/pgp_keys.asc" | apt-key add && \
    apt-get update && apt-get install -y sbt

COPY . .

RUN sbt compile stage

CMD ["sbt", "run"]
