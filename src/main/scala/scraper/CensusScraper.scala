package scraper

import org.jsoup._

import scala.jdk.CollectionConverters._

object CensusScraper {

  def CensusCats(args: Array[String]): List[String] = {
    val url = "https://www2.census.gov/econ/bps/"
    val indexDoc = Jsoup.connect(url).get()
    val links = indexDoc.select("table>tbody>tr>td>*").asScala.filter(!_.text.isEmpty())
    val cats = for (link <- links) yield link.text
    val cleanCats = cats.filter(x => !(List("Parent Directory", "Documentation", "Place") contains x))
    println(cleanCats.toList)
    cleanCats.toList
  }

  def retry[T](n: Int)(fn: => T): T = {
    try {
      fn
    } catch {
      case e: Throwable =>
        if (n > 1) {
          println(s"try $n failed.")
          Thread.sleep((n-1)*1000)
          retry(n - 1)(fn)
        }
        else throw e
    }
  }

  def DLFile(url: String): Array[String] = {
    val content = retry(3) {
      scala.io.Source.fromURL(url).mkString
    }
    val lines = content.split("[\r\n]+").filter(_ != "")
    lines.foreach(x => {
      println(x)
      println(x.split(",",-1).map(x => {
        x match {
          case "" => 0
//          case prop: String => prop
          case _ => 0
        }
      }).size)
    }
    )
//    val aShape = (lines.size, lines(0).size)
    println(lines)
    lines
  }


  def ScrapeCBCat(cat: String): Unit = {
    val home_url = "https://www2.census.gov/econ/bps/"
    val doc = Jsoup.connect(home_url + cat).get()
    val links = doc.select("table>tbody>tr>td>a")
    val linkData = links.asScala.filter(_.text.contains(".txt")).map(link => (link.attr("href"), link.attr("title"), link.text))
    val dataItems = for ((url, tooltip, name) <- linkData) yield {
      println("Scraping " + name)
      println(((url, tooltip, name)))
      val data = DLFile(home_url + cat + "/" + url)
      data.foreach(x => println(x))
      (data)
    }
    println(dataItems.size)
  }
}


object TalkToMe extends App {
  val cleanCats = CensusScraper.CensusCats(args)
//  val dataItems = for (cc <- cleanCats) yield {
//    println(cc)
//    ScrapeCensus.ScrapeCBCat(cc)
//  }
  // empty value
  val oneOff = CensusScraper.DLFile("https://www2.census.gov/econ/bps/Metro//ma0001c.txt")
  // empty trailing line
//  val oneOff = ScrapeCensus.DLFile(" https://www2.census.gov/econ/bps/County//co2010a.txt")
  println(oneOff)
}