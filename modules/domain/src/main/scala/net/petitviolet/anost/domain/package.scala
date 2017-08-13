package net.petitviolet.anost

import net.petitviolet.anost.support.contracts.AppContext

import scala.concurrent.Future
import scalaz._

package object domain {

  type Validated[A] = ValidationNel[String, A]
  type ::>[A, B] = A => Validated[B]

  def validation[A, B](cond: A => Boolean, t: => A => B)(f: => A => String): A ::> B = obj =>
    if (cond(obj)) success(t(obj)) else fail(f(obj))

  def fail[A](msg: String): Validated[A] = Validation.failure(NonEmptyList(msg))
  def success[A](obj: A): Validated[A] = Validation.success(obj)

  implicit def futureBind(implicit ctx: AppContext) = new Bind[Future] {
    import ctx.executionContext
    override def bind[A, B](fa: Future[A])(f: (A) => Future[B]): Future[B] = fa flatMap f
    override def map[A, B](fa: Future[A])(f: (A) => B): Future[B] = fa map f
  }
}
