package net.petitviolet.anost.domain.post

import net.petitviolet.anost.domain.support.Repository

trait PostRepository extends Repository[Post] {

}

trait UsesPostRepository {
  val postRepository: PostRepository
}
