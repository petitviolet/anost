package net.petitviolet.anost.adapter.repository

import net.petitviolet.anost.adapter.repository.dao.{ Comments, Posts }
import net.petitviolet.anost.domain.post.{ Comment, Post, PostRepository, Title }
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.contracts.AppContext
import net.petitviolet.anost.support.{ Id, MixInLogger }
import skinny.orm.feature.CRUDFeatureWithId

import scala.concurrent.Future
import scalaz.Kleisli

object PostRepositoryImpl extends PostRepository with MixInLogger {
  private lazy val PostsWithComments: CRUDFeatureWithId[Id[Posts], Posts] =
    Posts.joins(Posts.comments)

  override def resolve(implicit ctx: AppContext): Kleisli[Future, Id[Post], Post] = kleisliF {
    id: Id[Post] =>
      import ctx._
      val postOpt = PostsWithComments.findById(id.as[Posts])
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

  override def addComment(implicit ctx: AppContext): Kleisli[Future, (Post, Comment), Post] = kleisliF {
    case (post, comment) =>
      import ctx._
      val _ = Comments.insert(comment)
      post.copy(comments = comment +: post.comments)
  }
}

trait MixInPostRepository {
  val postRepository: PostRepository = PostRepositoryImpl
}
