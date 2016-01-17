package freight
package interface
package gui
package jswing

import swing.Publisher

import jswing.event.StatusMessage



class SingleGiverCollectionController(
  val coll: GiverTakable
)
extends GenSingleTakeableController[Giver, Taker]


