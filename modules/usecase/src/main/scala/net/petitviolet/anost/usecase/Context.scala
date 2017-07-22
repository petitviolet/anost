package net.petitviolet.anost.usecase

import net.petitviolet.anost.domain.user.AuthTokenValue
import net.petitviolet.anost.support.contracts.AppContext
import scalikejdbc.DBSession

import scala.concurrent.ExecutionContext

sealed class Context private[usecase] (private val ec: ExecutionContext, private val db: DBSession) extends AppContext {
  override implicit val executionContext: ExecutionContext = ec
  override implicit val dbSession: DBSession = db
}
final class LoginContext private[usecase] (
    private[usecase] val token: AuthTokenValue,
    private val ec: ExecutionContext,
    private val db: DBSession
) extends Context(ec, db) {
}

object Context {
  def apply()(implicit ec: ExecutionContext, db: DBSession): Context =
    new Context(ec, db)

  def login(token: AuthTokenValue)(implicit ec: ExecutionContext, db: DBSession): LoginContext =
    new LoginContext(token, ec, db)
}

