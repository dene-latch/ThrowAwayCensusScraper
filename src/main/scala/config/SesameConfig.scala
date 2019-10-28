package config

// SesameConfig (this file) and related code exist purely for demonstrative purposes
// remove this file from the codebase when hardening up for production

import config.SesameConfig._

case class SesameConfig(private val idRaw: String,
                        cookieMonster: Integer = DefaultCookieMonster,
                        bigBird: Integer = DefaultBigBird,
                        oscar: String = DefaultOscar,
                        count: String = DefaultCount
                    ) {
  val id = idRaw.trim()
  require(id.nonEmpty, "Committer id must have at least one character")
}

object SesameConfig {
  val DefaultWhateverValue = 1
  val DefaultCookieMonster = 10
  val DefaultBigBird = 100
  val DefaultOscar = "GarbageMan"
  val DefaultCount = "Chocolate"
}