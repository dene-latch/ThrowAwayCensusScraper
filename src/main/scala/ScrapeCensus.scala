import org.jsoup._
import scala.jdk.CollectionConverters._

object ScrapeCensus {

  def MozillaDemoScrape(args: Array[String]): Unit = {
    val indexDoc = Jsoup.connect("https://developer.mozilla.org/en-US/docs/Web/API").get()
    val links = indexDoc.select("h2#Interfaces").nextAll.select("div.index a")
    val linkData = links.asScala.map(link => (link.attr("href"), link.attr("title"), link.text))
    val articles = for((url, tooltip, name) <- linkData) yield {
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
}

object TalkToMe extends App {
  Console.println("Hello World: " + (args mkString ", "))
}