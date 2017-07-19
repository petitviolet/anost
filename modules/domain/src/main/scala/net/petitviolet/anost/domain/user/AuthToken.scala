package net.petitviolet.anost.domain.user

import java.time.LocalDateTime

case class AuthToken(value: AuthTokenValue, expiresAt: LocalDateTime) {
  // if true, token had expired
  def isExpired: Boolean = {
    expiresAt.isAfter(LocalDateTime.now())
  }
}
case class AuthTokenValue(value: String) {
  require(value.length == 36, s"auth_token is invalid. value: $value is wrong.")
}

object AuthToken {
  private val EXPIRE_DAY = 7

  def generate(user: User): AuthToken = {
    val expiresAt = LocalDateTime.now().plusDays(EXPIRE_DAY)
    val h = hash(user.id.value)
    // last 4 digits
    val m = s"${System.currentTimeMillis()}".takeRight(4)
    AuthToken(
      AuthTokenValue(s"$h$m"), expiresAt
    )
  }

  // 32 digits
  private def hash(str: String): String = {
    java.security.MessageDigest.getInstance("MD5").digest(str.getBytes).map("%02x".format(_)).mkString
  }
}
