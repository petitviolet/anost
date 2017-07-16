package net.petitviolet.anost.support.contracts

import scala.util.{ Failure, Success }

/**
 * 入力ポート
 *
 * @tparam Arg
 * @tparam Result
 */
trait InputPort[Arg, Result, Context <: AppContext] { self: UseCase =>

  override final type In = Arg

  override final type Out = Result

  override final type Ctx = Context

  /**
   * 入力ポートへ入力値argを渡し非同期に実行する。
   * 呼び出し側は結果をcallback関数経由で取得する。
   *
   * @param arg
   * @param callback
   * @tparam T
   */
  final def execute[T <: Callback[Out]](arg: In)(callback: T)(implicit ctx: Ctx): Unit = {
    // ユースケースの実行

    self.call(arg).onComplete {
      case Success(r) =>
        callback.onSuccess(r)
      case Failure(t) =>
        callback.onFailure(t)
    }(ctx.executionContext)
  }
}