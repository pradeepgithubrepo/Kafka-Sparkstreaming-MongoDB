package com.prad.util
import com.prad.kafka._
import org.apache.spark.sql.SparkSession
import java.util.TimerTask

import com.prad.remote.apiparse
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.spark.streaming.{Seconds, StreamingContext}

trait utility extends producer with apiparse {

def getsparksession : SparkSession = SparkSession.builder().appName("kafka stream").master("local").getOrCreate()
def getssc :StreamingContext = new StreamingContext(getsparksession.sparkContext, Seconds(60))

  def function2TimerTask(f: () => Unit): TimerTask = {
    return new TimerTask {
      def run() = f()
    }
  }

  def sleep(time: Long) { Thread.sleep(time) }

  def parseconfigs: Map[String,Any]={
   var sparkses = getsparksession
    val configdf= sparkses.read.json("data/__default__/user/current/connect.json")
    println(configdf)
    return configdf.collect.map(r => Map(configdf.columns.zip(r.toSeq):_*)).head
  }

  def kafkaParam() : Map[String,Object] = {
    val kafkaParams = Map(
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> "127.0.0.1:9092",
      "key.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      "value.deserializer" -> "org.apache.kafka.common.serialization.StringDeserializer",
      ConsumerConfig.GROUP_ID_CONFIG -> "kafkacorona",
      ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> "earliest")
    kafkaParams
  }
}
