package com.prad.kafka
import com.prad.util.utility
import org.apache.kafka.clients.consumer._
import java.util.Properties
import scala.collection.JavaConverters._
import java.util
import com.prad.util.Globals

object kafkatest {
  def main(args: Array[String]): Unit = {
    val props = new Properties()
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put(ConsumerConfig.GROUP_ID_CONFIG,"kafkacorona")
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest")

    val kafka_consumer = new KafkaConsumer[String, String](props)

    kafka_consumer.subscribe(util.Collections.singletonList("corona-metric"))

    implicit val formats = org.json4s.DefaultFormats

    while(true){
      val records=kafka_consumer.poll(100)
      for (record<-records.asScala){
        println(record)
      }
    }

  }
}
