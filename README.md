# Kafka-Sparkstreaming-MongoDB
Kafka-Sparkstreaming with Mongo DB for Data processing and Offset Management

Setting up Kafka 
start zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties

start kafka
bin/kafka-server-start.sh config/server.properties

Create a topic 
bin/kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic corona-metric --create --partitions 3 --replication-factor 1

list topics
bin/kafka-topics.sh -zookeeper 127.0.0.1:2181 --list

Producer
bin/kafka-console-producer.sh  --topic first-topic --broker-list 127.0.0.1:9092

Consumer
bin/kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic corona-metric

Consumer group
bin/kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic corona-metric --consumer-property group.id="kafkacorona"

Mongo DB
1. Setup Mongo Local with below collections
offset
offsethistory
country_metric



