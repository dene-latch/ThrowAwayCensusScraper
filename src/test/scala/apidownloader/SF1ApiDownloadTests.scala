package apidownloader

import apidownloader.CensusSF1ApiManager._

import org.scalatest.flatspec.AnyFlatSpec


class SF1ApiDownloadTests extends AnyFlatSpec {
  "test SF1 API call" should "works for decennial data and gets States" in {
    val start_url = "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"
    val parsed_states = (GetCensusVals _ compose source _) (start_url)
    val expected_states = Right(List("01", "02", "04", "05", "06", "22", "21", "08", "09", "10", "11", "12", "13", "15",
      "16", "17", "18", "19", "20", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36",
      "37", "38", "39", "40", "41", "42", "44", "45", "46", "47", "48", "49", "50", "51", "53", "54", "55", "56", "72"))
    assert(parsed_states == expected_states)
    }

  "test SF1 API call" should "work for decennial data and gets Counties" in {
    val synth_states = Right(List("01"))
    val synth_counties = synth_states.map(getCounties).fold(
        e => {
          throw e
        },
        x => { x.iterator.flatMap( y => y) }
      ).flatten.toMap
    println(s"test counties: ${synth_counties}")
    val expected_counties = Map("state_id" -> "01", "county_id" -> "5")
    assert(synth_counties == expected_counties)
  }


  "test SF1 API call" should "works for decennial data and gets Blocks" in {
    val synth_countyStates = Right(Seq(List(Map("state_id" -> "01", "county_id" -> "001"), Map("state_id" -> "01", "county_id" -> "087"))))
    val parsed_blocks = synth_countyStates.map(getBlocks).fold(
      e => {
        throw e
      },
      x => { x.iterator.flatMap( y => y) }
    ).flatten
    val expected_size = 3786
    assert(parsed_blocks.size == expected_size)
  }

}
