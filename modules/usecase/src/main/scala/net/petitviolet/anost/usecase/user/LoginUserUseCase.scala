package net.petitviolet.anost.usecase.user

import net.petitviolet.anost.domain.user.{ Email, Password, User, UsesUserRepository }
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase._

import scala.concurrent.Future
import scalaz.Scalaz.{ Id => _, _ }

trait LoginUserUseCase extends AnostUseCase[LoginUserArgs, LoginResult]
    with UsesUserRepository {
  override protected def call(arg: In)(implicit ctx: Ctx): Future[Out] = {
    import ctx._
    userRepository.login.map {
      _.map {
        case (user, token) =>
          LoginResultItem(
            user.id, user.name.value, user.email.value,
            AuthTokenOutput(token.tokenValue.value)
          )
      }
        .|> { LoginResult.apply }
    }.run(arg.asModel)
  }
}

case class LoginUserArgs(email: String, password: String) extends In {
  private[user] def asModel: (Email, Password) = (Email(email), Password(password))
}

case class LoginResultItem(id: Id[User], name: String, email: String, token: AuthTokenOutput) extends Out
case class LoginResult(itemOpt: Option[LoginResultItem]) extends Out

trait UsesLoginUserUseCase {
  val loginUserUseCase: LoginUserUseCase
}
