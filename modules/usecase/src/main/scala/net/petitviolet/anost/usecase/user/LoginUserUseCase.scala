package net.petitviolet.anost.usecase.user

import net.petitviolet.anost.domain.user.{ Email, Password, User, UsesUserRepository }
import net.petitviolet.anost.usecase._

import scala.concurrent.Future
import scalaz.Scalaz.{ ToIdOps => _, _ }
import net.petitviolet.operator._

trait LoginUserUseCase extends AnostUseCase[LoginUserArgs, LoginResult]
    with UsesUserRepository {
  override protected def call(arg: In)(implicit ctx: Ctx): Future[Out] = {
    import ctx._
    val x = userRepository.login.map(tokenOpt =>
      tokenOpt
        .map { _.tokenValue.value }
        .map { AuthTokenOutput.apply }
        .|> { LoginResult.apply })
    x.run(Email(arg.email), Password(arg.password))
  }
}

case class LoginUserArgs(email: String, password: String) extends In

case class LoginResult(tokenOpt: Option[AuthTokenOutput]) extends Out

trait UsesLoginUserUseCase {
  val loginUserUseCase: LoginUserUseCase
}
