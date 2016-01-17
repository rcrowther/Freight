package freight
package util

/** Time a stretch of code.
 * 
 * {{{
 * StopWatch.start("test1")
 * StopWatch.times(6) {
 * ... sometest
 * }
 * StopWatch.stop()
 * }}}
 * 
 * or
 * 
 * {{{
 * StopWatch.test("test1", 6) {
 * ... sometest
 * }
 * }}}
 * 
 * Little and easy for little, easy people.
 */
//TODO: The Apache.commons version?
object StopWatch {
  
      var s = 0L
      var id : String = ""
        
  def start(id: String = "") {
    StopWatch.this.id = id
   s = System.nanoTime();
  }    
      
      def times(multiply : Int)(block : => Unit) = {
         for (i <- 0 until multiply) {block}
      }
      
      def stop(formatTime: Boolean = true) {
        val diff = System.nanoTime() - s
      val tStr = if (formatTime)  humanFormat(diff)
                 else diff.toString + " ns"
      println(id + ": time taken :" + tStr)
      }
      
     def test(id: String = "", multiply : Int = 1)(block : => Unit) { 
       start(id)
      times(multiply)(block) 
      stop()
     }
     
     //private
     def humanFormat(time: Long) : String = {
       time match {
                  case x if (x < 1000L) => s"${x} nanosecs"
                  case x if (x < 1000000L) => f"${x/1000D}%g microsecs"
                  case x if (x < 1000000000L) => f"${x/1000000D}%g millisecs"
                  case x if (x < 60000000000L) => f"${x/1000000000D}%g secs"
                  case x if (x < 1000000000000L) => f"${x/60000000000D}%g minutes"
                  case x => s"${x/1000000000000L} seconds"
       }
       
     //String.format("%d:%02d:%02d", s/3600, (s%3600)/60, (s%60))
     }
     
}//Timer
