package net.petitviolet.anost.domain.user

import java.security.SecureRandom
import java.time.LocalDateTime

import scala.util.Random
import net.petitviolet.operator._

case class AuthToken(tokenValue: AuthTokenValue, expiresAt: LocalDateTime) {
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
    val h = hash(user.id.value).take(28)
    // last 4 digits
    val m = s"${System.currentTimeMillis()}".takeRight(4)
    val r = random().takeRight(4)
    AuthToken(
      AuthTokenValue(s"$r$h$m"), expiresAt
    )
  }

  private lazy val md = java.security.MessageDigest.getInstance("MD5")
  private lazy val rand = SecureRandom.getInstanceStrong |>> { _.setSeed(Random.nextLong()) }

  // 32 digits
  def hash(str: String): String = {
    md.digest(str.getBytes).map("%02x".format(_)).mkString.replace("-", "")
  }

  def random(): String = {
    rand.nextInt.toString
  }
}
