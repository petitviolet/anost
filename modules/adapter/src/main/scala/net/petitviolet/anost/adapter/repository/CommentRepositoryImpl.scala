package net.petitviolet.anost.adapter.repository

import net.petitviolet.anost.adapter.repository.dao.Comments
import net.petitviolet.anost.domain.comment.{ Comment, CommentRepository }
import net.petitviolet.anost.domain.post.Post
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.support.contracts.AppContext

import scala.concurrent.Future
import scalaz.Kleisli

object CommentRepositoryImpl extends CommentRepository {

  override def resolve(implicit ctx: AppContext): Kleisli[Future, Id[Comment], Option[Comment]] =
    kleisliF { id: Id[Comment] =>
      import ctx._
      Comments.findById(id.as[Comments]).map { Comments.toModel }
    }

  override def store(implicit ctx: AppContext): Kleisli[Future, Comment, Comment] =
    kleisliF { comment =>
      import ctx._
      Comments.insert(comment)
      comment
    }

  override def update(implicit ctx: AppContext): Kleisli[Future, Comment, Comment] =
    kleisliF { comment =>
      import ctx._
      val id = Comments.updateById(comment.id.as[Comments]).withAttributes(
        'sentence -> comment.sentence.value
      )
      comment
    }

  override def findAllByPostId(implicit ctx: AppContext): Kleisli[Future, Id[Post], Seq[Comment]] =
    kleisliF { postId =>
      import ctx._
      Comments.findAllByPost(postId).map { Comments.toModel }
    }
}

trait MixInCommentRepository {
  val commentRepository: CommentRepository = CommentRepositoryImpl
}
