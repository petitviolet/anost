package net.petitviolet.anost.usecase.user

import net.petitviolet.anost.domain.user.{ User, UsesUserRepository }
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase._

import scala.concurrent.Future
import scalaz.Scalaz.{ Id => _, _ }

trait GetUserUseCase extends AnostUseCase[GetUserArgs, MaybeUserOutput]
    with UsesUserRepository {
  override protected def call(arg: In)(implicit ctx: Ctx): Future[Out] = {
    import ctx._
    User.findById(Id(arg.id)).run(userRepository) map {
      userOpt =>
        userOpt.map { user =>
          Option(UserOutput(user.id, user.name.value, user.email.value))
        }.getOrElse { None } |> MaybeUserOutput.apply
    }
  }
}

case class GetUserArgs(id: String) extends AnyVal with In

trait UsesGetUserUseCase {
  val getUserUseCase: GetUserUseCase
}
