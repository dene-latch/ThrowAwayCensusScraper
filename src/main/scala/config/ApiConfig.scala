package config

import config.ApiConfig._

case class ApiConfig(private val idRaw: String,
                     name: String,
                     columns: Set[String],
                     filters: Set[String],
                     pk_cols: Set[String],
                     url: String
                    ) {
  val id = idRaw.trim()
  require(id.nonEmpty, "Committer id must have at least one character")
  require(name.nonEmpty, "api name must have at least one character")
  require(columns.nonEmpty, "Must have at least one column")
}

object ApiConfig {
  val DefaultWhateverValue = 1
}