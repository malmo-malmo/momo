FROM adoptopenjdk:11-jre-hotspot

EXPOSE 8080

# 언어 설정
RUN locale-gen ko_KR.UTF-8
ENV LANG ko_KR.UTF-8
ENV LANGUAGE ko_KR.UTF-8
ENV LC_ALL ko_KR.UTF-8

# TimeZone 설정
ENV TZ Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir /opt/app
COPY /build/libs/momo-socket-1.0.jar /opt/app

CMD ["java","-jar", \
"-Dspring.profiles.active=dev", \
"/opt/app/momo-socket-1.0.jar"]
