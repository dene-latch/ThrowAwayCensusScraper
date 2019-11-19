package sandbox
import java.io.File

object CensusMeta {

  def JoinFields() {
    val fieldIdsFile = new File(getClass.getClassLoader.
      getResource("census-data-docs/census_fields_id_list.txt").getPath)

    val fieldDescFile = new File(getClass.getClassLoader.
      getResource("census-data-docs/census_fields_natural_list.txt").getPath)


    val fieldIds: String = scala.io.Source.fromFile(fieldIdsFile).mkString
    val fieldDescs: String = scala.io.Source.fromFile(fieldDescFile).mkString

    println(fieldIds)
    println(fieldDescs)

  }

}

object main extends App {
  CensusMeta.JoinFields()
}

