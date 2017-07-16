package net.petitviolet.anost.usecase

import scalaz.NonEmptyList

class ValidationError(reason: NonEmptyList[String]) extends Exception {
  override def getMessage: String = {
    val msgs = reason.stream.mkString(", ")
    s"ValidationError($msgs)"
  }
}

