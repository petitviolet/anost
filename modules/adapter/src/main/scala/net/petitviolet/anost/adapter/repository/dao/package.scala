package net.petitviolet.anost.adapter.repository

import java.sql.ResultSet
import java.time.{ LocalDateTime, ZoneId }

import net.petitviolet.anost.domain.user.AuthTokenValue
import net.petitviolet.anost.support.Id
import scalikejdbc._

package object dao {
  def now(): LocalDateTime = LocalDateTime.now(ZoneId.systemDefault())

  private object BinderBuilder {
    /**
     * extractの時に良い感じに型変換する
     */
    private def typeBinder[A, @specialized(Int, Long) B](implicit m: Manifest[B]): (B => A) => TypeBinder[A] = constructor => new TypeBinder[A] {
      override def apply(rs: ResultSet, columnIndex: Int): A =
        constructor(rs.getObject[B](columnIndex, m.runtimeClass.asInstanceOf[Class[B]]))

      override def apply(rs: ResultSet, columnLabel: String): A =
        constructor(rs.getObject[B](columnLabel, m.runtimeClass.asInstanceOf[Class[B]]))
    }

    def intTypeBinder[A]: ((Int) => A) => TypeBinder[A] = typeBinder[A, Int]
    def longTypeBinder[A]: ((Long) => A) => TypeBinder[A] = typeBinder[A, Long]
    def stringTypeBinder[A]: ((String) => A) => TypeBinder[A] = typeBinder[A, String]
  }
  import BinderBuilder._

  implicit def idB[A]: TypeBinder[Id[A]] = stringTypeBinder(Id.apply[A])

  implicit def idBinder[A]: Binders[Id[A]] = Binders.string.xmap[Id[A]](Id.apply[A], _.value)
  implicit val tokenBinder: Binders[AuthTokenValue] = Binders.string.xmap[AuthTokenValue](AuthTokenValue.apply, _.value)
  implicit val dateBinder: Binders[LocalDateTime] = Binders.javaTimeLocalDateTime
}
