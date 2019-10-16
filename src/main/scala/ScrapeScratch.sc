import org.jsoup._
// import $ivy.`org.jsoup:jsoup:1.12.1`, org.jsoup._ // ammonite
import scala.jdk.CollectionConverters._
import scala.collection.mutable.ListBuffer

val url = "https://www2.census.gov/econ/bps/"
val doc = Jsoup.connect("https://www2.census.gov/econ/bps/").get()
val links = doc.select("table>tbody>tr>td>*")
var refs = new ListBuffer[String]()
val droppers = List[String](
  "Parent Directory",
  "Documentation"
)
for(link <- links.asScala) {
  if ( ! link.text.isEmpty() && ! droppers.contains(link.text) )
    refs += (url + link.text)
}
println(refs)


// // try by id
//val CountyLink = doc.select("a[id^=anch_322]")
//val ParentLink = doc.select("#anch_321")
//val all = doc.select("*")
//
//for(item <- all.asScala) {
//  if ( ! item.attr("id").isEmpty() ) println((item.attr("id")))
//}
