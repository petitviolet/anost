package net.petitviolet.anost.adapter.repository

import net.petitviolet.anost.adapter.repository.dao.{ AuthTokens, Users }
import net.petitviolet.anost.domain.user.{ AuthToken, AuthTokenValue, User, UserRepository }
import net.petitviolet.anost.support.contracts.AppContext
import net.petitviolet.anost.support.{ Id, MixInLogger }
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

    ???
  }

  override def findByToken(implicit ctx: AppContext): Kleisli[Future, AuthTokenValue, Option[User]] = kleisliF { token =>
    import ctx._
    Users.findByAutoToken(token).fold(notFound(s"user not found for token($token)")) {
      case (users, tokens) =>
        // if expired, result will be None. else Some(user)
        if (AuthTokens.toModel(tokens).isExpired) Some(Users.toModel(users))
        else None
    }
  }

  override def generateToken(implicit ctx: AppContext): Kleisli[Future, User, AuthToken] = kleisliF { user =>

    AuthTokens.generateFor(user) |> AuthTokens.toModel
  }
}

trait MixInUserRepository {
  val userRepository: UserRepository = UserRepositoryImpl
}

