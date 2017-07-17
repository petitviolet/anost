package net.petitviolet.anost.adapter.presenter

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.HttpCookie
import net.petitviolet.anost.adapter.support.json.JsonSupport
import net.petitviolet.anost.usecase.Out
import spray.json.{ DefaultJsonProtocol, JsValue }

import scala.collection.immutable

trait PresenterOutput extends Any {
  def cookies: Seq[HttpCookie]
  def contentType: ContentType
  def asEntity: HttpEntity.Strict
  def optionalHeader: immutable.Seq[HttpHeader] = Nil
}

object PresenterOutput {
  val empty: PresenterOutput = new EmptyOutput

  private class EmptyOutput extends PresenterOutput {
    val cookies: Seq[HttpCookie] = Nil
    val contentType: ContentType = ContentTypes.`text/html(UTF-8)`
    val asEntity: HttpEntity.Strict = ""
  }
}

case class HtmlOutput(cookies: Seq[HttpCookie], content: ResponseBody) extends PresenterOutput {
  override val contentType = ContentTypes.`text/html(UTF-8)`

  override def asEntity: HttpEntity.Strict =
    HttpEntity(contentType, content.value)
}

trait JsonPresenter[I <: Out] extends AnostPresenter[I, JsonOutput] with JsonSupport

case class JsonOutput(cookies: Seq[HttpCookie], jsValue: JsValue) extends PresenterOutput {
  override val contentType = ContentType(MediaTypes.`application/javascript`, HttpCharsets.`UTF-8`)

  override def asEntity: HttpEntity.Strict =
    HttpEntity(contentType, jsValue.toString())
}

case class NotFoundOutput(cookies: Seq[HttpCookie]) extends PresenterOutput {
  override val contentType = ContentTypes.`text/html(UTF-8)`
  override def asEntity: HttpEntity.Strict = HttpEntity(contentType, ResponseBody.EMPTY.value)
}

case class ResponseBody(value: String) extends AnyVal

object ResponseBody {
  val EMPTY = ResponseBody("")
}
