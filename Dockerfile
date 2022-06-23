FROM openjdk:17

RUN apt-get update && echo "1"
RUN apt-get install -y vim nano
WORKDIR /home/jdk/app
RUN javac MegaMarketApplication.java
CMD ["java", "MegaMarketApplication"]