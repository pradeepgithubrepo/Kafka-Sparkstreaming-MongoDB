# Kafka-Sparkstreaming-MongoDB
Kafka-Sparkstreaming with Mongo DB for Data processing and Offset Management

1 . Setting up Kafka 


a. start zookeeper

bin/zookeeper-server-start.sh config/zookeeper.properties

b. start kafka

bin/kafka-server-start.sh config/server.properties

c. Create a topic 

bin/kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic corona-metric --create --partitions 3 --replication-factor 1

d. list topics

bin/kafka-topics.sh -zookeeper 127.0.0.1:2181 --list

e. Producer

bin/kafka-console-producer.sh  --topic first-topic --broker-list 127.0.0.1:9092

f. Consumer

bin/kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic corona-metric

g. Consumer group

bin/kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic corona-metric --consumer-property group.id="kafkacorona"

2. Mongo DB

Setup Mongo Local with below collections

a. offset

b. offsethistory

c. country_metric



