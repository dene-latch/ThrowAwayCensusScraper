import java.sql.DriverManager
import java.sql.Connection

object PGManager {

  def main(args: Array[String]): Unit = {

    val driver = "org.postgresql.Driver"
    val url = "jdbc:postgresql://0.0.0.0:5432/mydb"
    val username = "john"
    val password = "pwd0123456789"

    var connection:Connection = null

    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)
//      connection = Database.forUrl(url, username, password)

      val statement = connection.createStatement()
      statement.executeQuery("CREATE TABLE IF NOT EXISTS test (id serial PRIMARY KEY, num integer, data varchar);")
      statement.executeQuery("INSERT INTO test (num, data) VALUES (%s, %s), (100, 'abcdef')" )
      val queryResult = statement.executeQuery("SELECT * FROM test;")
    } catch {
      case e: Throwable => e.printStackTrace
    }
    connection.close()
  }

}