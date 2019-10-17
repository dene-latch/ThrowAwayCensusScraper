import org.jsoup._
import scala.jdk.CollectionConverters._
import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object ScrapeCensus {

  def MozillaDemoScrape(args: Array[String]): Unit = {
    val indexDoc = Jsoup.connect("https://developer.mozilla.org/en-US/docs/Web/API").get()
    val links = indexDoc.select("h2#Interfaces").nextAll.select("div.index a")
    val linkData = links.asScala.map(link => (link.attr("href"), link.attr("title"), link.text))
    val articles = for ((url, tooltip, name) <- linkData) yield {
      println("Scraping " + name)
      val doc = Jsoup.connect("https://developer.mozilla.org" + url).get()
      val summary = doc.select("article#wikiArticle > p").asScala.headOption.fold("")(_.text)
      val methodsAndProperties = doc
        .select("article#wikiArticle dl dt")
        .asScala
        .map(elem => (elem.text, elem.nextElementSibling match {
          case null => ""
          case x => x.text
        }))
      (url, tooltip, name, summary, methodsAndProperties)
    }
  }

  def CensusCats(args: Array[String]): List[String] = {
    val url = "https://www2.census.gov/econ/bps/"
    val indexDoc = Jsoup.connect(url).get()
    val links = indexDoc.select("table>tbody>tr>td>*").asScala.filter(!_.text.isEmpty())
    val cats = for (link <- links) yield link.text
    val cleanCats = cats.filter(x => !(List("Parent Directory", "Documentation") contains x))
    println(cleanCats.toList)
    cleanCats.toList
  }

  def retry[T](n: Int)(fn: => T): T = {
    try {
      fn
    } catch {
      case e =>
        if (n > 1) {
          println(s"try $n failed.")
          Thread.sleep((n-1)*1000)
          retry(n - 1)(fn)
        }
        else throw e
    }
  }

  def DLFile(url: String): Unit = {
    val content = retry(3) {
      scala.io.Source.fromURL(url).mkString
    }
    val lines = content.split("\n").filter(_ != "")
    val aShape = (lines.size, lines(0).size)
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
      (data)
    }
    println(dataItems.size)
  }
}


object TalkToMe extends App {
  val cleanCats = ScrapeCensus.CensusCats(args)
  val dataItems = for (cc <- cleanCats) yield {
    println(cc)
    ScrapeCensus.ScrapeCBCat(cc)
  }
}