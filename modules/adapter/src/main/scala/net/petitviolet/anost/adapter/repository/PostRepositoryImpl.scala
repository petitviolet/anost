package net.petitviolet.anost.adapter.repository

import net.petitviolet.anost.adapter.repository.dao.Posts
import net.petitviolet.anost.domain.post.{ Post, PostRepository }
import net.petitviolet.anost.support.contracts.AppContext
import net.petitviolet.anost.support.{ Id, MixInLogger }

import scala.concurrent.Future
import scalaz.Kleisli

object PostRepositoryImpl extends PostRepository with MixInLogger {
  override def resolve(implicit ctx: AppContext): Kleisli[Future, Id[Post], Post] = ???

  override def store(implicit ctx: AppContext): Kleisli[Future, Post, Post] = kleisliF { post =>
    import ctx._
    val id = Posts.insert(post)
    logger.debug(s"inserted $id")
    post
  }

  override def update(implicit ctx: AppContext): Kleisli[Future, Post, Post] = ???
}

trait MixInPostRepository {
  val postRepository: PostRepository = PostRepositoryImpl
}
