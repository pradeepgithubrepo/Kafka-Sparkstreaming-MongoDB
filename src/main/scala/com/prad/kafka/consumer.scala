package com.prad.kafka
import com.prad.util.{Globals, utility}
import org.json4s._
import org.json4s.jackson.JsonMethods._
import org.apache.spark.streaming.kafka010.ConsumerStrategies
import org.apache.spark.streaming.kafka010.HasOffsetRanges
import org.apache.spark.streaming.kafka010.KafkaUtils
import org.apache.spark.streaming.kafka010.LocationStrategies
import org.apache.spark.sql.functions.{col, lit}
import com.prad.mongo.mongodbapi
import scala.collection.JavaConverters.mapAsJavaMapConverter
import java.util.ArrayList
import java.util.HashMap

object consumer extends utility {
  def main(args: Array[String]): Unit = {
    val kafkaParams = kafkaParam
    val sparkses = getsparksession
    val ssc = getssc
    val columnNames = Seq("cases","country_name","deaths")
    val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Array("corona-metric"), kafkaParams))

    stream.foreachRDD((rdd) =>
    {
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      if (!rdd.isEmpty) {
        println("rdd count"+rdd.count())
        val countryrdd = rdd.map(x => x.value)
        val countrydf = sparkses.read.json(countryrdd).select(columnNames.map(c => col(c)): _*)
                       .withColumn("repdate",lit((java.time.LocalDate.now).toString))
        println(countrydf.count())
//        countrydf.show(20, false)
        val countrydata = countrydf.collect().map(r => Map(countrydf.columns.zip(r.toSeq):_*))
        for (info <- countrydata){
          mongodbapi.countrymetricupdate(info("country_name").toString,info("cases").toString,info("deaths").toString)
        }
        offsetRanges.foreach(offset => {
          println(offset.topic, offset.partition, offset.fromOffset, offset.untilOffset)
          mongodbapi.offsetrangeupdate(offset.topic + offset.partition,(offset.fromOffset).toInt, (offset.untilOffset).toInt)
        })
      }
    })
    ssc.start()
    ssc.awaitTermination()
  }
}
