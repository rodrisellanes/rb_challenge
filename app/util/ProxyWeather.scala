package util

import javax.inject.{Inject, Singleton}

import models.Location
import play.api.libs.json._
import play.api.libs.ws.{WSClient, WSRequest}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProxyWeather @Inject() (ws: WSClient)(implicit ec: ExecutionContext) {

  // Split URL (endpoint yahoo weather)
  val url = "https://query.yahooapis.com/v1/public/yql?q="
  val select = "select item.condition from weather.forecast where woeid = "
  val format = " and u='c'&format=json"

  val request: WSRequest = ws.url(url)

  def getWeatherByWoeid(woeid: Int): Future[JsValue] = {
    ws.url(url + select + woeid + format).get().map {
      response => response.json
    }
  }

  // TEST
  val fullUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20woeid%20%3D%202487889&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
  val futureResult: Future[JsValue] = ws.url(fullUrl).get().map {
    response => response.json
  }

}
