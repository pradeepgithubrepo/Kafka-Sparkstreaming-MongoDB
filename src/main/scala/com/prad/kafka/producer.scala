package com.prad.kafka
import java.util.Properties
import com.prad.util.{utility,Globals}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.json4s.DefaultFormats
import org.json4s.jackson.Json

trait producer {
  def kafkaproducer(): Unit = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,Globals.configMap("kafkabootstrap").toString)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,Globals.configMap("kafkakeyser").toString)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,Globals.configMap("kafkavalueset").toString)

    val kaf_producer = new KafkaProducer[String, String](props)
    println(Globals.parsedapires)
   val cntry_stat_list  = Globals.parsedapires("countries_stat").asInstanceOf[List[Map[String,Any]]]
    for(country <- cntry_stat_list){
      val countryjson = Json(DefaultFormats).write(country)
      val record = new ProducerRecord(Globals.configMap("topic").toString, country("country_name").toString, countryjson)
      kaf_producer.send(record)
    }
    kaf_producer.flush()
    kaf_producer.close()
  }
}
