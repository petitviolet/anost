package net.petitviolet.anost.adapter.repository

import net.petitviolet.anost.adapter.repository.dao.Comments
import net.petitviolet.anost.domain.comment.{ Comment, CommentRepository }
import net.petitviolet.anost.domain.post.Post
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import net.petitviolet.operator._
import net.petitviolet.anost.support.contracts.AppContext

import scala.concurrent.Future
import scalaz.Kleisli
import scalikejdbc._

object CommentRepositoryImpl extends CommentRepository {
  private lazy val c = Comments.defaultAlias

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

  override def delete(userId: Id[User])(implicit ctx: AppContext): Kleisli[Future, Id[Comment], Boolean] =
    kleisliF { commentId =>
      import ctx._
      Comments.deleteBy {
        sqls.eq(Comments.column.userId, userId.value)
          .and
          .eq(Comments.column.id, commentId.value)
      } |> { _.isPositive }
    }
}

trait MixInCommentRepository {
  val commentRepository: CommentRepository = CommentRepositoryImpl
}
