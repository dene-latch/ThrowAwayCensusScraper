package config

case class ApiConfig(private val idRaw: String,
                     columns: Set[String],
                     filters: Set[String],
                     pk_cols: Set[String],
                     url: String,
                     customConfig: Map[_ <: Any, Any]
                    ) {
  val id:String = idRaw.trim()
  require(id.nonEmpty, "Committer id must have at least one character")
  require(columns.nonEmpty, "Must have at least one column")
}

object ApiConfig {
  val DefaultWhateverValue = 1
}