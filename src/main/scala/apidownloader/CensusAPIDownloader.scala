package apidownloader

import CensusApiParamLookup._
import com.github.tototoshi.csv.CSVReader

object CensusAPIDownloader {

  type m_type = Map[String, String]
  type it_type = Iterator[Map[String, String]]

  def source(url: String): String = scala.io.Source.fromURL(url).mkString
  def clean(s : String): String = s.replace("[", "").replace("]", "").replace(",\n", "\n")
  def sink(s: String): it_type = CSVReader.open(scala.io.Source.fromString(s)).iteratorWithHeaders
  def enrich(pk_cols:List[String], m: m_type): m_type = m + ("primary_key" -> pk_cols.map(m(_)).mkString)
  def pipe(url: String, enricher: m_type=>m_type): it_type =   (sink _ compose (clean _ compose source _ ))(url).map( enricher )
  def census_api_handler(pk_cols:List[String]): String => it_type = pipe( _ : String, enrich( pk_cols, _ ))

  //  def pipe(url: String): it_type =   (sink _ compose (clean _ compose source _ ))(url)
  //  def add_pk(pk_cols:List[String], m: m_type): m_type = m + ("primary_key" -> pk_cols.map(m(_)).mkString)
  //  def census_api_handler(pk_cols:List[String]): String => it_type = pipe( _ : String).map( add_pk( pk_cols, _ ) )


  def DownloadFromApi(url: String, census_api_name: String): Any = {
    try {
      val content:Iterator[Map[String, String]] = {
        census_api_handler( pk_cols=api_params(census_api_name)("pk_cols"))(url)
      }
      println(s"content.size: ${content.size}")
      content
    } catch {
      case e: Throwable => e.printStackTrace
    }
  }

  def ConstructCensusUrl(base_url: String, request_args: Map[String,String]): String =  {
    val final_url: String = base_url +
      "?get=" + request_args("columns") +
      "&" + request_args("filter")
    println(final_url)
    final_url
  }
}


object CensusApiTalker extends App {
  val census_api_name = "reconst" // New Residential Construction
  val base_url:String = base_urls(census_api_name)
  val request_args: Map[String, String] = List(
    ("columns",api_params(census_api_name)("column_list").mkString(",")),
    ("filter", api_params(census_api_name)("filter_list").mkString("&"))
  ).toMap

  val url = CensusAPIDownloader.ConstructCensusUrl(base_url, request_args)
  CensusAPIDownloader.DownloadFromApi(url, census_api_name=census_api_name)
}