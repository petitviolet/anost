package net.petitviolet.anost.adapter.support.json

import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.post._
import net.petitviolet.anost.usecase.user._
import spray.json._

trait JsonSupport extends DefaultJsonProtocol {
  implicit def idJson[A]: RootJsonFormat[Id[A]] = new RootJsonFormat[Id[A]] {
    override def read(json: JsValue): Id[A] = json match {
      case JsString(id) => Id(id)
      case _ => throw DeserializationException("Id")
    }

    override def write(obj: Id[A]): JsValue = JsString(s"${obj.value}")
  }
  implicit val userArgJson: RootJsonFormat[RegisterUserArgs] = jsonFormat3(RegisterUserArgs.apply)
  implicit val userOutputJson: RootJsonFormat[UserOutput] = jsonFormat3(UserOutput.apply)
  implicit val savePostArgJson: RootJsonFormat[SavePostArg] = jsonFormat4(SavePostArg.apply)
  implicit val postOutputJson = jsonFormat5(PostOutput.apply)
  implicit val postsOutputJson = jsonFormat1(PostsOutput.apply)
  implicit val getPostArgJson = jsonFormat1(GetPostArg.apply)
  //  implicit val findPostArgJson =
}

