#!/bin/bash
sleep 10
cd /home/jdk/app
chmod u+x /home/jdk/app/mvnw
/home/jdk/app/mvnw clean install
/home/jdk/app/mvnw spring-boot:run