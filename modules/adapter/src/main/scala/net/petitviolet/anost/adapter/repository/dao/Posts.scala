package net.petitviolet.anost.adapter.repository.dao

import java.time.LocalDateTime

import net.petitviolet.anost.adapter.repository.AnostMapper
import net.petitviolet.anost.domain.comment.Comment
import net.petitviolet.anost.domain.post._
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import scalikejdbc._
import skinny.orm.Alias
import skinny.orm.feature.associations.HasManyAssociation

case class Posts(id: Id[Posts], ownerId: Id[Users],
  title: String, fileType: String, content: String, commentIds: Seq[Id[Comments]] = Nil,
  createdAt: LocalDateTime, updatedAt: LocalDateTime)

object Posts extends AnostMapper[Posts] {
  override def tableName: String = "posts"
  override def defaultAlias: Alias[Posts] = createAlias("p")

  private def p: Alias[Posts] = defaultAlias

  lazy val comments: HasManyAssociation[Posts] = hasMany[Comments](
    many = Comments -> Comments.defaultAlias,
    on = (p, c) => sqls.eq(p.id, c.postId),
    merge = (p, cs) => p.copy(commentIds = cs.map { _.id })
  )

  override def extract(rs: WrappedResultSet, rn: ResultName[Posts]): Posts = {
    Posts(
      Id(rs.get(rn.id)),
      Id(rs.get(rn.ownerId)),
      rs.get(rn.title),
      rs.get(rn.fileType),
      rs.get(rn.content),
      Nil,
      rs.get(rn.createdAt),
      rs.get(rn.updatedAt)
    )
  }

  def toModel(posts: Posts): Post = Post(
    posts.id.as[Post],
    posts.ownerId.as[User],
    Title(posts.title),
    FileType(posts.fileType),
    Contents(posts.content),
    posts.commentIds.map { _.as[Comment] }
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
    Posts.findAllBy {
      sqls.eq(p.ownerId, userId.value)
    }
  }

  def findAllLikeTitle(title: String)(implicit s: DBSession): Seq[Posts] = {
    Posts.findAllBy {
      sqls.like(p.title, s"%$title%")
    }
  }
}
