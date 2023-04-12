FROM public.ecr.aws/ubuntu/ubuntu:22.04 as build-image-base

ARG APP_NAME=spring-boot-doma2-rest-sample

ENV JAVA_VERSION 22.3.1.r17-grl
ENV GRADLE_VERSION 8.0.2
ENV LANG ja_JP.UTF-8

ENV DOCKER_UID 1000
ENV DOCKER_USER docker
ENV DOCKER_PASSWORD docker

RUN apt-get update
RUN apt-get install -y gcc zlib1g-dev build-essential language-pack-ja-base language-pack-ja locales
RUN locale-gen ja_JP.UTF-8
RUN rm /bin/sh && ln -s /bin/bash /bin/sh
RUN apt-get -qq -y install curl wget unzip zip
RUN useradd -m --uid ${DOCKER_UID} --groups sudo ${DOCKER_USER} \
        && echo ${DOCKER_USER}:${DOCKER_PASSWORD} | chpasswd

USER ${DOCKER_USER}
RUN curl -s "https://get.sdkman.io" | /bin/bash
RUN set -x \
        && echo "sdkman_auto_answer=true" > $HOME/.sdkman/etc/config \
        && echo "sdkman_auto_selfupdate=false" >> $HOME/.sdkman/etc/config \
        && echo "sdkman_insecure_ssl=false" >> $HOME/.sdkman/etc/config
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh \
        && sdk install java ${JAVA_VERSION} \
        && gu install native-image \
        && sdk install gradle ${GRADLE_VERSION}"

FROM build-image-base as build-image
USER ${DOCKER_USER}
RUN bash -c "mkdir -p /home/$DOCKER_USER/app"
WORKDIR /home/$DOCKER_USER/app
COPY . .
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh \
        && gradle clean nativeCompile --stacktrace"

FROM public.ecr.aws/docker/library/alpine:latest
COPY --from=public.ecr.aws/awsguru/aws-lambda-adapter:0.6.4 /lambda-adapter /opt/extensions/lambda-adapter
EXPOSE 8080
WORKDIR /opt
COPY --from=build-image /home/docker/app/build/native/nativeCompile/$APP_NAME app
CMD [ "sh", "-c", "/opt/app" ]
