package com.prad.util
import java.text.SimpleDateFormat
import java.util.Calendar
import scala.collection.mutable.ListBuffer

object Globals {
  var configMap = Map[String,Any]()
  var parsedapires = Map[String,Any]()
  val dateFormat = new SimpleDateFormat("YYYYMMdd")
  val dayFormat = new SimpleDateFormat("MMdd")
  val timeFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSSSSS")

  def getCurrentDate() =
  { dateFormat.format(Calendar.getInstance().getTime()) }

  def getCurrentTime() =
  { timeFormat.format(Calendar.getInstance().getTime()) }

  def getCurrentDay() =
  { dayFormat.format(Calendar.getInstance().getTime()) }
}
