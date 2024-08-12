FROM ubuntu:20.04

ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y \
    curl \
    gnupg \
    tar \
    gzip \
    git \
    docker.io \
    software-properties-common \
    wget \
    tzdata \
    vim

ENV TZ=Asia/Tokyo
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN wget -O- https://apt.corretto.aws/corretto.key | apt-key add - \
    && add-apt-repository 'deb https://apt.corretto.aws stable main' \
    && apt-get update \
    && apt-get install -y java-11-amazon-corretto-jdk

RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list \
    && curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x99E82A75642AC823" | apt-key add -

RUN apt-get update && apt-get install -y sbt

ARG USERNAME

ENV USERNAME=${USERNAME}

RUN mkdir -p /etc/sudoers.d \
    && useradd -m -s /bin/bash $USERNAME \
    && echo "$USERNAME ALL=(ALL) NOPASSWD:ALL" >> /etc/sudoers.d/$USERNAME \
    && chmod 0440 /etc/sudoers.d/$USERNAME

WORKDIR /workspaces/exam-manager

RUN chown -R $USERNAME:$USERNAME /workspaces/exam-manager
USER $USERNAME
RUN sbt compile

CMD ["sbt", "shell"]
