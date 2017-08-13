package net.petitviolet.anost.adapter.repository.dao

import java.time.LocalDateTime

import net.petitviolet.anost.adapter.repository.AnostNoIdMapper
import net.petitviolet.anost.domain.user.{ AuthToken, AuthTokenValue, User }
import net.petitviolet.anost.support.{ AnostLogger, Id, MixInLogger }
import net.petitviolet.operator._
import scalikejdbc._
import skinny.orm.Alias

case class AuthTokens(token: String, userId: Id[Users], expiresAt: LocalDateTime, createdAt: LocalDateTime)

object AuthTokens extends AnostNoIdMapper[AuthTokens] with MixInLogger {
  override def tableName: String = "auth_tokens"
  override def defaultAlias: Alias[AuthTokens] = createAlias("at")
  private lazy val at = defaultAlias

  override def extract(rs: WrappedResultSet, n: ResultName[AuthTokens]): AuthTokens = {
    autoConstruct(rs, n)
  }

  private[repository] def generateFor(user: User): AuthTokens = {
    AuthToken.generate(user)
      .|> { token =>
        // always delete and create
        val count = deleteBy(sqls.eq(AuthTokens.column.userId, user.id.value))
        val created = createWithAttributes(
          'token -> token.tokenValue.value,
          'user_id -> user.id.value,
          'expires_at -> token.expiresAt
        )
        aLogger.debug(s"token: $token, count: $count, created: $created")
        fromModel(user.id.as[Users])(token)
      }
  }

  def fromModel(userId: Id[Users])(at: AuthToken): AuthTokens = AuthTokens(
    at.tokenValue.value, userId, at.expiresAt, now()
  )

  def toModel(at: AuthTokens): AuthToken = AuthToken(
    AuthTokenValue(at.token), at.expiresAt
  )
}
