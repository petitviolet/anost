package net.petitviolet.anost.usecase.post

import net.petitviolet.anost.domain.post.Post
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.Out

case class PostOutput(id: Id[Post], ownerId: Id[User],
  title: String, fileType: String,
  contents: String, comments: Seq[CommentOutput] = Nil) extends Out

case class CommentOutput(userId: Id[User], sentence: String)

// contentsが重いのでそこは削る
case class PostElement(id: Id[Post], ownerId: Id[User], title: String, fileType: String) extends Out
case class PostsOutput(posts: Seq[PostElement]) extends Out

object PostOutput {
  def fromModel(post: Post): PostOutput = PostOutput(
    post.id, post.ownerId, post.title.value, post.fileType.value,
    post.contents.value, post.comments.map { c => CommentOutput(c.userId, c.sentence.value) }
  )
}

object PostsOutput {
  private def toElement(post: Post): PostElement = PostElement(
    post.id, post.ownerId, post.title.value, post.fileType.value
  )

  def fromModels(posts: Seq[Post]): PostsOutput = PostsOutput(
    posts map toElement
  )
}

