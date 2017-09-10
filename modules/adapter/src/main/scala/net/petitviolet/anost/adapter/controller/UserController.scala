package net.petitviolet.anost.adapter.controller

import akka.http.scaladsl.model.Uri.Path
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import net.petitviolet.anost.adapter.presenter._
import net.petitviolet.anost.adapter.repository.Database.Anost
import net.petitviolet.anost.adapter.repository.MixInUserRepository
import net.petitviolet.anost.domain.user.UserRepository
import net.petitviolet.anost.support.{ MixInLogger, UsesLogger }
import net.petitviolet.anost.usecase.user._

trait UserController extends AnostController with UsesLogger
    with UsesGetUserUseCase with UsesUserPresenter with UsesMaybeUserPresenter
    with UsesRegisterUserUseCase with UsesAuthTokenPresenter
    with UsesLoginUserUseCase with UsesLoginUserPresenter {
  override def configKey: String = "user"
  override protected def route: Route = {
    pathPrefix("user") {
      (path(Segment) & pathEnd & get) { userId =>
        implicit val ctx = createContext(Anost.writeSession)
        val out = maybeUserPresenter.execute(getUserUseCase.execute(GetUserArgs(userId)))
        onCompleteResponse("/user", out) { ok }
      } ~
        (pathEnd & post & entity(as[RegisterUserArgs])) { arg =>
          implicit val ctx = createContext(Anost.writeSession)
          val out = authTokenPresenter.execute(registerUserUseCase.execute(arg))
          onCompleteResponse("/user", out) { ok }
        } ~
        (path("login") & get & pathEnd & parameters('email.as[String], 'password.as[String])).as(LoginUserArgs.apply) { arg =>
          implicit val ctx = createContext(Anost.readSession)
          val out = loginUserPresenter.execute(loginUserUseCase.execute(arg))
          onCompleteResponse("/user/login", out) { ok }
        }
    }
  }
}

object UserControllerImpl extends UserController with MixInLogger
    with MixInMaybeUserPresenter with MixInUserPresenter
    with MixInLoginUserPresenter with MixInAuthTokenPresenter {
  override val getUserUseCase: GetUserUseCase = new GetUserUseCase with MixInUserRepository
  override val registerUserUseCase: RegisterUserUseCase = new RegisterUserUseCase with MixInUserRepository
  override val loginUserUseCase: LoginUserUseCase = new LoginUserUseCase with MixInUserRepository
}

