package net.petitviolet.anost.domain.comment

import net.petitviolet.anost.domain.post.Post
import net.petitviolet.anost.domain.support.Entity
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.domain.{ ::>, Validated, validation }
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.support.contracts.AppContext

import scala.concurrent.Future
import scalaz.Kleisli

case class Comment(
    id: Id[Comment],
    postId: Id[Post],
    userId: Id[User],
    sentence: Sentence
) extends Entity {
  override type ID = Id[Comment]
}

object Comment {
  import CommentSpecification._
  type CommentOps[A] = Kleisli[Future, CommentRepository, A]

  def create(postId: Id[Post], userId: Id[User], sentence: String): Validated[Comment] = {
    sentenceSpec(sentence).map { s => new Comment(Id.generate(), postId, userId, s) }
  }

  def findByPost(postId: Id[Post])(implicit ctx: AppContext): CommentOps[Seq[Comment]] = Kleisli {
    repo => repo.findAllByPostId.run(postId)
  }

  def store(comment: Comment)(implicit ctx: AppContext): CommentOps[Comment] = Kleisli {
    repo => repo.store.run(comment)
  }
}

private object CommentSpecification {
  val sentenceSpec: String ::> Sentence = validation[String, Sentence](
    _.nonEmpty, Sentence.apply
  )(_ => "Sentence must not empty")
}

case class Sentence(value: String) extends AnyVal
