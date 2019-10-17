import org.jsoup._
import scala.jdk.CollectionConverters._
import scala.collection.mutable.ListBuffer
import io.Source

//val url = "https://www2.census.gov/econ/bps/State/"
//val doc = Jsoup.connect(url).get()
//val links = doc.select("table>tbody>tr>td>a")
//val linkData = links.asScala.map(link => (link.attr("href"), link.attr("title"), link.text))
//val dataItems = for ((url, tooltip, name) <- linkData) yield {
//  println("Scraping " + name)
//  println(((url, tooltip, name)))
//  (url, tooltip, name)
//}
//
//println(dataItems.size)


// grab csv data
val url = "https://www2.census.gov/econ/bps/County/co0001c.txt"

val content = scala.io.Source.fromURL(url).mkString

val lines = content.split("\n").filter(_ != "")

val aShape = (lines.size, lines(0).size)

//val doc = Jsoup.connect(url).ignoreContentType(true).userAgent("Mozilla").get
//
//val info = doc.text()
//
//val s = Source.fromString(info)
//
//def printLines(source: Source) {
//  for (line <- source.getLines) {
//    println(line)
//  }
//}
//
//printLines(s)
//
//val line = s.getLines.take(1).toList
//
//println(line)
//
//for (line <- bufferedSource.getLines) {
//  val cols = line.split(",").map(_.trim)
//  // do whatever you want with the columns here
//  println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}")
//}
//
//println("Month, Income, Expenses, Profit")
//val bufferedSource = io.Source.fromFile("/tmp/finance.csv")
//for (line <- bufferedSource.getLines) {
//  val cols = line.split(",").map(_.trim)
//  // do whatever you want with the columns here
//  println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}")
//}
//bufferedSource.close
