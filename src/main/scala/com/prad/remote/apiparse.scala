package com.prad.remote
import com.prad.util.{Globals,utility}
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClientBuilder
import scala.util.parsing.json._
import java.util.concurrent._
import org.json4s._
import org.json4s.jackson.JsonMethods._

trait apiparse {
  def apirespose() = {
    val httpGet = new HttpGet(Globals.configMap("apiurl").toString)
    // set the desired header values
    httpGet.setHeader("x-rapidapi-host", Globals.configMap("apihost").toString)
    httpGet.setHeader("x-rapidapi-key", Globals.configMap("apikey").toString)

    // execute the request
    val client = HttpClientBuilder.create.build
    val httpResponse = client.execute(httpGet)
    val entity = httpResponse.getEntity()
    var content = ""
    if (entity != null) {
      val inputStream = entity.getContent()
      content = scala.io.Source.fromInputStream(inputStream).getLines.mkString
      inputStream.close
    }
    println("Coming inside apires")
    println(content)
    client.close()
    implicit val formats = org.json4s.DefaultFormats
    Globals.parsedapires = parse(content).extract[Map[String, Any]]
  }
}
