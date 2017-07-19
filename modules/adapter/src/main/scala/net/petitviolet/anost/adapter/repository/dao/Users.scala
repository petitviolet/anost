package net.petitviolet.anost.adapter.repository.dao

import net.petitviolet.anost.adapter.repository.AnostMapper
import net.petitviolet.anost.domain.user._
import net.petitviolet.anost.support.Id
import org.joda.time.DateTime
import scalikejdbc._
import skinny.orm.Alias

case class Users(id: Id[Users], name: String, email: String, password: String,
  createdAt: DateTime, updatedAt: DateTime)

object Users extends AnostMapper[Users] {
  override def tableName: String = "users"
  override def defaultAlias: Alias[Users] = createAlias("u")
  private lazy val u = defaultAlias
  private lazy val at = AuthTokens.syntax("at")

  def toModel(u: Users): User = User(
    u.id.as[User],
    UserName(u.name),
    Email(u.email),
    Password(u.password)
  )

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

  private[repository] def insert(user: User)(implicit s: DBSession): Id[Users] = {
    val dateTime = now()
    val id = createWithAttributes(
      'id -> user.id,
      'name -> user.name.value,
      'email -> user.email.value,
      'password -> user.password.value,
      'created_at -> dateTime,
      'updated_at -> dateTime
    )
    id
  }

  private[repository] def findByAutoToken(token: AuthTokenValue)(implicit s: DBSession): Option[(Users, AuthTokens)] = {
    withSQL {
      select(u.result.*, at.result.*)
        .from(Users as u)
        .innerJoin(AuthTokens as at)
        .on(at.userId, u.id)
        .where(
          sqls.eq(at.token, token.value)
        )
    }.map { rs =>
      (
        Users.extract(rs, u.resultName),
        AuthTokens.extract(rs, at.resultName)
      )
    }.single.apply
  }
}
