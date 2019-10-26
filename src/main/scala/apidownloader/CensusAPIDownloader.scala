package apidownloader

import CensusApiParamLookup._
import apidownloader.ConfigLoader._

import java.io.{File, FileInputStream, InputStream}
import scala.jdk.CollectionConverters._
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tototoshi.csv.CSVReader
import org.yaml.snakeyaml.Yaml

import java.io.File
import scala.beans.BeanProperty

class TempestConfig {
  @BeanProperty var reconst: String = ""
  @BeanProperty var name: String = ""
  @BeanProperty var another_name: String = ""
}



object CensusAPIDownloader {

  type m_type = Map[String, String]
  type it_type = Iterator[Map[String, String]]

  def source(url: String): String = scala.io.Source.fromURL(url).mkString
  def clean(s : String): String = s.replace("[", "").replace("]", "").replace(",\n", "\n")
  def sink(s: String): it_type = CSVReader.open(scala.io.Source.fromString(s)).iteratorWithHeaders
  def pipe(url: String): it_type =   (sink _ compose (clean _ compose source _ ))(url)
  def add_pk(pk_cols:List[String], m: m_type): m_type = m + ("primary_key" -> pk_cols.map(m(_)).mkString)
  def census_api_handler(pk_cols:List[String]): String => it_type = pipe( _ : String).map( add_pk( pk_cols, _ ) )

  def DownloadFromApi(url: String, census_api_name: String) {
    try {
      val content:Iterator[Map[String, String]] = {
        census_api_handler( pk_cols=api_params(census_api_name)("pk_cols"))(url)
      }
      content.foreach(println(_))
      content
    } catch {
      case ioe: java.io.IOException => ioe.printStackTrace()
      case ste: java.net.SocketTimeoutException => ste.printStackTrace()
    }
  }

  def ConstructCensusUrl(base_url: String, request_args: Map[String,String]): String =  {
    var final_url: String = base_url
    final_url += "?get=" + request_args("columns")
    final_url += "&" + request_args("filter")
    println(final_url)
    final_url
  }

  def yamlPlayground(): Unit = {
    // technique 1.
    val yaml_path = "/Users/denefarrell/src/github.com/dene-latch/ThrowAwayCensusScraper/whatever.yaml"
    val inFile: InputStream = new FileInputStream (new File (yaml_path))
    val yaml: Yaml = new Yaml
    val configData = yaml.load(inFile).asInstanceOf[java.util.Map[String, String]].asScala
    println(configData)
    println(configData("reconst"))
    // --- technique 2.
    val data = """
    {"some": "json data"}
    """
    val mapper = new ObjectMapper
    mapper.registerModule(DefaultScalaModule)
    val my_map = mapper.readValue(data, classOf[Map[String, String]])
    println(my_map)
    // -- technique 3.
    val config = loadConfig[String](yaml_path)
    println(config)
  }
}


object TalkToMe extends App {
  val census_api_name = "reconst" // New Residential Construction
  val base_url:String = base_urls(census_api_name)
  val request_args: Map[String, String] = List(
    ("columns",api_params(census_api_name)("column_list").mkString(",")),
    ("filter", api_params(census_api_name)("filter_list").mkString("&"))
  ).toMap

  val url = CensusAPIDownloader.ConstructCensusUrl(base_url, request_args)
  //  CensusAPIDownloader.DownloadFromApi(url, census_api_name=census_api_name)
  CensusAPIDownloader.yamlPlayground()
}