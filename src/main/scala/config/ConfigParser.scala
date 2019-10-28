package config

import java.util
import scala.jdk.CollectionConverters._
import ConfigParser._
import Transformers._


class ConfigParser(config: java.util.LinkedHashMap[String, java.util.LinkedHashMap[String, Object]]) {

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
            throw new IllegalArgumentException(s"The the value '$value' for key '$key' in section '$section' has an unexpected type", ex)
          case ex: NumberFormatException =>
            throw new IllegalArgumentException(s"The the value '$value' for key '$key' in section '$section' cannot be formatted as a number", ex)
        }
    }
    else
      None
  }

  private[this] def getRequired[T](section: String, key: String, transformer: Object => T): T =
    get(section, key, transformer) match {
      case Some(value) => value
      case None        => throw new IllegalArgumentException(
        s"Failed to get a value for the required key: '$key' from the '$section' section")
    }

  private[this] def getOptional[T](section: String, key: String, transformer: Object => T, default: T): T =
    get(section, key, transformer).getOrElse(default)

  private[this] def getString(section: String, key: String): Option[String] =
    get(section, key, asString)

  private[this] def getRequiredString(section: String, key: String): String =
    getRequired(section, key, asString)


  private[this] def getOptionalString(section: String, key: String, default: String): String =
    getString(section, key).getOrElse(default)


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

  private[this] val apiConfigs: Seq[ApiConfig] = {

    for (
      apiId <- getRequired(ApiConfigSectionName, "apis", asArrayList[String]).map(_.trim)
    ) yield {
      val column_list: Set[String] = getRequired(apiId, "column_list", asArrayList[String]).map(_.trim).toSet
      val filter_list: Set[String] = getRequired(apiId, "filter_list", asArrayList[String]).toSet
      val pk_cols: Set[String] = getRequired(apiId, "pk_cols", asArrayList[String]).toSet
      val url: String = getRequiredString(apiId, "url")
      val customConfig =
        getString(apiId, "customConfig") match {
          case Some(customConfigSectionName:String) => getSection(customConfigSectionName:String).asScala.map {
            case (k:String, v:String) => (k,v)
          }.toMap
          case None  => Map[Any, String]()
        }
      ApiConfig(apiId, column_list, filter_list, pk_cols, url, customConfig)
    }
  }

  private[this] val sesameConfig: SesameConfig = {
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

  val superConfig = SuperConfig(apiConfigs, sesameConfig)
}

object ConfigParser {
  val ApiConfigSectionName = "data_sources"
  val OtherConfigSectionName = "example-section"
  def apply(config: java.util.LinkedHashMap[String, java.util.LinkedHashMap[String, Object]]): ConfigParser =
    new ConfigParser(config)
}
