package net.petitviolet.anost.support.contracts

import scala.concurrent.Future

/**
 * @tparam C
 */
trait OutputPort[C <: Callback[_], Ctx <: AppContext] {

  type UseCaseExecutor = C => Unit

  type Rendered

  def execute(call: => UseCaseExecutor)(implicit ctx: Ctx): Future[Rendered]
}
