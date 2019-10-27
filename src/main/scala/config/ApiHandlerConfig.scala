package config


case class ApiHandlerConfig(private val dataSourceNamespaceeRaw: String,
                            apiConfigs: Iterable[ApiConfig],
                            sesameConfig: SesameConfig
                           )
                            {
  require(apiConfigs.nonEmpty, "Must specify at least one committer config")
  require(apiConfigs.map(cfg => cfg.id).toSet.size == apiConfigs.size, "Committer ids must be unique")

  val namespace = dataSourceNamespaceeRaw.trim()
  require(namespace.replaceAll("[^A-Za-z0-9-_]", "_") == namespace, "Api data source namespace must consist of alphanumeric characters, dashes (-), and underscores (_)")
}

object ApiHandlerConfig {
  val DefaultWhateverVal: Int = 1
}