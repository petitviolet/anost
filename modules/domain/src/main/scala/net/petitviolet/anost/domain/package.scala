package net.petitviolet.anost

import scalaz._

package object domain {

  type Validated[A] = ValidationNel[String, A]
  type ::>[A, B] = A => Validated[B]

  def validation[A, B](cond: A => Boolean, t: => A => B)(f: => A => String): A ::> B = obj =>
    if (cond(obj)) success(t(obj)) else fail(f(obj))

  def fail[A](msg: String): Validated[A] = Validation.failure(NonEmptyList(msg))
  def success[A](obj: A): Validated[A] = Validation.success(obj)

}
