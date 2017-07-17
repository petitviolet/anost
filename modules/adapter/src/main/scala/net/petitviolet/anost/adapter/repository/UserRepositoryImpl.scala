package net.petitviolet.anost.adapter.repository

import net.petitviolet.anost.adapter.repository.dao.Users
import net.petitviolet.anost.domain.post.Post
import net.petitviolet.anost.domain.user.{ User, UserRepository }
import net.petitviolet.anost.support.{ Id, MixInLogger }
import net.petitviolet.anost.support.contracts.AppContext

import scala.concurrent.Future
import scalaz.Kleisli

object UserRepositoryImpl extends UserRepository with MixInLogger {
  override def resolve(implicit ctx: AppContext): Kleisli[Future, Id[User], User] = Kleisli { userId =>
    import ctx._
    ???
  }

  override def store(implicit ctx: AppContext): Kleisli[Future, User, User] = kleisliF { user =>
    import ctx._
    val id = Users.insert(user)
    logger.debug(s"insert $id")
    user
  }

  override def update(implicit ctx: AppContext): Kleisli[Future, User, User] = Kleisli { user =>
    import ctx._
    ???
  }
}

trait MixInUserRepository {
  val userRepository: UserRepository = UserRepositoryImpl
}

