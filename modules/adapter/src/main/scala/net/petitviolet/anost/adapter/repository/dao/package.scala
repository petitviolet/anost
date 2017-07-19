package net.petitviolet.anost.adapter.repository

import java.sql.ResultSet
import java.time.{ LocalDateTime, ZoneId, ZoneOffset }
import java.util.Locale

import net.petitviolet.anost.domain.user.AuthTokenValue
import net.petitviolet.anost.support.Id
import org.joda.time.{ DateTime, DateTimeZone }
import scalikejdbc._

package object dao {
  def now(): DateTime = DateTime.now(DateTimeZone.getDefault)

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

    def intTypeBinder[A] = typeBinder[A, Int]
    def longTypeBinder[A] = typeBinder[A, Long]
    def stringTypeBinder[A] = typeBinder[A, String]
  }
  import BinderBuilder._

  implicit def idB[A] = stringTypeBinder(Id.apply[A])

  implicit def idBinder[A] = Binders.string.xmap[Id[A]](Id.apply[A], _.value)
  implicit val tokenBinder = Binders.string.xmap[AuthTokenValue](AuthTokenValue.apply, _.value)

  implicit class DateTimeConverter(val ldt: LocalDateTime) extends AnyVal {
    def asJoda: DateTime = {
      val instant = ldt.atZone(ZoneId.systemDefault()).toInstant
      new DateTime(instant.toEpochMilli)
    }
  }

  implicit class JodaConverter(val jdt: DateTime) extends AnyVal {
    def asLocalDateTime: LocalDateTime = {
      LocalDateTime.of(
        jdt.getYear,
        jdt.getMonthOfYear,
        jdt.getDayOfMonth,
        jdt.getHourOfDay,
        jdt.getMinuteOfHour,
        jdt.getSecondOfMinute,
        jdt.getMillisOfSecond * 1000000
      )
    }
  }
}
