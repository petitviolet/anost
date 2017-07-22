package net.petitviolet.anost.usecase.user

import net.petitviolet.anost.domain.Validated
import net.petitviolet.anost.domain.user.{ User, UsesUserRepository }
import net.petitviolet.anost.usecase._

import scala.concurrent.Future
import scalaz.Scalaz._

trait RegisterUserUseCase extends AnostUseCase[RegisterUserArgs, AuthTokenOutput]
    with UsesUserRepository {
  override protected def call(arg: In)(implicit ctx: Ctx): Future[Out] = {
    import ctx._
    arg.asModel().fold[Future[Out]](toFutureFailed, { user =>
      (for {
        _ <- userRepository.store
        token <- userRepository.generateToken
      } yield {
        AuthTokenOutput(token.tokenValue.value)
      }).run(user)
    })
  }
}

case class RegisterUserArgs(name: String, email: String, password: String) extends In {
  private[usecase] def asModel(): Validated[User] = User.create(name, email, password)
}

trait UsesRegisterUserUseCase {
  val registerUserUseCase: RegisterUserUseCase
}
