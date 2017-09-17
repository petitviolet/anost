package net.petitviolet.anost.adapter.support.json

import net.petitviolet.anost.domain.user.{ AuthTokenValue, User }
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.comment._
import net.petitviolet.anost.usecase.post._
import net.petitviolet.anost.usecase.user._
import spray.json._

//noinspection TypeAnnotation
trait JsonSupport extends DefaultJsonProtocol {
  implicit def idJson[A]: RootJsonFormat[Id[A]] = new RootJsonFormat[Id[A]] {
    override def read(json: JsValue): Id[A] = json match {
      case JsString(id) => Id(id)
      case _ => throw DeserializationException("Id")
    }

    override def write(obj: Id[A]): JsValue = JsString(s"${obj.value}")
  }
  implicit lazy val authTokenArgJson = jsonFormat1(AuthTokenValue.apply)
  implicit lazy val userArgJson = jsonFormat3(RegisterUserArgs.apply)
  implicit lazy val userOutputJson = jsonFormat3(UserOutput.apply)
  implicit lazy val maybeOutputJson = jsonFormat1(MaybeUserOutput.apply)
  implicit lazy val savePostArgJson = jsonFormat3(SavePostArg.apply)
  implicit lazy val updatePostArgJson = jsonFormat4(UpdatePostArg.apply)
  implicit lazy val commentOwnerJson = jsonFormat2(CommentOwner.apply)
  implicit lazy val commentOutputJson = jsonFormat4(CommentOutput.apply)
  implicit lazy val postOutputJson = jsonFormat6(PostOutput.apply)
  implicit lazy val postElementJson = jsonFormat4(PostElement.apply)
  implicit lazy val postsOutputJson = jsonFormat1(PostsOutput.apply)
  implicit lazy val getPostArgJson = jsonFormat1(GetPostArg.apply)
  implicit lazy val loginUserArgJson = jsonFormat2(LoginUserArgs.apply)
  implicit lazy val authTokenJson = jsonFormat1(AuthTokenOutput.apply)
  implicit lazy val authTokenOpt = optionFormat[AuthTokenOutput]
  implicit lazy val loginResultItemJson = jsonFormat4(LoginResultItem.apply)
  implicit lazy val loginResultItemOpt = optionFormat[LoginResultItem]
  implicit lazy val loginResultJson = jsonFormat1(LoginResult.apply)
  implicit lazy val addCommentArgJson = jsonFormat2(AddCommentArg.apply)
  //  implicit val findPostArgJson =
}

