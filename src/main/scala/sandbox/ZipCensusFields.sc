import java.io.{File, BufferedWriter, FileWriter}
import com.github.tototoshi.csv._

val fieldIdsFile = new File(getClass.getClassLoader.
  getResource("census-data-docs/census_fields_id_list.txt").getPath)
val fieldIdsString:String = scala.io.Source.fromFile(fieldIdsFile).mkString
val fieldIdsList:List[String] = fieldIdsString.split(",").map(_.trim).toList

val fieldDescFile = new File(getClass.getClassLoader.
  getResource("census-data-docs/census_fields_natural_list.txt").getPath)
val fieldDescsString:String = scala.io.Source.fromFile(fieldDescFile).mkString
val fieldDescsList:List[String] = fieldDescsString.split(";").map(_.trim).toList

fieldIdsList.size
fieldDescsList.size

val pairs:List[List[ String ] ] =  (fieldDescsList zip fieldIdsList).map(x=>List(x._1,x._2))






val byDescFile = new BufferedWriter(new FileWriter(
  "/Users/denefarrell/Desktop/census-id-pairs-by-desc.csv"))
val byDescWriter = new CSVWriter(byDescFile)
val byDescFields = Seq("desc","id")
byDescWriter.writeRow(byDescFields)
byDescWriter.writeAll(pairs: Seq[ Seq[ String ] ] )
byDescFile.close()

val flippedPairs:List[List[ String ] ] =  (fieldDescsList zip fieldIdsList).map(x=>List(x._2,x._1))
val byIdFile = new BufferedWriter(new FileWriter(
  "/Users/denefarrell/Desktop/census-id-pairs-by-id.csv"))
val byIdWriter = new CSVWriter(byIdFile)
val byIdFields = Seq("id","desc")
byIdWriter.writeRow(byIdFields)
byIdWriter.writeAll(flippedPairs: Seq[ Seq[ String ] ] )
byIdFile.close()


