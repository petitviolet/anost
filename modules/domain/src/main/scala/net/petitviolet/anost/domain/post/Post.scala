package net.petitviolet.anost.domain.post

import net.petitviolet.anost.domain._
import net.petitviolet.anost.domain.support.Entity
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.support.contracts.AppContext

import scala.concurrent.Future
import scalaz.Kleisli
import scalaz.Scalaz._

case class Post(
    id: Id[Post],
    ownerId: Id[User],
    title: Title,
    fileType: FileType,
    contents: Contents
) extends Entity {
  type ID = Id[Post]
}

case class Title(value: String) extends AnyVal
case class Contents(value: String) extends AnyVal
// enum?
case class FileType(value: String) extends AnyVal

object Post {
  import PostSpecification._
  type PostOps[A] = Kleisli[Future, PostRepository, A]

  def create(postId: Id[Post], ownerId: Id[User], title: String, fileType: String, contents: String): Validated[Post] =
    (titleSpec(title) |@|
      fileTypeSpec(fileType) |@|
      contentsSpec(contents))(Post(postId, ownerId, _, _, _))

  def create(ownerId: Id[User], title: String, fileType: String, contents: String): Validated[Post] =
    (titleSpec(title) |@|
      fileTypeSpec(fileType) |@|
      contentsSpec(contents))(Post(Id.generate(), ownerId, _, _, _))

  def save(post: Post)(implicit ctx: AppContext): PostOps[Post] = Kleisli {
    repo => repo.store.run(post)
  }

  def update(post: Post)(implicit ctx: AppContext): PostOps[Post] = Kleisli {
    repo => repo.update.run(post)
  }

  def ofUser(userId: Id[User])(implicit ctx: AppContext): PostOps[Seq[Post]] = Kleisli {
    repo => repo.findByUserId.run(userId)
  }

  def searchByTitle(title: Title)(implicit ctx: AppContext): PostOps[Seq[Post]] = Kleisli {
    repo => repo.findByTitle.run(title)
  }
}

private object PostSpecification {
  val titleSpec: String ::> Title = validation[String, Title](
    _.nonEmpty, Title.apply
  )(_ => "title should not be empty")

  val contentsSpec: String ::> Contents = validation[String, Contents](
    _.nonEmpty, Contents.apply
  )(_ => "contents should not be empty")

  val fileTypeSpec: String ::> FileType = validation[String, FileType](
    _.nonEmpty, FileType.apply
  )(_ => "fileType should not be empty")
}
