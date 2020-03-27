package com.prad.kafka
import com.prad.util.{utility,Globals}
import java.util.Timer

object driver extends  utility with producer {
  def main(args: Array[String]): Unit = {
    //Read Global Config File
    Globals.configMap =parseconfigs
      //Invoke a timer every scheduled time
      val timer = new Timer()
      timer.schedule(function2TimerTask(timerTask),0, 50000)
      def timerTask()  = apirespose()
    println("before invoke")
    sleep(3000)
    println("after invoke")
    kafkaproducer()

  }
}
