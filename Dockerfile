FROM ghcr.io/graalvm/jdk-community:21
ENV LANG=C.UTF-8
COPY ./build/libs/dolbit-normalno-all.jar /home/app/dolbit-normalno.jar
WORKDIR /home/app
CMD ["java", "-jar", "dolbit-normalno.jar"]
