package freight
package core
package objects
package mutable

import java.lang.Character


/*
 <id type=id>0</id><language type=string>en</language><title type=string>Browning</title><description type=string>Poem</description><body type=text>When the fight begins within himself, a man's worth something.</body>null
 */

/** Reads strings to break them into parts.
  *
  * This lexer is intended as general. It works on
  * Java Unicode (UTF-32) text.
  */
//TODO: Extend with String, not chararry, locaters, where appropriate
// TODO: Check the grammar of exxtended methods.
final class SimpleLexer(d: String) {

  var str = d.toCharArray

  var cursor = 0

  def get: Character = new Character(str(cursor))



  /** Skip a given string.
    *
    * Matches the string char by char.
    */
  def ~(s: String) {
    var i = 0
    val sz = s.size
    while(i < sz && s(i) == d(cursor)) {
      i += 1
      cursor += 1
    }
    elErrorIf(sz != i, s"skip text '$s' not matched at cursor position: $cursor")
  }

  /** Skip a char.
    */
  def ~(c: Char) {
    if( c == d(cursor)) cursor += 1
    else error(s"skip char $c not matched at cursor position: $cursor")
  }

  /** Skip by one step.
    */
  def skip() {cursor += 1}

  /** Skip to a char and over.
    */
  def skipPast(chr: Character) {
    var c = get
    while(chr != c) {
      cursor += 1
      c = get
    }
  }

  /** Skip a fixed number of steps.
    */
  def skip(n: Int) {
    cursor += n
  }

  /** Skip digits.
    */
  def skipDigits() {
    var chr = get

    while (Character.isDigit(chr)) {
      cursor += 1
      chr = get
    }
  }

  /** Skip a newline.
    *
    * CR+LF
    */
  def skipNewline() {
    var chr = get
    elErrorIf((chr != 0x000D && chr != 0x000A), s"newline char ${chr.toLong} not matched at cursor position: $cursor")

    while (chr == 0x000D || chr == 0x000A) {
      cursor += 1
      chr = get
    }
  }

  /** Match whitespace.
    */
// Nope, skipWhitespace
  def whitespace() {
    var chr = get
    while (Character.isWhitespace(chr)) {
      cursor += 1
      chr = get
    }
  }


  /** Match a numeric with optional fraction.
    *
    * Matches initial sign '+' | '-'.
    *
    * Fails if empty
    */
  // Signs?
  def fraction : String = {
    val start = cursor

    if (get == '+' || get == '-') cursor += 1

    skipDigits()

    // if on a dot, it's a fractional end.
    // skip the dot, pick up following digits
    if(get == 0x002E) {
      cursor += 1
      skipDigits()
    }
    if (cursor == start) throw new FreightException(s"can not read float at cursor position: $cursor")
    else new String(str, start, cursor - start)
  }

  /** Matches digits.
    *
    * Matches initial sign '+' | '-'.
    *
    * Fails if empty
    */
  def number : String = {
    val start = cursor

    if (get == '+' || get == '-') cursor += 1

    skipDigits()

    if (cursor == start) throw new FreightException(s"can not read number at cursor position: $cursor")
    else new String(str, start, cursor - start)
  }

  /** Matches anything until a given chr.
    *
    * Succeeds if string is empty.
    *
    * Cursor is repositioned past the match.
    *
    * @return a string of chrs until the given string. 
    */
  def freeStringUntil(chr: Char) : String = {
    val start = cursor
    val idx = d.indexOf(chr, cursor)
    if(idx == -1) throw new FreightException(s"freeStringUntil failed to find char end $chr end at cursor position: $cursor")
    else {
      cursor = idx + 1
      new String(str, start, idx - start)
    }
  }

  /** Matches anything until a given string.
    *
    * Succeeds if string is empty.
    *
    * Cursor is repositioned past the match.
    *
    * @return a string of chrs until the given string. 
    */
  def freeStringUntil(s: String) : String = {
    val start = cursor
    val idx = d.indexOf(s, cursor)
    if(idx == -1) throw new FreightException(s"freeStringUntil failed to find string end $s at cursor position: $cursor")
    else {
      cursor = idx + s.length()
      new String(str, start, idx - start)
    }
  }


  /** Matches the classic symbol set.
    *
    * Matches Letter ~ (Letter | Digit | '_')
    *
    *  Fails if the symbol is empty.
    */
  // TODO: Needs more, like 2D - 5F _
  def symbol : String = {
    val start = cursor

    var chr = get
    if (!Character.isLetter(chr))  throw new FreightException(s"read symbol first letter $chr not a letter at cursor position: $cursor")
    else {
      cursor += 1

      chr = get

      // Testing for '_' too
      while (Character.isLetterOrDigit(chr) || chr == 0x005F) {
        cursor += 1
        chr = get
      }
      if (cursor == start) throw new FreightException(s"read symbol is empty at cursor position: $cursor")
      else new String(str, start, cursor - start)
    }
  }

  /** Matches text in quotes.
    *
    * Matches '"' ~ zeroOrMore(Any | '\"') ~ '"'.
    *
    * @return text *without* quotes
    */
  // Quoted is pecial as it finishes as it starts.
  //But couldn't it be done with smaller things (though escape is funny...)
  def quoted : String = {
    // Get the first quote
    this.~('"')

    // Right, start
    val start = cursor
    var chr = get
    var prev = chr

    // Checking for escapes
    //chr != " && prev != \
    while (!(chr == 0x0022 && prev != 0x005C)) {
      cursor += 1
      prev = chr
      chr = get
    }

    // Get the trailing quote
    this.~('"')

    // NB: If it gets this far, it at least matched quotes.
    new String(str, start, cursor - start - 1)
  }

  def reload(d: String) {
    str = d.toCharArray
    cursor = 0
  }

  def reset() {
    cursor = 0
  }

}//SimpleLexer



object SimpleLexer {

  def apply(d: String)
      : SimpleLexer =
  {
    new SimpleLexer(d)
  }

}//SimpleLexer
