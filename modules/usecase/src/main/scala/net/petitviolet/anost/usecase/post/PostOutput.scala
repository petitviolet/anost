package net.petitviolet.anost.usecase.post

import net.petitviolet.anost.domain.comment.Comment
import net.petitviolet.anost.domain.post.Post
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.Out
import net.petitviolet.anost.usecase.comment.CommentOutput

case class PostOutput(id: Id[Post], ownerId: Id[User],
  title: String, fileType: String,
  contents: String, comments: Seq[CommentOutput] = Nil) extends Out

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

  def fromModelWithComment(post: Post, commentAndUsers: Seq[(Comment, User)]): PostOutput = {
    val cs = commentAndUsers map { case (c, u) => CommentOutput.convert(c, u) }
    PostOutput(
      post.id, post.ownerId, post.title.value, post.fileType.value,
      post.contents.value, cs
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

