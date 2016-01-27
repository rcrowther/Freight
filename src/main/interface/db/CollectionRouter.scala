package freight
package interface
package db

import java.nio.file.{Path, Files}
import java.io.File



object CollectionRouter {


  //http://www.javaworld.com/article/2077260/learn-java/learn-java-the-basics-of-java-class-loaders.html
  //Class r = loadClass(String className, boolean resolveIt);
  // Collection caches. No need to be mutable, only appended?
  val givers = scala.collection.mutable.Map.empty[String, Generator]
  val fieldQueries = scala.collection.mutable.Map.empty[String, FieldQueryable]
  val refmaps = scala.collection.mutable.Map.empty[String, RefMap]
  val binaries = scala.collection.mutable.Map.empty[String, BinaryTakable]

  private[this] def collectionKey(
    connectionType: Int,
    collectionName: String
  )
      : String =
  {
    ConnectionType.toMachineString(connectionType) + collectionName
  }

  //TODO: Cant enable cache until meta reflection sorted
  def giver(
    connectionType: Int,
    collectionName: String,
    meta: CompanionMeta
  )
      : Generator =
  {
    val key = collectionKey(connectionType, collectionName)
    givers.get(key).getOrElse{
      ConnectionRouter.connection(connectionType).collection(
        collectionName,
        meta
      )
    }
  }

  //TODO: Cant enable cache until meta reflection sorted
  def fieldQuery(
    connectionType: Int,
    collectionName: String,
    meta: CompanionMeta
  )
      : FieldQueryable =
  {
    val key = collectionKey(connectionType, collectionName)
    fieldQueries.get(key).getOrElse{
      ConnectionRouter.connection(connectionType).fieldQueryCollection(
        collectionName,
        meta
      )
    }
  }

// TODO: all in the get shit toether box...
  def fieldQuery(
    connectionType: Int,
    collectionName: String
  )
      : FieldQueryable = 
{

collectionName match {
case "poems" => fieldQuery( ConnectionType.sqlitejdbc, collectionName, freight.objects.Paper )
case _ => error(s"unrecognised test collection collectionName: $collectionName")
}
}


  def refmap(
    connectionType: Int,
    collectionName: String
  )
      : RefMap =
  {
    val key = collectionKey(connectionType, collectionName)
    refmaps.get(key).getOrElse{
      val c = ConnectionRouter.connection(connectionType).refMap(
        collectionName
      )
      refmaps += (key -> c)
      c
    }
  }


  def binary(
    connectionType: Int,
    collectionName: String
  )
      : BinaryTakable =
  {
    val key = collectionKey(connectionType, collectionName)
    binaries.get(key).getOrElse{
      val c = ConnectionRouter.connection(connectionType).binaryCollection(
        collectionName
      )
      binaries += (key -> c)
      c
    }
  }

}//Router
