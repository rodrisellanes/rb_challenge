package util

import javax.inject.{Inject, Singleton}

import models.{Conditions, Forecast, Location}
import play.api.libs.json._
import play.api.libs.ws.{WSClient, WSRequest}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProxyWeather @Inject() (ws: WSClient)(implicit ec: ExecutionContext) {

  // Spitted URL Conditions (endpoint yahoo weather)
  val url = "https://query.yahooapis.com/v1/public/yql?q="
  val select_cond = "select item.condition from weather.forecast where woeid = "
  val select_forecast = "select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\""
  val format_cond = " and u='c'&format=json"
  val format_forecast = "\") and u='c'&format=json"


  val request: WSRequest = ws.url(url)

  def weatherByWoeid(woeid: Int): Future[JsValue] = {
    ws.url(url + select_cond + woeid + format_cond).get().map {
      response => response.json
    }
  }

  def forecastByCity(city: String): Future[JsValue] = {
    ws.url(url + select_forecast + city + format_forecast).get().map {
      response => response.json
    }
  }

  def cityForecast(city: String): Future[Seq[Forecast]] = {
    ws.url(url + select_forecast + city + format_forecast).get().map {
      response => {
        val forecast = (response.json \ "query" \ "results" \ "channel" \ "item" \\ "forecast").map(_.as[Seq[JsValue]])
        convertToForecast(forecast.flatten)
      }
    }
  }

  def dateForecast(forecasts: Seq[Forecast], date: Int): Forecast = {
    forecasts.find(f => f.date.startsWith(date.toString)).get
  }

  def convertToForecast(js: Seq[JsValue]): Seq[Forecast] = {
    js.map( f =>
      Forecast.apply(
        (f \ "code").as[String],
        (f \ "date").as[String],
        (f \ "day").as[String],
        (f \ "high").as[String],
        (f \ "low").as[String],
        (f \ "text").as[String]
      )
    )
  }

  def weatherConditionsByWoeid(woeid: Long): Future[Conditions] = {
    ws.url(url + select_cond + woeid + format_cond).get().map {
      response => Conditions.apply(
        (response.json \ "query" \ "results" \ "channel" \ "item" \ "condition" \ "date").as[String],
        (response.json \ "query" \ "results" \ "channel" \ "item" \ "condition" \ "temp").as[String].toDouble,
        (response.json \ "query" \ "results" \ "channel" \ "item" \ "condition" \ "text").as[String]
      )
    }
  }

  // TEST
  val fullUrl = "https://query.yahooapis.com/v1/public/yql?q=select%20item.condition%20from%20weather.forecast%20where%20woeid%20%3D%202487889&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
  val futureResult: Future[JsValue] = ws.url(fullUrl).get().map {
    response => response.json
  }

}
