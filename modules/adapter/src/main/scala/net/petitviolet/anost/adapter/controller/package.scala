package net.petitviolet.anost.adapter

import akka.http.scaladsl.unmarshalling.Unmarshaller
import net.petitviolet.anost.support.Id
import net.petitviolet.anost.usecase.Context
import scalikejdbc.DBSession

import scala.concurrent.{ ExecutionContext, Future }

package object controller {
  def createContext(session: DBSession)(implicit ec: ExecutionContext): Context = {
    Context()(ec, session)
  }

  def idUnmarshaller[A]: Unmarshaller[String, Id[A]] =
    Unmarshaller.apply { (_: ExecutionContext) => (s: String) => Future.successful(Id(s)) }

}
