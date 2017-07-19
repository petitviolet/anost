package net.petitviolet.anost.usecase

import net.petitviolet.anost.domain.user.{ AuthTokenValue, User, UsesUserRepository }
import net.petitviolet.anost.support.UsesLogger
import net.petitviolet.anost.support.contracts.{ InputPort, UseCase }

import scala.concurrent.Future
import scalaz.Scalaz._

// usecases for all users
trait AnostUseCase[I <: In, O <: Out] extends UseCase with InputPort[I, O, Context] {
}

// usecases only for logged-in users
trait AnostAuthUseCase[I <: In, O <: Out] extends AnostUseCase[AuthIn[I], O] with UsesUserRepository with UsesLogger {

  final override protected def call(arg: In)(implicit ctx: Ctx): Future[Out] = {
    import ctx._
    (userRepository.findByToken andThenK { user => callWithUser(arg.in, user) }).run(arg.token)
  }

  protected def callWithUser(arg: I, user: User)(implicit ctx: Ctx): Future[Out]
}

trait In extends Any
trait Out extends Any

case class AuthIn[I <: In](token: AuthTokenValue, in: I) extends In
