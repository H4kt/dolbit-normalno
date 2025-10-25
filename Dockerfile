FROM amazoncorretto:21.0.9-alpine
ENV LANG=C.UTF-8
RUN apk update
RUN apk add python3 curl
RUN curl -L https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp -o /usr/local/bin/yt-dlp
RUN chmod a+rx /usr/local/bin/yt-dlp
RUN mkdir /home/app
COPY ./build/libs/pivo-sound-all.jar /home/app/pivo-sound.jar
WORKDIR /home/app
CMD ["java", "-jar", "pivo-sound.jar"]
