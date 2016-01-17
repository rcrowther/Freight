package freight.interface.html.servlet





class RoutingDemo
    extends ConnectionBase
{
  //override val developmentMode = false


  before() {
    println("before call!")
  }

  //get("paper/display") {
  // get("paper/display/\\d?\\d?") {
  /** Use a convenience symbol to represent "longNumber".
    */
  get("paper/display/[:long:]") {
    "<p>params: " + wrequest.urlExtractor.query.toString +
    "<p>url data: " + wrequest.url +
    "<p>id: " + wrequest.urlExtractor.path(3).toLong
  }

  /** Convenience 'elem' allows anything that fits in a path element.
    */
  get("paper/display/[:elem:]/limit") {
    "<p>params: " + wrequest.urlExtractor.query.toString +
    "<p>url data: " + wrequest.url +
    "<p>wildcard elem: " + wrequest.urlExtractor.path(3)
  }

  /** Must be explicit about trailing '/' matches.
    */
  get("razor/?") {
    "<p>" + "This URL works with trailing slash!" + "</p>"
  }

  /** redirect to '/razor'.
    */
  get("redirect") {
    redirect("/razor")
  }

  /** Render an error message with a return html code.
    */
  get("error") {
    renderError(404, "Quack!")
  }

  /** Throw an exception and see what happens.
*
* If developmentMode is true, this should render a stacktrace, if false, an html error page.
    */
  get("exception") {
    throw new Exception("Quack! Quack!")
  }

  // Explicitly render a string
  // also available:
  // renderInputStream
  // renderByteArray
  get("string"){
    val str = "Sheepshank"
    renderString(str)
  }

  /** throws stock error
    *  404 error with message
    */
  get("stockerror") {
    stockError()
  }



  get("") {
    "<p> What, no home? <p>"
  }

/** Not found
*/
  /*
   notFound() {
   "<p> What, no home? <p>"
   }
   */

  after() {
    println("after call!")
  }


}//TestServlet
