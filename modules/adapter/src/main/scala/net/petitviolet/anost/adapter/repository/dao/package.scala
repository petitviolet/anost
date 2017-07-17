package net.petitviolet.anost.adapter.repository

import java.sql.ResultSet

import net.petitviolet.anost.support.Id
import org.joda.time.DateTime
import scalikejdbc._

package object dao {
  def now(): DateTime = DateTime.now()

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

}
