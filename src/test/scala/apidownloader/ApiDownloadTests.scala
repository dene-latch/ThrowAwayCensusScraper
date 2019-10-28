package apidownloader

import apidownloader.CensusApiParamLookup.{api_params, base_urls}
import org.scalatest.freespec.AnyFreeSpec


class ApiDownloadTests extends AnyFreeSpec {

  "yaml test files" - {
      val census_api_name = "reconst" // New Residential Construction
      val base_url:String = base_urls(census_api_name)
      val request_args: Map[String, String] = List(
        ("columns",api_params(census_api_name)("column_list").mkString(",")),
        ("filter", api_params(census_api_name)("filter_list").mkString("&"))
      ).toMap

      val url = CensusApiManager.ConstructCensusUrl(base_url, request_args)
      CensusApiManager.DownloadFromApi(url, census_api_name=census_api_name)
    }
}
