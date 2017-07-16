package net.petitviolet.anost.usecase

import net.petitviolet.anost.support.contracts.AppContext
import scalikejdbc.DBSession

import scala.concurrent.ExecutionContext

final class Context private (private val ec: ExecutionContext, private val db: DBSession) extends AppContext {
  override implicit val executionContext: ExecutionContext = ec
  override implicit val dbSession: DBSession = db
}

object Context {
  def apply()(implicit ec: ExecutionContext, db: DBSession): Context =
    new Context(ec, db)
}
