import scala.concurrent.{ExecutionContext, Future, Await}

import scala.concurrent.duration._

// val parsed_states = io.circe.parser.decode[List[List[String]]](source)
// creates the following
val example_data = Right(List(List("NAME", "state"), List("Alabama", "01"), List("Alaska", "02"), List("Arizona", "04")))

val x: Either[String, Int] = Right(42)
def f(i: Int) = i + 1
val y = x.map(f) // shorter, point-free notation
val z = x.fold(_ => 0, i => i + 1)

def blackBoxEither: String => Either[Exception, List[List[String]]] = (url:String) => {
  if (url == "passalong") {
    Right(List(List("NAME", "state"), List("Alabama", "01"), List("Alaska", "02"), List("Arizona", "04")))
  }
  else Left(new Exception(s"This didn't work bc blackbox didn't parse ${url}"))
}

val seed = "passalong"
//val seed = "notgonnawork"
val xx: Either[Exception, List[List[String]]] = blackBoxEither(seed)
def ff(i: List[List[String]]) = i.tail
def fff(i: List[List[String]]) = i.collect { case List(k, v) => k -> v }.toMap

val yy = xx.map(ff).map(fff)
val zz =
  xx.fold(
  e  => e,
  i => i.tail)

val last_pair = xx.map(ff).map(fff).
  fold(e => e.getMessage, i => i.last)

implicit val ec:ExecutionContext = ExecutionContext.global
val zz  = Future {
  xx.fold(
    e  => e,
    i => i.tail)
}

val zzz = zz
Await.result(zzz,  Duration(0, "millis"))

println(s"zzz: ${zzz}")
println(s"last_pair: ${last_pair}")
println(s"xx.map(fff): ${xx.map(fff)}")


// another Future example
def donutStock(donut: String): Future[Int] = Future {
  // assume some long running database operation
  println("checking donut stock")
  10
}

val vanillaDonutStock = Await.result(donutStock("vanilla donut"), Duration(5, "seconds"))
println(s"Stock of vanilla donut = $vanillaDonutStock")

// alternative approach to Either
def throwableToLeft[T](block: => T): Either[java.lang.Throwable, T] =
  try {
    block
    println("throwableToLeft try success")
    Right(block)
  } catch {
    case ex: Throwable => {
      println("throwableToLeft inside catch")
      Left(ex)
    }
  }

def blackBoxRaw: String => Map[String,String] = (url:String) => {
  if (url == "passalong") {
    Map("Alabama"-> "01", "Alaska"-> "02")
  }
  else throw new Exception(s"blackBoxRaw failure with ${url}")
}

println("throwableToLeft")
throwableToLeft { blackBoxRaw(seed) } match {
  case Right(s) => println(s)
  case Left(e) => {
    Left(e)
  }
}





