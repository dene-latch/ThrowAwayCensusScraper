package apidownloader

object CensusSF1ApiParamLookup {

  //  "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=state:*"
  //  "https://api.census.gov/data/2010/dec/sf1?get=NAME&for=county:*&in=state:"
  //  "https://api.census.gov/data/2010/dec/sf1?get=NAME,GEO_ID,BLKGRP,BLOCK,CBSA,CONCIT,COUNTY,COUSUB,CSA,DIVISION,H001001,H002002&for=block:*&in=state:53%20county:"

  val api_params: Map[String, Map[String, List[String]]] =
    List(
      (
        "states",
        List(
          ("column_list",
            List(
              "NAME"
            )
          ),
          ("filter_list",
            List("for=state:*")
          ),
          ("pk_cols",
            List(
              "NAME"
            )
          )
        ).toMap
      ),
      (
        "counties",
        List(
          ("column_list",
            List(
              "NAME"
            )
          ),
          ("filter_list",
            List(
              "for=county:*",
              "in=state:${state_id}"
            )
          ),
          ("pk_cols",
            List(
              "NAME"
            )
          )
        ).toMap
      ),
      (
        "blocks",
        List(
          ("column_list",
            List(
              "NAME",
              "GEO_ID",
              "BLKGRP",
              "BLOCK",
              "CBSA",
              "CONCIT",
              "COUNTY",
              "COUSUB",
              "CSA",
              "DIVISION",
              "H001001",
              "H002002"
            )
          ),
          ("filter_list",
            List(
              "&for=block:*",
              "&in=state:${state_id}%20county:${county_id}"
            )
          ),
          ("pk_cols",
            List(
              "NAME"
            )
          )
        ).toMap
      )
    ).toMap
}

//  val base_urls: Map[String, String] =
//    List(
//      (
//        "sf1-2010",
//        "https://api.census.gov/data/2010/dec/sf1"
//      )
//    ).toMap
