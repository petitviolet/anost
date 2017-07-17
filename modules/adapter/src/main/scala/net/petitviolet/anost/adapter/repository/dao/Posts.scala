package net.petitviolet.anost.adapter.repository.dao

import net.petitviolet.anost.adapter.repository.AnostMapper
import net.petitviolet.anost.domain.post.Post
import net.petitviolet.anost.support.Id
import org.joda.time.DateTime
import scalikejdbc._
import skinny.orm.Alias

case class Posts(id: Id[Posts], ownerId: Id[Users], title: String, fileType: String, content: String,
  createdAt: DateTime, updatedAt: DateTime)

object Posts extends AnostMapper[Posts] {
  override def defaultAlias: Alias[Posts] = createAlias("p")

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

  private[repository] def insert(post: Post)(implicit s: DBSession): Id[Posts] = {
    val dt = now()
    createWithAttributes(
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