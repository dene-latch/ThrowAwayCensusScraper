package apidownloader


import com.github.tototoshi.csv.CSVReader


object CensusSF1ApiManager {
  def source(url: String): String = scala.io.Source.fromURL(url).mkString

  def GetCensusVals(source_string: String ): Either[io.circe.Error, List[String]] = {
    val response = io.circe.parser.decode[List[List[String]]](source_string)
    response.map(x=>x.map(y=>y.last).tail)
  }

  def getCounties(states: List[String]): Seq[List[Map[String, String]]] = {
    println(s"getCounties states: ${states}")
    states.flatMap(
      state_id => {
        lazy val state_url = s"https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:${state_id}"
        lazy val all_counties_ = (GetCensusVals _ compose source _) (state_url)
        lazy val all_counties = all_counties_.getOrElse(None).iterator.to(Seq).tail.map(x=>List(x.last))
        all_counties.map(x => x.map(y => Map("state_id" -> state_id.toString, "county_id" -> y.toString)))
      }
    )
  }

  def getBlocks(counties: Seq[List[Map[String, String]]]): Seq[List[Iterator[Map[String, String]]]] = {
    counties.map(byState =>
      byState.map(
        stateCounty => {
          lazy val state_id:String = stateCounty("state_id")
          lazy val county_id:String = stateCounty("county_id")
          lazy val county_url = s"https://api.census.gov/data/2010/dec/sf1?get=NAME,GEO_ID,BLKGRP,BLOCK,CBSA,CONCIT,COUNTY,COUSUB,CSA,DIVISION,H001001,H002002&for=block:*&in=state:${state_id}%20county:${county_id}"
          println(s"stateCounty: ${stateCounty}")
          println(county_url)
          val parsed_blocks_prep_ = (GetCensusVals _ compose source _) (county_url)
          val parsed_blocks_prep = parsed_blocks_prep_.getOrElse(None).iterator.to(Seq).mkString("\n")
          val parsed_blocks = CSVReader.open(scala.io.Source.fromString(parsed_blocks_prep)).iteratorWithHeaders
          parsed_blocks
        }
      )
    )
  }

  def CensusNestedData(start_url: String): Iterator[Map[String, String]] = {
    lazy val parsed_states = (GetCensusVals _ compose source _) (start_url)
    lazy val parsed_blocks = parsed_states.map(getCounties).map(getBlocks)
    parsed_blocks.fold(
      e => {
        throw e
      },
      x => { x.iterator.flatMap( y => y) }
    ).flatten
  }


  // doing it w/o functional programming
  def gimmeVals(s: String): Seq[List[String]] = io.circe.parser.decode[List[List[String]]](s).getOrElse(None).iterator.to(Seq).tail

  def FullCensusDataLoops(): Unit = {
    val base_url = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"
    val parsed_states = (gimmeVals _ compose source _ )(base_url)
    for (c <- parsed_states) {
      println(c)
      val state_url = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:"
      val parsed_counties = (gimmeVals _ compose source _ )(state_url+c.last)
      for (cc <- parsed_counties) {
        println(cc)
        val state_id = c.last
        val county_id = cc.last
        val county_url = s"https://api.census.gov/data/2010/dec/sf1?get=NAME,GEO_ID,BLKGRP,BLOCK,CBSA,CONCIT,COUNTY,COUSUB,CSA,DIVISION,H001001,H002002&for=block:*&in=state:${state_id}%20county:${county_id}"
        println(county_url)
        val county_string_source = scala.io.Source.fromURL(county_url).mkString
        val county_vals = io.circe.parser.decode[List[List[String]]](county_string_source).getOrElse(None).iterator.to(Seq)
        println(county_vals)
        county_vals.iterator
      }
    }
  }
}

object Main extends App {
//  CensusSF1ApiManager.FullCensusDataLoops()
  val start_url = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"
  val iterable_census_data = CensusSF1ApiManager.CensusNestedData("https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*")
  println(iterable_census_data.size)
}