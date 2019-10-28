package config


case class SuperConfig(apiConfigs: Iterable[ApiConfig], sesameConfig: SesameConfig) {
  require(apiConfigs.nonEmpty, "Must specify at least one api config")
  require(apiConfigs.map(cfg => cfg.id).toSet.size == apiConfigs.size, "API ids must be unique")
}

object SuperConfig {
  val DefaultWhateverVal: Int = 1
}