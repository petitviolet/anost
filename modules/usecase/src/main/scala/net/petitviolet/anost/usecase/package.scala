package net.petitviolet.anost

import net.petitviolet.anost.support.contracts.AppContext

import scala.concurrent.{ ExecutionContext, Future }
import scalaz._

package object usecase {
  def toFutureFailed[A](nelS: NonEmptyList[String]): Future[A] =
    Future.failed(new ValidationError(nelS))

  implicit def futureBind(implicit ctx: AppContext) = new Bind[Future] {
    import ctx.executionContext
    override def bind[A, B](fa: Future[A])(f: (A) => Future[B]): Future[B] = fa flatMap f
    override def map[A, B](fa: Future[A])(f: (A) => B): Future[B] = fa map f
  }

  implicit class FutureAsUnitOut[A](val f: Future[A]) extends AnyVal {
    def asUnit(implicit ctx: ExecutionContext): Future[UnitOut] = f map { _ => UnitOut }
  }
}
