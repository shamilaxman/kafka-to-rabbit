FROM centos:7

RUN localedef -c -f UTF-8 -i en_US en_US.UTF-8
ENV LC_ALL=en_US.UTF-8

RUN yum install -y \
       java-1.8.0-openjdk \
       java-1.8.0-openjdk-devel

ENV JAVA_HOME /etc/alternatives/jre

WORKDIR /
ADD kafka2rabbit.bridge.properties kafka2rabbit.bridge.properties
ADD target/kafka-0.0.1-SNAPSHOT-jar-with-dependencies.jar Kafka2Rabbit.jar
CMD java -Dfile.encoding=UTF-8 -jar Kafka2Rabbit.jar