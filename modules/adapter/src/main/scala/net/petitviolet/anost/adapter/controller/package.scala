package net.petitviolet.anost.adapter

import akka.http.scaladsl.unmarshalling.Unmarshaller
import net.petitviolet.anost.domain.user.AuthTokenValue
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.{ Context, LoginContext }
import scalikejdbc.DBSession

import scala.concurrent.{ ExecutionContext, Future }

package object controller {
  def createContext(session: DBSession)(implicit ec: ExecutionContext): Context = {
    Context.apply()(ec, session)
  }
  def createContext(session: DBSession, token: AuthTokenValue)(implicit ec: ExecutionContext): LoginContext = {
    Context.login(token)(ec, session)
  }

  def idUnmarshaller[A]: Unmarshaller[String, Id[A]] =
    Unmarshaller.apply { (_: ExecutionContext) => (s: String) => Future.successful(Id(s)) }

}
