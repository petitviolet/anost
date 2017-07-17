package net.petitviolet.anost.adapter.support.json

import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.post.{ PostOutput, SavePostArg }
import net.petitviolet.anost.usecase.user.{ RegisterUserArgs, UserOutput }
import spray.json.{ DefaultJsonProtocol, RootJsonFormat }

trait JsonSupport extends DefaultJsonProtocol {
  implicit def idJson[A]: RootJsonFormat[Id[A]] = jsonFormat(Id.apply[A], "value")
  implicit val userArgJson: RootJsonFormat[RegisterUserArgs] = jsonFormat3(RegisterUserArgs.apply)
  implicit val userOutputJson: RootJsonFormat[UserOutput] = jsonFormat3(UserOutput.apply)
  implicit val savePostArgJson: RootJsonFormat[SavePostArg] = jsonFormat4(SavePostArg.apply)
  implicit val postOutputJson = jsonFormat5(PostOutput.apply)
}

