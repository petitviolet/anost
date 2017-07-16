package net.petitviolet.anost.usecase

import net.petitviolet.anost.support.contracts.{ InputPort, UseCase }

trait AnostUseCase[I <: In, O <: Out] extends UseCase with InputPort[I, O, Context] {
}

trait In
trait Out
