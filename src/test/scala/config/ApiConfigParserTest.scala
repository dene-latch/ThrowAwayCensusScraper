package config

import org.specs2.mutable.Specification

class ApiConfigParserTest extends Specification {

  "CastleConfigParser" should {
    "load config with custom values" in {
      val apiConfig = config.parse("""
############################ API Consumer CONFIGURATION ############################

version: "3"
apis:

  # ----------------
  # --- REQUIRED ---

  namespace: reconst

  api-id: my_test-namespace

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

  example-section:

  # ----------------
  # --- OPTIONAL ---

   cookieMonster: 1073741824
   bigBird: 4194176
   oscar: "grouch"
   count: "dracula"
                                      """)

      apiConfig.namespace must_== "my_test-namespace"
      val myApiConfig = apiConfig.apiConfigs.head
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

      apiConfig.sesameConfig.cookieMonster must_== 1073741824
      apiConfig.sesameConfig.bigBird must_== 4194176
      apiConfig.sesameConfig.oscar must_== "grouch"
      apiConfig.sesameConfig.cookieMonster must_== "dracula"

    }
  }
}