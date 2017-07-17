package net.petitviolet.anost.adapter.repository.dao

import net.petitviolet.anost.adapter.repository.AnostMapper
import net.petitviolet.anost.domain.post._
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import org.joda.time.DateTime
import scalikejdbc._
import skinny.orm.Alias

case class Posts(id: Id[Posts], ownerId: Id[Users],
  title: String, fileType: String, content: String,
  createdAt: DateTime, updatedAt: DateTime)

object Posts extends AnostMapper[Posts] {
  override def defaultAlias: Alias[Posts] = createAlias("p")

  private def posts: Alias[Posts] = defaultAlias

  override def extract(rs: WrappedResultSet, rn: ResultName[Posts]): Posts = {
    Posts(
      Id(rs.get(rn.id)),
      Id(rs.get(rn.ownerId)),
      rs.get(rn.title),
      rs.get(rn.fileType),
      rs.get(rn.content),
      rs.get(rn.createdAt),
      rs.get(rn.updatedAt)
    )
  }

  def toModel(posts: Posts): Post = Post(
    posts.id.as[Post],
    posts.ownerId.as[User],
    Title(posts.title),
    FileType(posts.fileType),
    Contents(posts.content)
  )

  private[repository] def insert(post: Post)(implicit s: DBSession): Id[Posts] = {
    val dt = now()
    Posts.createWithAttributes(
      'id -> post.id,
      'owner_id -> post.ownerId.value,
      'title -> post.title.value,
      'file_type -> post.fileType.value,
      'content -> post.contents.value,
      'created_at -> dt,
      'updated_at -> dt
    )
  }

  def findAllByUserId(userId: Id[User])(implicit s: DBSession): Seq[Posts] = {
    Nil
    //    Posts.findAllBy {
    //      sqls.eq(Users.defaultAlias.id, userId)
    //    }
  }

  def findAllLikeTitle(title: String)(implicit s: DBSession): Seq[Posts] = {
    Nil
    //    Posts.findAllBy {
    //      sqls.like(Users.defaultAlias.id, title)
    //    }
  }
}
