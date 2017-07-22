package net.petitviolet.anost.usecase.user

import net.petitviolet.anost.domain.user.{ Email, Password, User, UsesUserRepository }
import net.petitviolet.anost.usecase._

import scala.concurrent.Future
import scalaz.Scalaz._

trait LoginUserUseCase extends AnostUseCase[LoginUserArgs, LoginResult]
    with UsesUserRepository {
  override protected def call(arg: In)(implicit ctx: Ctx): Future[Out] = {
    import ctx._
    userRepository.login.map { tokenOpt =>
      tokenOpt
        .map { _.tokenValue.value }
        .map { AuthTokenOutput.apply }
        .|> { LoginResult.apply }
    }.run(Email(arg.email), Password(arg.password))
  }
}

case class LoginUserArgs(email: String, password: String) extends In

case class LoginResult(tokenOpt: Option[AuthTokenOutput]) extends Out

trait UsesLoginUserUseCase {
  val loginUserUseCase: LoginUserUseCase
}
