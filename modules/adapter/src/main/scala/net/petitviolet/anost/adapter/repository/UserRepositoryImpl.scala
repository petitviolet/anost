package net.petitviolet.anost.adapter.repository

import net.petitviolet.anost.adapter.repository.dao.{ AuthTokens, Users }
import net.petitviolet.anost.domain.user.{ AuthToken, User, UserRepository }
import net.petitviolet.anost.support.{ Id, MixInLogger }
import net.petitviolet.anost.support.contracts.AppContext
import net.petitviolet.operator._

import scala.concurrent.Future
import scalaz.Kleisli

object UserRepositoryImpl extends UserRepository with MixInLogger {
  override def resolve(implicit ctx: AppContext): Kleisli[Future, Id[User], User] = kleisliF { userId =>
    import ctx._
    Users.findById(userId.as[Users])
      .map { Users.toModel }
      .getOrElse { notFound(s"user not found by id($userId)") }
  }

  override def store(implicit ctx: AppContext): Kleisli[Future, User, User] = kleisliF { user =>
    import ctx._
    val id = Users.insert(user)
    aLogger.debug(s"insert $id")
    user
  }

  override def update(implicit ctx: AppContext): Kleisli[Future, User, User] = Kleisli { user =>
    import ctx._
    ???
  }

  override def findByToken(implicit ctx: AppContext): Kleisli[Future, AuthToken, Option[User]] = kleisliF { token =>
    import ctx._
    Users.findByAutoToken(token) map Users.toModel
  }

  override def generateToken(implicit ctx: AppContext): Kleisli[Future, User, AuthToken] = kleisliF { user =>
    import ctx._
    AuthTokens.generateFor(user) |> AuthTokens.toModel
  }
}

trait MixInUserRepository {
  val userRepository: UserRepository = UserRepositoryImpl
}

