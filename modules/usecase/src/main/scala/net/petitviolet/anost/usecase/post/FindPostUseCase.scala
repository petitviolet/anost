package net.petitviolet.anost.usecase.post

import net.petitviolet.anost.domain.post.{ Post, Title, UsesPostRepository }
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.{ AnostUseCase, In, ValidationError }
import net.petitviolet.operator._

import scala.concurrent.Future

trait FindPostUseCase extends AnostUseCase[FindPostArg, PostsOutput]
    with UsesPostRepository {
  override protected def call(arg: In)(implicit ctx: Ctx): Future[Out] = {
    import ctx._
    (arg match {
      case PostOfUserArg(userId) => Post.ofUser(userId)
      case PostLikeTitleArg(titleQuery) => Post.searchByTitle(Title(titleQuery))
      case AllPost => Post.all()
    }) |> { _.run(postRepository) } map { PostsOutput.fromModels }

  }
}

sealed trait FindPostArg extends In
case class PostOfUserArg(userId: Id[User]) extends FindPostArg
case class PostLikeTitleArg(titleQuery: String) extends FindPostArg
case object AllPost extends FindPostArg

object FindPostArg {
  def byUser(str: String): FindPostArg = PostOfUserArg(Id(str))
  def byTitle(str: String): FindPostArg = PostLikeTitleArg(str)
  def apply(userOpt: Option[String], titleOpt: Option[String]): FindPostArg = {
    (userOpt, titleOpt) match {
      case (Some(userId), _) => byUser(userId)
      case (None, Some(title)) => byTitle(title)
      case _ => throw ValidationError("find post argument was not enough.")
    }
  }
}

trait UsesFindPostUseCase {
  val findPostUseCase: FindPostUseCase
}
