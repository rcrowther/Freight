package freight
package interface
package gui
package generic

import java.util.concurrent.atomic.AtomicLong
import scala.collection.JavaConverters._
//import scala.collection.mutable.ConcurrentMap
import java.util.concurrent.ConcurrentHashMap

import db.{CollectionRoute}

/** Handle routed actions.
  *
  * Usually convert actions and data to signals in a serialsed link. However, `Local` versions will handle directly. 
  *
  * The actions usually come from outlying gui components 
  */
class WireConnectorLocal 
extends WireConnector
{

private val collectionLoader = db.CollectionRouter


def fieldQuery(r: CollectionRoute)
 : FieldqueryCommunicator =
{
val coll = collectionLoader.fieldQuery(
r.connectionType,
 r.collectionName
)
 new FieldqueryCommunicatorLocal( coll )
}

def refmap(r: CollectionRoute)
 : RefmapCommunicator =
{
val coll = collectionLoader.refmap(r.connectionType, r.collectionName)
 new RefmapCommunicatorLocal( coll )
}

}//WireConnectorLocal
