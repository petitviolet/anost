package net.petitviolet.anost.usecase

import scalaz.NonEmptyList

case class ValidationError(reason: NonEmptyList[String]) extends Exception {
  override def getMessage: String = {
    val msgs = reason.stream.mkString(", ")
    s"ValidationError($msgs)"
  }
}

object ValidationError {
  def apply(msg: String): ValidationError = ValidationError(NonEmptyList(msg))
}
