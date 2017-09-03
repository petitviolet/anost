package net.petitviolet.anost.usecase.post

import net.petitviolet.anost.domain.post.Post
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import net.petitviolet.operator._
import net.petitviolet.anost.usecase.{ Out, ValidationError }

case class PostOutput(id: Id[Post], ownerId: Id[User],
  title: String, fileType: String,
  contents: String, comments: Seq[CommentOutput]) extends Out

case class CommentOutput(owner: CommentOwner, sentence: String)
case class CommentOwner(userId: Id[User], userName: String)

// eliminate contents and comments
case class PostElement(id: Id[Post], ownerId: Id[User], title: String, fileType: String) extends Out
case class PostsOutput(posts: Seq[PostElement]) extends Out

object PostOutput {
  def fromModel(post: Post): PostOutput = {
    PostOutput(
      post.id, post.ownerId, post.title.value, post.fileType.value,
      post.contents.value, Nil
    )
  }

  def fromModelWithComment(post: Post, users: Seq[User]): PostOutput = {
    val comments = post.comments.map { c =>
      users.find { _.id == c.userId }.getOrElse {
        // if user not found, its something wrong!
        throw ValidationError(s"wrong userId. postId: ${post.id}, userId: ${c.userId}")
      } |> { user =>
        CommentOwner(user.id, user.name.value)
      } |> { owner =>
        CommentOutput(owner, c.sentence.value)
      }
    }
    PostOutput(
      post.id, post.ownerId, post.title.value, post.fileType.value,
      post.contents.value, comments
    )
  }
}

object PostsOutput {
  private def toElement(post: Post): PostElement = PostElement(
    post.id, post.ownerId, post.title.value, post.fileType.value
  )

  def fromModels(posts: Seq[Post]): PostsOutput = PostsOutput(
    posts map toElement
  )
}

