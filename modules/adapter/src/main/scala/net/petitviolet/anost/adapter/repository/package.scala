package net.petitviolet.anost.adapter

import net.petitviolet.anost.support.contracts.AppContext

import scala.concurrent.{ ExecutionContext, Future }
import scalaz.Kleisli

package object repository {
  def kleisli[A, B](f: A => Future[B])(implicit ec: ExecutionContext): Kleisli[Future, A, B] =
    Kleisli { f }

  def kleisliF[A, B](f: A => B)(implicit ctx: AppContext): Kleisli[Future, A, B] =
    Kleisli { a => Future(f(a))(ctx.executionContext) }

  case class NotFound(msg: String) extends Exception(msg)
  def notFound(msg: String) = throw NotFound(msg)
}
