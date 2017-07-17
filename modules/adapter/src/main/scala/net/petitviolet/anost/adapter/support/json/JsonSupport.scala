package net.petitviolet.anost.adapter.support.json

import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.user.{ RegisterUserArgs, UserOutput }
import spray.json.{ DefaultJsonProtocol, RootJsonFormat }

trait JsonSupport extends DefaultJsonProtocol {
  implicit def idJson[A]: RootJsonFormat[Id[A]] = jsonFormat(Id.apply[A], "value")
  implicit val userArgJson: RootJsonFormat[RegisterUserArgs] = jsonFormat3(RegisterUserArgs.apply)
  implicit val userOutputJson: RootJsonFormat[UserOutput] = jsonFormat3(UserOutput.apply)
}

