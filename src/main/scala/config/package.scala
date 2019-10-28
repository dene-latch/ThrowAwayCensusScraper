import org.yaml.snakeyaml.Yaml

package object config {

  def parse(config: java.util.LinkedHashMap[String, java.util.LinkedHashMap[String, Object]]): SuperConfig =
    ConfigParser(config).superConfig

  def parse(yamlConfig: String): SuperConfig = {
    val yaml = new Yaml()
    val list = yaml.load(yamlConfig)
      .asInstanceOf[java.util.LinkedHashMap[String, java.util.LinkedHashMap[String, Object]]]
    this.parse(list)
  }

}