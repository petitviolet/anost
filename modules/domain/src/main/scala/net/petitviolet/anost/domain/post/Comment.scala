package net.petitviolet.anost.domain.post

import net.petitviolet.anost.domain.{ ::>, Validated, validation }
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import scalaz.Kleisli
import scalaz.Scalaz._

case class Comment(postId: Id[Post], userId: Id[User], sentence: Sentence)

object Comment {
  import CommentSpecification._

  def apply(postId: Id[Post], userId: Id[User], sentence: String): Validated[Comment] = {
    sentenceSpec(sentence).map { s => new Comment(postId, userId, s) }
  }
}

private object CommentSpecification {
  val sentenceSpec: String ::> Sentence = validation[String, Sentence](
    _.nonEmpty, Sentence.apply
  )(_ => "Sentence must not empty")
}

case class Sentence(value: String) extends AnyVal
