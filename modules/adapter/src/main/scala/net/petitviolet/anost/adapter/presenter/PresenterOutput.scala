package net.petitviolet.anost.adapter.presenter

import akka.http.scaladsl.model.{ ContentType, ContentTypes, HttpEntity, HttpHeader }
import akka.http.scaladsl.model.headers.HttpCookie
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

case class Content(value: String) extends AnyVal

object Content {
  val EMPTY = Content("")
}

