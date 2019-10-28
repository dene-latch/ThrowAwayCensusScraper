package apidownloader

object CensusApiParamLookup {

  val api_params: Map[String, Map[String, List[String]]] =
    List(
      (
        "reconst",
        List(
          ("column_list",
            List(
              "cell_value",
              "data_type_code",
              "time_slot_id",
              "category_code",
              "seasonally_adj"
            )
          ),
          ("filter_list",
            List("time=from+2019")
          ),
          ("pk_cols",
            List(
              "category_code",
              "data_type_code",
              "time_slot_id"
            )
          )
        ).toMap
      )
    ).toMap

  val base_urls: Map[String, String] =
    List(
      (
        "reconst",
        "https://api.census.gov/data/timeseries/eits/resconst" // New Residential Construction
      ),
      (
        "hv",
        "https://api.census.gov/data/timeseries/eits/hv" // Housing Vacancies and Homeownership
      ),
      (
        "mhs",
        "https://api.census.gov/data/timeseries/eits/mhs" // Manufactured Homes Survey
      ),
      (
        "vip",
        "https://api.census.gov/daa/timeseries/eits/vip" // Construction Spending
      )
    ).toMap

}


//  val base_url = "https://api.census.gov/data/1987/cbp" // County Business Patterns
//  val cols: List[String] = List(
//    "GEO_TTL",
//    "SIC_TTL",
//    "EMP,ESTAB"
//  )
//  val filters: List[String] = List(
//    "for=county:*&in=state:*"
//  )