package net.petitviolet.anost.adapter.repository.dao

import java.time.LocalDateTime

import net.petitviolet.anost.adapter.repository.AnostMapper
import net.petitviolet.anost.domain.comment.{ Comment, Sentence }
import net.petitviolet.anost.domain.post._
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import scalikejdbc._
import skinny.orm.Alias

case class Comments(id: Id[Comments], postId: Id[Posts], userId: Id[Users],
  sentence: String,
  createdAt: LocalDateTime, updatedAt: LocalDateTime)

object Comments extends AnostMapper[Comments] {
  override def tableName: String = "comments"
  override def defaultAlias: Alias[Comments] = createAlias("c")

  private def p: Alias[Comments] = defaultAlias

  override def extract(rs: WrappedResultSet, rn: ResultName[Comments]): Comments = {
    Comments(
      Id(rs.get(rn.id)),
      Id(rs.get(rn.postId)),
      Id(rs.get(rn.userId)),
      rs.get(rn.sentence),
      rs.get(rn.createdAt),
      rs.get(rn.updatedAt)
    )
  }

  def toModel(c: Comments): Comment = Comment(
    c.id.as[Comment],
    c.postId.as[Post],
    c.userId.as[User],
    Sentence(c.sentence)
  )

  private[repository] def insert(comment: Comment)(implicit s: DBSession): Id[Comments] = {
    val dt = now()
    Comments.createWithAttributes(
      'id -> comment.id,
      'post_id -> comment.postId.value,
      'user_id -> comment.userId.value,
      'sentence -> comment.sentence.value,
      'created_at -> dt,
      'updated_at -> dt
    )
  }

  def findAllByPost(postId: Id[Post])(implicit s: DBSession): Seq[Comments] = {
    Comments.findAllBy {
      sqls.eq(p.postId, postId.value)
    }
  }

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
}
