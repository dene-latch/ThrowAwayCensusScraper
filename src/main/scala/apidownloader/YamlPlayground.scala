package apidownloader

import java.io.{File, FileInputStream, InputStream, InputStreamReader}

import scala.jdk.CollectionConverters._

//jackson
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.ObjectMapper

//circe
import io.circe.yaml.parser

//snake
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

import scala.beans.BeanProperty
import scala.io.Source


class CensusApiConfig {
  @BeanProperty var version: String = _
  @BeanProperty var data_sources: String = _
  @BeanProperty var name: String = _

  @BeanProperty var column_list = new java.util.ArrayList[String]()
  @BeanProperty var filter_list = new java.util.ArrayList[String]()
  @BeanProperty var pk_cols = new java.util.ArrayList[String]()
  @BeanProperty var url: String = _
}

/**
 * With the Snakeyaml Constructor approach shown in the main method,
 * this class must have a no-args constructor.
 */
class EmailAccount {
  @BeanProperty var accountName: String = _
  @BeanProperty var username: String = _
  @BeanProperty var password: String = _
  @BeanProperty var mailbox: String = _
  @BeanProperty var imapServerUrl: String = _
  @BeanProperty var minutesBetweenChecks: Int = 0
  @BeanProperty var protocol: String = _
  @BeanProperty var usersOfInterest = new java.util.ArrayList[String]()

  override def toString: String = {
    s"${accountName}, ${username}, ${imapServerUrl}"
  }
}


object YamlPlayground {
  def whoami() : String = Thread.currentThread.getStackTrace()(2).getMethodName

  val email_acct_txt:String =
    """
    accountName: Ymail Account
    username: USERNAME
    password: PASSWORD
    mailbox: INBOX
    imapServerUrl: imap.mail.yahoo.com
    protocol: imaps
    minutesBetweenChecks: 1
    usersOfInterest: [barney, betty, wilma]
    """

  def YamlPlaySnake(yaml_path: String): Unit = {
    println("\n"+whoami)

    // try on email account
    val email_yaml = new Yaml(new Constructor(classOf[EmailAccount]))
    val e = email_yaml.load(email_acct_txt).asInstanceOf[EmailAccount]
    println(e)

    // try on yaml file
    val inFile: InputStream = new FileInputStream(new File(yaml_path))
    val yaml: Yaml = new Yaml
    val configData = yaml.load(inFile).asInstanceOf[java.util.Map[String, String]].asScala
    println(configData)
    println(configData("data_sources"))
  }

  def YamlPlayJackson(yaml_path: String): Unit = {
    println("\n"+whoami)
    val data =
      """
    {"some": "json data"}
    """
    val mapper = new ObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val my_map = mapper.readValue(data, classOf[Map[String, String]])
    println(my_map)
  }

  def YamlPlayConfigLoader(yaml_path: String): Unit = {
    println("\n"+whoami)
    val apis_yaml = new Yaml(new Constructor(classOf[CensusApiConfig]))
    val inFile: InputStream = new FileInputStream(new File(yaml_path))
    val config = apis_yaml.load[CensusApiConfig](inFile)
    println(config)
    println(config.data_sources)
  }

  def YamlRefActions(): Unit = {
    println("\n"+whoami)
    val testFiles = new File(getClass.getClassLoader.getResource("example-yamls").getPath)
      .listFiles.filter(_.getName endsWith ".yml").map {
      file => file.getName -> file.getName.replaceFirst("yml$", "json")
    }
    println(testFiles)

    testFiles.foreach{
      case (yamlFile, jsonFile) => {
        val jsonStream = getClass.getClassLoader.getResourceAsStream(s"example-yamls/$jsonFile")
        val json = Source.fromInputStream(jsonStream).mkString
        jsonStream.close()
        val parsedJson = io.circe.jawn.parse(json)
        def yamlStream = getClass.getClassLoader.getResourceAsStream(s"example-yamls/$yamlFile")
        def yamlReader = new InputStreamReader(yamlStream)
        val yaml = Source.fromInputStream(yamlStream).mkString
        val parsedYamlString = parser.parse(yaml)
        val parsedStreamString = parser.parseDocuments(yaml)
        val parsedYamlReader = parser.parse(yamlReader)
        val parsedStreamReader = parser.parseDocuments(yamlReader)
        assert(parsedJson == parsedYamlString)
        assert(parsedJson == parsedStreamString.head)
        assert(parsedJson == parsedYamlReader)
        assert(parsedJson == parsedStreamReader.head)

        println(parsedJson)
      }
    }
  }
}


object YamlPlayTalker extends App {
  val yaml_path:String = getClass.getClassLoader.getResource("example-yamls/census-api-configs.yaml").getPath
  YamlPlayground.YamlPlaySnake(yaml_path)
  YamlPlayground.YamlPlayJackson(yaml_path)
//  YamlPlayground.YamlPlayConfigLoader(yaml_path)
  YamlPlayground.YamlRefActions()
}