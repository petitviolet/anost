package net.petitviolet.anost.adapter.presenter

import net.petitviolet.anost.support.contracts.Presenter
import net.petitviolet.anost.usecase.{ Context, Out }

trait AnostPresenter[I <: Out, O <: PresenterOutput] extends Presenter[I, O, Context]

