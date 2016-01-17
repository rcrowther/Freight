package freight
package interface
package gui
package jswing

import swing.Publisher

import jswing.event.StatusMessage


import java.nio.channels.{
  ReadableByteChannel,
  WritableByteChannel
}

class SingleBinaryCollectionController(
  val coll: BinaryTakable
)
extends GenSingleTakeableController[(ReadableByteChannel, Long), WritableByteChannel]

