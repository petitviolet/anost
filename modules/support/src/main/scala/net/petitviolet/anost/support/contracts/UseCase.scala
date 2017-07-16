package net.petitviolet.anost.support.contracts

import scala.concurrent.Future

/**
 * UseCase
 * asynchronous process with input `In` and output `Out`
 */
trait UseCase {

  type In

  type Out

  type Ctx <: AppContext

  protected def call(arg: In)(implicit ctx: Ctx): Future[Out]
}
