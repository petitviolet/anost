package net.petitviolet.anost.adapter.controller

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import net.petitviolet.anost.adapter.presenter.{ MixInUserPresenter, UsesUserPresenter }
import net.petitviolet.anost.adapter.repository.Database.Anost
import net.petitviolet.anost.adapter.repository.MixInUserRepository
import net.petitviolet.anost.support.{ MixInLogger, UsesLogger }
import net.petitviolet.anost.usecase.user.{ RegisterUserArgs, RegisterUserUseCase, UsesRegisterUserUseCase }

trait UserController extends AnostController with UsesLogger
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

object UserControllerImpl extends UserController with MixInLogger
    with MixInUserPresenter {
  override val registerUserUseCase: RegisterUserUseCase = new RegisterUserUseCase with MixInUserRepository
}

