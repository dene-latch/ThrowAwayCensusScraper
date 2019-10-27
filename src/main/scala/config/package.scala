//package com.box.castle

import config.ApiConfig
import org.yaml.snakeyaml.Yaml

package object config {

  def parse(config: java.util.LinkedHashMap[String, java.util.LinkedHashMap[String, Object]]): ApiHandlerConfig =
    ConfigParser(config).apiHandlerConfig

  def parse(yamlConfig: String): ApiHandlerConfig = {
    val yaml = new Yaml()
    val list = yaml.load(yamlConfig)
      .asInstanceOf[java.util.LinkedHashMap[String, java.util.LinkedHashMap[String, Object]]]
    this.parse(list)
  }

}