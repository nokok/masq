FROM ghcr.io/graalvm/graalvm-ce:latest as gradle-graalvm

WORKDIR /build

RUN gu install native-image

ADD ./gradlew ./
ADD ./gradle ./gradle
ADD settings.gradle ./

RUN ./gradlew

FROM gradle-graalvm

ADD . .

RUN ./gradlew nativeImage

CMD ./build/bin/masq
