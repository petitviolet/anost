package net.petitviolet.anost.domain.support

trait Entity {
  type ID
  def id: ID
}
