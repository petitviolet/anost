package net.petitviolet.anost.adapter.repository

import net.petitviolet.anost.adapter.repository.dao.Posts
import net.petitviolet.anost.domain.post.{ Post, PostRepository, Title }
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.contracts.AppContext
import net.petitviolet.anost.support.{ Id, MixInLogger }

import scala.concurrent.Future
import scalaz.Kleisli

object PostRepositoryImpl extends PostRepository with MixInLogger {
  override def resolve(implicit ctx: AppContext): Kleisli[Future, Id[Post], Post] = kleisliF {
    id: Id[Post] =>
      import ctx._
      val postOpt = Posts.findById(id.as[Posts])
      postOpt.map { Posts.toModel } getOrElse { throw NotFound(s"post($id) does not exist") }
  }

  override def store(implicit ctx: AppContext): Kleisli[Future, Post, Post] = kleisliF { post =>
    import ctx._
    val id = Posts.insert(post)
    aLogger.debug(s"inserted $id")
    post
  }

  override def update(implicit ctx: AppContext): Kleisli[Future, Post, Post] = kleisliF { post =>
    import ctx._
    val id = Posts.updateById(post.id.as[Posts]).withAttributes(
      'title -> post.title.value,
      'file_type -> post.fileType.value,
      'content -> post.contents.value
    )
    post
  }

  override def findByUserId(implicit ctx: AppContext): Kleisli[Future, Id[User], Seq[Post]] = kleisliF {
    userId =>
      import ctx._
      val postss = Posts.findAllByUserId(userId)
      postss map { Posts.toModel }
  }

  override def findByTitle(implicit ctx: AppContext): Kleisli[Future, Title, Seq[Post]] = kleisliF {
    title =>
      import ctx._
      val postss = Posts.findAllLikeTitle(title.value)
      postss map { Posts.toModel }
  }
}

trait MixInPostRepository {
  val postRepository: PostRepository = PostRepositoryImpl
}
