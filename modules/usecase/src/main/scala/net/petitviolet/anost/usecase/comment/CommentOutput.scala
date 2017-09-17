package net.petitviolet.anost.usecase.comment

import net.petitviolet.anost.domain.comment.Comment
import net.petitviolet.anost.domain.post.Post
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.Out
import net.petitviolet.operator._

case class CommentOutput(id: Id[Comment], postId: Id[Post], owner: CommentOwner, sentence: String) extends Out
case class CommentOwner(userId: Id[User], userName: String)

object CommentOutput {
  def convert(comment: Comment, user: User): CommentOutput = {
    CommentOwner(user.id, user.name.value) |> { owner =>
      CommentOutput(comment.id, comment.postId, owner, comment.sentence.value)
    }
  }
}
