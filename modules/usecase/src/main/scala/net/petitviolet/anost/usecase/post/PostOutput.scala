package net.petitviolet.anost.usecase.post

import net.petitviolet.anost.domain.post.Post
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.Out

case class PostOutput(id: Id[Post], ownerId: Id[User], title: String, fileType: String, contents: String) extends Out

case class PostsOutput(values: Seq[PostOutput]) extends Out

object PostOutput {
  def fromModel(post: Post): PostOutput = PostOutput(
    post.id, post.ownerId, post.title.value, post.fileType.value, post.contents.value
  )
}

object PostsOutput {
  def fromModels(posts: Seq[Post]): PostsOutput = PostsOutput(
    posts map PostOutput.fromModel
  )
}

