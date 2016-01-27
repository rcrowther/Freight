package freight.interface
package html
package servlet

import freight._
import generic.RouteItem
import idset.{HttpMethod, Options, Get, Post, Put, Delete, Trace}

import core.util.StringUtil


/** Connection handling Html request/responses.
  *  
  * This subclass of [[freight.interfaces.html.Connection]] adds
  * convenience methods for handling Freight Objects. If you only need
  * routing code, use the connection. This class is nicer for building
  * Freight-type Servlets.
  */
// Shoulld be an addin trait on ConnectionBase. Possibly RESTBasics?
class Connection
    extends ConnectionBase
{
  /*

   /** Renders a freight object to the response.
   * 
   * Builds an HTML display automatically from the object. Any class
   * instance derived from GenObject will work.
   */
   //def renderObject(obj: GenObjectWithVisitors) {
   //   renderString(obj.keyedWrite[String](new freight.adaptors.Html("")))
   // }
   //TODO: What about byte arrays?
   */

//TODO: Needs templating?
      val mkPage = pageBuilder.PreparedHTML5()
  
  /** Route for freight collections using Http method `GET`.
    * 
    * Supply with an object Meta and Collection, and it will read an
    * object and render to the return.
    *
    * Errors go to `notFound`
    */
  def get(
    matchStr: String,
    meta: CompanionMeta,
    coll: GiverTakable
  )
  {

    val rm = new ParsedRouteMatcher(matchStr + "/[:long:]")
    val idIdx = StringUtil.count(matchStr, '/') + 2

    def freightAction(): Any = {
      val id: String = wrequest.urlExtractor.path(idIdx)
      val b = new StringBuilder()
 val ob = ObjectBuilder(b, coll.meta.titleString)
      val ok = coll(id.toLong, ob)
      if (!ok) renderError(500)
      else {
        // default response code is?
        mkPage(title="", language="EN", description="", b.result)
      }
    }
    
    val pr = RouteItem(rm, freightAction _)
    routes += (Get, pr)
  }

/*
  /** Route for freight collections using Http method `POST`.
    * 
    * Supply with an object Meta and Collection, and it will trigger
    * some resource handling. The result of the handling is
    * undetermined, but the spec asks that the result is in some way
    * ''suborinate'' to the URL.
    *
    * @return  if successful 204 "No Content", else 500 "Internal
    *  Server Error"
    */
  def post(
    matchStr: String,
    meta: CompanionMeta,
    coll: GiverTakable
  )
  {

    val rm = new ParsedRouteMatcher(matchStr + "/[:long:]")
    val idIdx = StringUtil.count(matchStr, '/') + 2

    def freightAction(): Any = {
      val id: String = wrequest.urlExtractor.path(idIdx)
      val g: Giver = speak.from( wrequest.readAll() )
      val ok = *do something resourceful*
      if (!ok) renderError(500)
      else render204()

    }
    
    val pr = RouteItem(rm, freightAction _)
    routes += (Get, pr)
  }


  private val speak: StringCanSpeak

  /** Route for freight collections using Http method `PUT`.
    * 
    * Supply with an object Meta and Collection, and it will merge
    * (create or update) an object. The object must be associated with
    * the URL, but the action can have side effects, even changing
    * other URLs (e.g. /news/mostrecentarticle).
    *
    * @return  if successful status 201 "Created" or 204 "No Content"
    *  (meaning "Modified"), else 500 "Internal Server Error"
    */
  def put(
    matchStr: String,
    meta: CompanionMeta,
    coll: GiverTakable
  )
  {

    val rm = new ParsedRouteMatcher(matchStr + "/[:long:]")
    val idIdx = core.util.String.count(matchStr, '/') + 2

    def freightAction(): Any = {
      val id: String = wrequest.urlExtractor.path(idIdx)
      val g: Giver = speak.from( wrequest.readAll() )
      val writtenId: Long = coll<(id, g).toLong
      if (writtenId == NullID) renderError(500)
      else {
        if (writtenId == id) render204()
        else render201()
      }
    }
    
    val pr = RouteItem(rm, freightAction _)
    routes += (Get, pr)
  }
*/

  /** Route for freight collections using Http method `DELETE`.
    * 
    * Supply with an object Meta and Collection, and it will delete an
    * object. The object must be associated with the URL.
    *
    * @return  if successful status 204 "No Content", else 500 "Internal
    *  Server Error"
    * [[https://tools.ietf.org/html/rfc2616#section-9.7]]
    */
  def delete(
    matchStr: String,
    meta: CompanionMeta,
    coll: GiverTakable
  )
  {

    val rm = new ParsedRouteMatcher(matchStr + "/[:long:]")
    val idIdx = StringUtil.count(matchStr, '/') + 2

    def freightAction(): Any = {
      val id: String = wrequest.urlExtractor.path(idIdx)

      val ok = coll-(id.toLong)
      if (!ok) renderError(500)
      else render204()
    }
    
    val pr = RouteItem(rm, freightAction _)
    routes += (Delete, pr)
  }


}//Connection
