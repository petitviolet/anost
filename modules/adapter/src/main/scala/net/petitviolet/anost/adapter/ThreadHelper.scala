package net.petitviolet.anost.adapter

import java.util.concurrent.{ ExecutorService, ForkJoinPool }

import scala.concurrent.ExecutionContext

object ThreadHelper {
  def createExecutor(num: Int): ExecutorService = new ForkJoinPool(num)
  def createContext(num: Int): ExecutionContext = ExecutionContext.fromExecutorService(createExecutor(num))

  def global: ExecutionContext = ExecutionContext.Implicits.global
}

