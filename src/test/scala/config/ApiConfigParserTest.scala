package config

import org.specs2.mutable.Specification

class ApiConfigParserTest extends Specification {

  "CastleConfigParser" should {
    "load config with custom values" in {
      val apiConfig = config.parse("""
############################ API Consumer CONFIGURATION ############################

data_sources:

  # ----------------
  # --- REQUIRED ---

  apis:
    - reconst


reconst:
  column_list:
    - cell_value
    - data_type_code
    - time_slot_id
    - category_code
    - seasonally_adj
  filter_list:
    - "time=from+1900"
  pk_cols:
    - category_code
    - data_type_code
    - time_slot_id
  url: https://api.census.gov/data/timeseries/eits/resconst
  customConfig: reconstCustomConfig

reconstCustomConfig:
  someKey: someValue
  anotherKey: anotherValue

example-section:

  # ----------------
  # --- OPTIONAL ---

   cookieMonster: 1073741824
   bigBird: 4194176
   oscar: "grouch"
   count: "dracula"
                                      """)

      val myApiConfig = apiConfig.apiConfigs.head
      println(myApiConfig)

      myApiConfig.columns must_== Set(
        "cell_value",
        "data_type_code",
        "time_slot_id",
        "category_code",
        "seasonally_adj"
      )

      myApiConfig.filters must_== Set(
        "time=from+1900"
      )

      myApiConfig.pk_cols must_== Set(
        "data_type_code",
        "time_slot_id",
        "category_code"
      )

      myApiConfig.url must_== "https://api.census.gov/data/timeseries/eits/resconst"

      myApiConfig.customConfig must havePairs("someKey" -> "someValue", "anotherKey" -> "anotherValue")
      myApiConfig.customConfig.size must_== 2

      apiConfig.sesameConfig.cookieMonster must_== 1073741824
      apiConfig.sesameConfig.bigBird must_== 4194176
      apiConfig.sesameConfig.oscar must_== "grouch"
      apiConfig.sesameConfig.count must_== "dracula"

    }
  }
}