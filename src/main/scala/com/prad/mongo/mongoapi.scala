package com.prad.mongo
import com.mongodb.spark.config.ReadConfig
import com.mongodb.spark.sql._
import com.prad.util.utility
object mongoapi extends  utility {
  def main(args: Array[String]): Unit = {
    val sparkses = getsparksession
    val readConfig = ReadConfig(Map("uri" -> "mongodb://127.0.0.1/", "database" -> "corono", "collection" -> "countrymetric"))
    val df = sparkses.read.mongo(readConfig)
//    val zipDf = sparkses.read.mongo(readConfig)
//    zipDf.printSchema()
//    zipDf.show(false)
  }
}
