package net.petitviolet.anost.adapter.repository.dao

import net.petitviolet.anost.adapter.repository.AnostMapper
import net.petitviolet.anost.domain.user.User
import net.petitviolet.anost.support.Id
import org.joda.time.DateTime
import scalikejdbc._
import skinny.orm.Alias

case class Users(id: Id[Users], name: String, email: String, password: String,
  createdAt: DateTime, updatedAt: DateTime)

object Users extends AnostMapper[Users] {
  override def tableName: String = "users"
  override def defaultAlias: Alias[Users] = createAlias("u")

  override def extract(rs: WrappedResultSet, rn: ResultName[Users]): Users = {
    Users(
      Id(rs.get(rn.id)),
      rs.get(rn.name),
      rs.get(rn.email),
      rs.get(rn.password),
      rs.get(rn.createdAt),
      rs.get(rn.updatedAt)
    )
  }

  private[adapter] def insert(user: User)(implicit s: DBSession): Id[Users] = {
    val dateTime = now()
    createWithAttributes(
      'id -> user.id,
      'name -> user.name.value,
      'email -> user.email.value,
      'password -> user.password.value,
      'created_at -> dateTime,
      'updated_at -> dateTime
    )
  }
}
