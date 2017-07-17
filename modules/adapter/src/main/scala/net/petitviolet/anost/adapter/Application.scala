package net.petitviolet.anost.adapter

import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.http.scaladsl.settings.{ ParserSettings, RoutingSettings }
import net.petitviolet.anost.adapter.controller.{ AnostController, UserControllerImpl }
import net.petitviolet.anost.adapter.repository.Database
import net.petitviolet.anost.adapter.support._
import net.petitviolet.anost.support.MixInLogger
import org.apache.commons.daemon.{ Daemon, DaemonContext }

import scala.concurrent.duration._
import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.io.StdIn
import scala.util.{ Failure, Success }

class Application() extends MixInLogger with UsesHttpConfig with MixInActorSystem {
  private implicit lazy val executionContext: ExecutionContext = ThreadHelper.createContext(1)
  private val TERMINATE_DURATION = 10.seconds

  private var bind: Future[ServerBinding] = _
  private lazy val (host, port) = (httpConfig.host, httpConfig.port)

  /**
   * HTTPサーバーの起動
   */
  def startServer(): Unit = {
    logger.info(s"application starting...")

    val http = Http()(system)
    bind = http.bindAndHandle(AppController, host, port)

    // DB初期化
    Database.setup()

    // 自分自身に一度リクエストを送信する
    //    checkTestRequest()

    logger.info(s"application started - $host:$port")
  }

  /**
   * サーバーを停止する
   */
  def stopServer(): Unit = {
    val f = bind flatMap { _.unbind() } flatMap { _ =>
      logger.info("application shutting down...")
      try {
        Database.shutDown()
      } catch {
        case t: Throwable =>
          logger.warn(s"Failed shut down database connections", t)
      }
      materializer.shutdown()
      system.terminate()
    }

    f.onComplete {
      case Success(x) =>
        logger.info(s"application shutting down complete: $x")
      case Failure(t) =>
        logger.error(s"application shutting down failed: $t", t)
    }

    val _ = Await.result(f, TERMINATE_DURATION)
  }

  /**
   * 一度自分自身にリクエストを送信する
   * object等の初期化を兼ねる
   */
  private def checkTestRequest(): Unit = {
    val req: HttpRequest = HttpRequest(HttpMethods.GET, uri = Uri(s"http://$host:$port/mb/health"))
    val resF: Future[HttpResponse] = Http().singleRequest(req)(materializer)

    val res = Await.result(resF, Duration.Inf)
    logger.info(s"check-self-request: $res")
  }
}

/**
 * デーモン起動用のclass
 */
class ApplicationDaemon() extends Application with Daemon {
  private var isStarted: Boolean = false

  override def init(context: DaemonContext): Unit = ()

  override def start(): Unit = {
    if (!isStarted) startServer()
    else sys.error(s"already started!")
  }

  override def stop(): Unit = {
    stopServer()
    isStarted = false
  }

  override def destroy(): Unit = logger.info("application destroyed")
}

/**
 * 起動用のMain class
 */
object ApplicationMain extends App {
  val app = new Application()
  app.startServer()
  val _ = StdIn.readLine()
  app.stopServer()
}

private object AppController extends AnostController with MixInLogger {
  import AnostController._
  override def configKey: String = "root"

  lazy val exceptionHandler = ExceptionHandler {
    case t: Throwable =>
      logger.debugStackTrace(t)
      logger.warn(s"exception handler: $t")

      respondWithHeaders(defaultHeaders: _*) {
        complete(StatusCodes.BadRequest)
      }
  }

  private val rejectionHandler =
    RejectionHandler.newBuilder
      .handleNotFound(NOT_FOUND)
      .handle {
        case r: MethodRejection =>
          logger.debug(s"rejection: $r")
          NOT_FOUND
        case other: Rejection =>
          logger.warn(s"rejection: $other")
          respondWithHeaders(defaultHeaders: _*) {
            complete(StatusCodes.BadRequest)
          }
      }
      .result()

  private val routingSettings = RoutingSettings(ConfigContainer.config)
  private val parserSettings = ParserSettings(ConfigContainer.config)

  protected lazy val route =
    Route.seal {
      UserControllerImpl
    }(routingSettings, parserSettings, rejectionHandler, exceptionHandler)
}

