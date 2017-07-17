package net.petitviolet.anost.adapter.controller

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import net.petitviolet.anost.adapter.presenter.{ JsonOutput, MixInUserPresenter, UsesUserPresenter }
import net.petitviolet.anost.adapter.repository.Database.Anost
import net.petitviolet.anost.adapter.repository.MixInUserRepository
import net.petitviolet.anost.support.MixInLogger
import net.petitviolet.anost.usecase.user.{ RegisterUserArgs, RegisterUserUseCase, UsesRegisterUserUseCase }

trait UserController extends AnostController with MixInLogger
    with UsesRegisterUserUseCase with UsesUserPresenter {
  override def configKey: String = "user"
  override protected def route: Route = {
    pathPrefix("user") {
      (pathEnd & post & entity(as[RegisterUserArgs])) { arg =>
        implicit val ctx = createContext(Anost.writeSession)
        val out = userPresenter.execute(registerUserUseCase.execute(arg))
        onCompleteResponse("/user", out) { ok }
      }
    }
  }
}

object UserControllerImpl extends UserController with MixInUserPresenter
  with MixInRegisterUserUseCase

trait MixInRegisterUserUseCase {
  val registerUserUseCase: RegisterUserUseCase = RegisterUserUseCaseImpl
}

private object RegisterUserUseCaseImpl extends RegisterUserUseCase
  with MixInUserRepository
