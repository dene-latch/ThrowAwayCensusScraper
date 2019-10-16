# following haoyi's blog

install ammonite

```shell script
sudo sh -c '(echo "#!/usr/bin/env sh" && curl -L https://github.com/lihaoyi/Ammonite/releases/download/1.6.8/2.13-1.6.8) > /usr/local/bin/amm && chmod +x /usr/local/bin/amm'
```

import ivy for Jsoup

```scala
amm
```

DL wiki webpage w/ Jsoup

```scala
import $ivy.`org.jsoup:jsoup:1.12.1`, org.jsoup._

val doc = Jsoup.connect("http://en.wikipedia.org/").get()

doc.title()

val headlines = doc.select("#mp-itn b a")

import collection.JavaConverters._

for(headline <- headlines.asScala) yield (headline.attr("title"), headline.attr("href"))

for(headline <- headlines.asScala) yield headline.text

for(headline <- headlines.asScala) {
    println(headline.attr("id")
}




```

census

```scala
import $ivy.`org.jsoup:jsoup:1.12.1`, org.jsoup._
import collection.JavaConverters._

val doc = Jsoup.connect("https://www2.census.gov/econ/bps/").get() 
val links = doc.select("table>tbody>tr>td") 

for(link <- links.asScala) yield (link.attr("title"), link.attr("href"))
for(link <- links.asScala) yield link.text
for(link <- links.asScala) {
    println(link.text)
}
println(links.select("*").text)

// need scala to inspect
val mylinks = links.select(":nth-child(1)").asScala


val doc2 = Jsoup.connect()







```


scrape mozilla developer page

```scala
{
  import $ivy.`org.jsoup:jsoup:1.12.1`, org.jsoup._
  import collection.JavaConverters._
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
      .map(elem => (elem.text, elem.nextElementSibling match {case null => "" case x => x.text}))
    (url, tooltip, name, summary, methodsAndProperties)
  }
}
```

