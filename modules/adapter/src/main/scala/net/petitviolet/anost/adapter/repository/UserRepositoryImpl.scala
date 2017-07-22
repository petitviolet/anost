package net.petitviolet.anost.adapter.repository

import net.petitviolet.anost.adapter.repository.dao.{ AuthTokens, Users }
import net.petitviolet.anost.domain.user._
import net.petitviolet.anost.support.contracts.AppContext
import net.petitviolet.anost.support.{ Id, MixInLogger }
import net.petitviolet.operator._

import scala.concurrent.Future
import scalaz.Kleisli
import scalikejdbc._

object UserRepositoryImpl extends UserRepository with MixInLogger {
  private lazy val u = Users.defaultAlias
  private lazy val at = AuthTokens.defaultAlias

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

  override def findByToken(implicit ctx: AppContext): Kleisli[Future, AuthTokenValue, User] = kleisliF { token =>
    import ctx._
    Users.findByAutoToken(token).collect {
      case (users, tokens) // if expired, result will be None. else Some(user)
      if AuthTokens.toModel(tokens).isExpired => Users.toModel(users)
    } getOrElse { notFound(s"user not found for token($token)") }
  }

  override def generateToken(implicit ctx: AppContext): Kleisli[Future, User, AuthToken] = kleisliF { user =>

    AuthTokens.generateFor(user) |> AuthTokens.toModel
  }

  override def login()(implicit ctx: AppContext): Kleisli[Future, (Email, Password), Option[AuthToken]] = kleisliF {
    case (email, password) =>
      import ctx._
      Users.findBy {
        sqls.eq(u.email, email.value)
          .and
          .eq(u.password, password.value)
      }.map { users =>
        users |> Users.toModel |> AuthTokens.generateFor |> AuthTokens.toModel
      }
  }
}

trait MixInUserRepository {
  val userRepository: UserRepository = UserRepositoryImpl
}

