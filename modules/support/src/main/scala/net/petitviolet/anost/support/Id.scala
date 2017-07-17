package net.petitviolet.anost.support

import java.util.UUID

case class Id[+A](value: String) extends AnyVal {
  def as[B]: Id[B] = Id(value)
}

object Id {
  def generate[A](): Id[A] = Id(newValue())
  private def newValue(): String = UUID.randomUUID().toString
}
