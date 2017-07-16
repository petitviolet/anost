package net.petitviolet.anost.support.contracts

import scalikejdbc.DBSession

import scala.concurrent.ExecutionContext

/**
 * an input parameter to [[InputPort]]
 * should implement on Application
 */
trait AppContext {
  // 非同期処理のためのもの
  implicit val executionContext: ExecutionContext

  // IOのためのもの
  implicit val dbSession: DBSession
}
