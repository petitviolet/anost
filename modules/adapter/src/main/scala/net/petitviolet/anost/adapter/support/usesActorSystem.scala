package net.petitviolet.anost.adapter.support

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

trait UsesActorSystem {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
}

trait MixInActorSystem {
  implicit lazy val system: ActorSystem = ActorSystemContainer.system
  implicit lazy val materializer: ActorMaterializer = ActorSystemContainer.materializer
}

/**
 * 初期化を一度だけするためobjectに持たせる
 */
private object ActorSystemContainer {
  lazy val system = ActorSystem("anost")
  lazy val materializer: ActorMaterializer = ActorMaterializer()(system)
}

