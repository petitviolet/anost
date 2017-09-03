package net.petitviolet.anost.adapter.controller

import akka.event.Logging
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.AuthenticationResult
import net.petitviolet.anost.adapter.presenter.PresenterOutput
import net.petitviolet.anost.adapter.support.json.JsonSupport
import net.petitviolet.anost.adapter.support.{ ThreadHelper, UsesControllerConfig }
import net.petitviolet.anost.domain.user.AuthTokenValue
import net.petitviolet.anost.support.UsesLogger
import net.petitviolet.anost.usecase.ValidationError

import scala.concurrent.{ ExecutionContext, Future, TimeoutException }

/**
 * Controllerが[[Route]]をimplementしておくことで
 * `FooController ~ BarController`のようにobjectそのものをrouteとして扱えて見た目がすっきりする
 */
trait AnostController extends Route with UsesLogger with UsesControllerConfig
    with SprayJsonSupport with JsonSupport {

  private val TAG = getClass.getSimpleName
  protected def route: Route
  private lazy val _route: Route = route

  protected val withAuth = authenticateOrRejectWithChallenge[OAuth2BearerToken, AuthTokenValue] {
    case Some(OAuth2BearerToken(token)) =>
      Future.successful(AuthenticationResult.success(AuthTokenValue(token)))
    case _ =>
      Future.successful(AuthenticationResult.failWithChallenge(
        HttpChallenge("bearer", None, Map("error" -> "missing token"))
      ))
  }

  /**
   * 何かしらの理由により正常なレスポンスを返却できなかった場合に返却する代替レスポンス
   * - Akka-HTTPでタイムアウトされた
   * - URLが不正だった
   */
  protected def fallbackOutput: PresenterOutput = PresenterOutput.empty

  /**
   * タイムアウト時に返却するレスポンス
   */
  protected lazy val timeoutDirective: Directive0 =
    withRequestTimeoutResponse { req: HttpRequest =>
      aLogger.error(s"Timeout occurred. request: $req")
      toResponse(fallbackOutput)
    }

  /**
   * 何かしらの例外が発生した場合は各controllerでcatchしてfallbackを返却する
   */
  protected def exceptionHandler(implicit s: sourcecode.File) = ExceptionHandler {
    case t: Throwable =>
      //      logger.debugStackTrace(t)
      aLogger.warn(s"[${s.value}] exception-handler: $t")
      //      ok(fallbackOutput)
      failWith(t)
  }

  // directive合成を1回だけにするためvalで持つ
  private lazy val directives = handleExceptions(exceptionHandler) & timeoutDirective

  final override def apply(v1: RequestContext): Future[RouteResult] = {
    directives apply _route apply v1
  }

  protected implicit lazy val ec: ExecutionContext = {
    val threadNum = controllerConfig.parallelism

    if (threadNum == 0) {
      ThreadHelper.global
    } else {
      ThreadHelper.createContext(threadNum)
    }
  }

  protected def withSegments(path: String) = {
    pathPrefix(path / Segments) & get
  }

  protected def onCompleteResponse[O <: PresenterOutput](
    path: => String,
    resultF: Future[O],
    headers: => Seq[HttpHeader] = AnostController.defaultHeaders
  )(toResponse: => O => (Route)) = {
    import scala.util.{ Failure, Success }

    onComplete(resultF) {
      case Success(output) =>
        val resRoute: Route = toResponse(output)
        aLogger.debug(s"path: $path, output: $output")
        doResponse(output, resRoute, headers)
      case Failure(t) =>
        onFailure(path, t)
    }
  }

  private def doResponse[O <: PresenterOutput](
    output: O,
    responseRoute: Route,
    headers: Seq[HttpHeader]
  ): Route = {
    val addCookie: Directive0 = {
      val cookies = output.cookies
      if (cookies.nonEmpty) setCookie(cookies.head, cookies.tail: _*)
      else Directives.pass
    }

    (logRequestResult(TAG, Logging.DebugLevel) &
      addCookie &
      respondWithHeaders(headers: _*)) {
        responseRoute
      }
  }

  private def onFailure(path: String, t: Throwable): Route = {
    val logLevel: Logging.LogLevel = t match {
      case ng: ValidationError =>
        // validationエラーはdebug
        aLogger.debug(s"validation NG: ${ng.getMessage}")
        Logging.DebugLevel
      case te: TimeoutException =>
        // errorログ出さない系の例外
        // 特に対応が必要ないもの
        aLogger.warn(s"some error occurred: $te")
        Logging.WarningLevel
      case _ =>
        t.printStackTrace()
        aLogger.error(s"$TAG Controller error", t)
        Logging.ErrorLevel
    }

    logRequest(path, logLevel) {
      failWith(t)
    }
  }

  protected def toResponse[P <: PresenterOutput](output: P, statusCode: StatusCode = StatusCodes.OK): HttpResponse = {
    HttpResponse(statusCode, entity = output.asEntity, headers = output.optionalHeader)
  }

  protected def ok[P <: PresenterOutput](output: P): Route = {
    val res: HttpResponse = {
      toResponse(output)
    }
    complete(res)
  }
}

object AnostController {
  /**
   * 以下のように使うDirective
   * {{{
   * Controller.extract { (request, ip, userAgent) =>
   *   complete(???)
   * }
   * }}}
   */
  //  val extract: Directive[(HttpRequest, IP, UserAgent)] =
  //    extractRequest & IP.extractIp & UserAgent.extractUserAgent

  lazy val defaultHeaders: collection.immutable.Seq[HttpHeader] = {
    val server = Server.apply("anost")
    corsHeaders ++: server +: Nil
  }

  lazy val corsHeaders: collection.immutable.Seq[HttpHeader] = {
    val cors = `Access-Control-Allow-Origin`(HttpOriginRange.*)
    import HttpMethods._
    val allow = `Access-Control-Allow-Methods`(GET :: POST :: PUT :: DELETE :: HEAD :: OPTIONS :: Nil)
    val allowHeader = `Access-Control-Allow-Headers`("Origin, X-Requested-With, Content-Type, Accept, Authorization")

    cors +: allow +: allowHeader +: Nil
  }

  val NOT_FOUND: Route =
    respondWithHeaders(defaultHeaders: _*) {
      complete((StatusCodes.NotFound, ""))
    }

  val emptyOk: Route = complete(StatusCodes.OK)
}

