package net.petitviolet.anost.domain.support

import net.petitviolet.anost.support.contracts.AppContext

import scalaz.Kleisli
import scala.concurrent.Future

trait Repository[E <: Entity] {
  def resolve(implicit ctx: AppContext): Kleisli[Future, E#ID, E]
  def store(implicit ctx: AppContext): Kleisli[Future, E, E]
  def update(implicit ctx: AppContext): Kleisli[Future, E, E]
}
