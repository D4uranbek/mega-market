FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu

#RUN apt-get update && echo "1"
#RUN apt-get install -y vim nano
WORKDIR /home/jdk/app

COPY cmd.sh /tmp
RUN chmod u+x /tmp/cmd.sh

#RUN javac /home/jdk/app/src/main/java/com/yandex/mega_market/MegaMarketApplication.java
CMD ["/tmp/cmd.sh"]
