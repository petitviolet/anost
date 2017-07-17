package net.petitviolet.anost.adapter

import net.petitviolet.anost.usecase.Context
import scalikejdbc.DBSession

import scala.concurrent.ExecutionContext

package object controller {
  def createContext(session: DBSession)(implicit ec: ExecutionContext): Context = {
    Context()(ec, session)
  }
}
