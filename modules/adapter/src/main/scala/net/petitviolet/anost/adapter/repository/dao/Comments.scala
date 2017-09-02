package net.petitviolet.anost.adapter.repository.dao

import java.time.LocalDateTime

import net.petitviolet.anost.adapter.repository.AnostMapper
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

  def toModel(comments: Comments): Comment = Comment(
    comments.postId.as[Post],
    comments.userId.as[User],
    Sentence(comments.sentence)
  )

  private[repository] def insert(comment: Comment)(implicit s: DBSession): Id[Comments] = {
    val dt = now()
    Comments.createWithAttributes(
      //      'id -> comment.id, // auto-increment
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

}
