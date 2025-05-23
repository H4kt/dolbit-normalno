FROM amazoncorretto:21.0.7-al2023-headless
ENV LANG=C.UTF-8
RUN yum update -y
RUN yum install -y python3
RUN curl -L https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp -o /usr/local/bin/yt-dlp
RUN chmod a+rx /usr/local/bin/yt-dlp
RUN yum clean all
CMD ["java", "-version"]
