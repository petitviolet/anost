package net.petitviolet.anost

import scala.concurrent.Future
import scalaz.NonEmptyList

package object usecase {
  def toFutureFailed[A](nelS: NonEmptyList[String]): Future[A] =
    Future.failed(new ValidationError(nelS))
}
