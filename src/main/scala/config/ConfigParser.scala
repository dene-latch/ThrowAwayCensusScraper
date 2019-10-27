package config

import java.util

import config._


import scala.jdk.CollectionConverters._
import ConfigParser._

import Transformers._


class ConfigParser(config: java.util.LinkedHashMap[String, java.util.LinkedHashMap[String, Object]]) {

  /**
   * Utility method to grab an entire section of config file as a Map[String, Object]
   * @param section
   * @return
   */
  private[this] def getSection(section: String): java.util.LinkedHashMap[String, Object] =
    if (config.containsKey(section)) {
      val result = config.get(section)
      if (result == null) {
        new util.LinkedHashMap[String, Object]()
      }
      else {
        result
      }
    }
    else {
      new util.LinkedHashMap[String, Object]()
    }

  /**
   * Utility method to get the value of a config key
   * @param section The config section
   * @param key The config key
   * @param transformer A transformation function to apply to the value
   */
  private[this] def get[T](section: String, key: String, transformer: Object => T): Option[T] = {
    val sectionMap = getSection(section)
    if (sectionMap.isEmpty)
      None
    else
    if (sectionMap.containsKey(key)) {
      val value = sectionMap.get(key)
      if (value == null)
        None
      else
        try { Some(transformer(value)) }
        catch {
          case ex: ClassCastException =>
            println(ex)
            throw new IllegalArgumentException(s"The the value '$value' for key '$key' in section '$section' has an unexpected type", ex)
          case ex: NumberFormatException =>
            throw new IllegalArgumentException(s"The the value '$value' for key '$key' in section '$section' cannot be formatted as a number", ex)

        }
    }
    else
      None
  }

  /**
   * Utility method to read a required config key. If the config key does not exist,
   * an IllegalArgumentException will be thrown
   * @param section The config section
   * @param key The config key
   * @param transformer A transformation function to apply to the value
   * @throws IllegalArgumentException
   */
  private[this] def getRequired[T](section: String, key: String, transformer: Object => T): T =
    get(section, key, transformer) match {
      case Some(value) => value
      case None        => throw new IllegalArgumentException(
        s"Failed to get a value for the required key: '$key' from the '$section' section")
    }

  /**
   * Utility method to read an optional config key and return the value if it exists, otherwise
   * return a default value
   * @param section The config section
   * @param key The config key
   * @param transformer Transformation function to apply on the value
   * @param default The default value to return if the config key is not defined
   */
  private[this] def getOptional[T](section: String, key: String, transformer: Object => T, default: T): T =
    get(section, key, transformer).getOrElse(default)

  /**
   * Utility method to read a config key and return its value as String
   * @param section The config section
   * @param key The config key
   */
  private[this] def getString(section: String, key: String): Option[String] =
    get(section, key, asString)

  /**
   * Utility method to read a required config key and return its value as String.
   * If the config key is absent, an IllegalArgumentException is raised.
   * @param section The config section
   * @param key The config key
   * @throws IllegalArgumentException
   */
  private[this] def getRequiredString(section: String, key: String): String =
    getRequired(section, key, asString)

  /**
   * Utility method to read an optional config key and return its value as String.
   * If the config key is absent, the specified default value will be returned.
   * @param section The config section
   * @param key The config key
   * @param default The default value to return
   */
  private[this] def getOptionalString(section: String, key: String, default: String): String =
    getString(section, key).getOrElse(default)

  /**
   * Utility method to read an optional key and return its Value or defaultValue if not found
   */
  private[this] def getFromMap[T](cfg: java.util.LinkedHashMap[String, Object],
                                  key: String,
                                  transformer: Object => T, defaultValue: T): T = {
    if (cfg.containsKey(key)) {
      val value = cfg.get(key)
      transformer(value)
    }
    else {
      defaultValue
    }
  }

  private[this] val dataSourceNamespace: String =
    getRequiredString(ApiConfigSectionName, "namespace")

  private[this] val apiConfigs: Seq[ApiConfig] = {
    import ApiConfig._

    for (
      apiId <- getRequired(ApiConfigSectionName, "api-id", asArrayList[String]).map(_.trim)
    ) yield {

      val name = getRequiredString(apiId, "name")

      val column_list: Set[String] =
        getRequired(apiId, "column_list", asArrayList[String]).toSet

      val filter_list: Set[String] =
        getRequired(apiId, "filter_list", asArrayList[String]).toSet

      val pk_cols: Set[String] =
        getRequired(apiId, "pk_cols", asArrayList[String]).toSet

      val url: String =
        getRequiredString(apiId, "url")

      val customApiConfig =
        getString(apiId, "customConfig") match {
          case Some(customConfigSectionName) => getSection(customConfigSectionName).asScala.map {
            case (k, v) => (k, asString(v))
          }.toMap
          case None => Map[String, String]()
        }


      ApiConfig(
        apiId,
        name,
        column_list,
        filter_list,
        pk_cols,
        url)
    }
  }


  private[this] val sesameConfig: SesameConfig = {
    import SesameConfig._

    if (getSection(OtherConfigSectionName).isEmpty) {
      SesameConfig(idRaw=getOptional(OtherConfigSectionName, "name", asString, "sesame"))
    } else {
      SesameConfig(
        getOptional(OtherConfigSectionName, "name", asString, "sesame"),
        getOptional(OtherConfigSectionName, "cookieMonster", asInteger, 1),
        getOptional(OtherConfigSectionName, "bigBird", asInteger, 1),
        getOptional(OtherConfigSectionName, "oscar", asString, "defaultOscar"),
        getOptional(OtherConfigSectionName, "count", asString, "defaultCount")
      )
    }
  }


  val apiHandlerConfig = ApiHandlerConfig(
    dataSourceNamespace,
    apiConfigs,
    sesameConfig)
}

object ConfigParser {

  val ApiConfigSectionName = "apis"
  val OtherConfigSectionName = "example-section"

  def apply(config: java.util.LinkedHashMap[String, java.util.LinkedHashMap[String, Object]]): ConfigParser =
    new ConfigParser(config)
}
