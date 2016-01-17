package freight
package interface
package gui
package generic

trait FieldModifiableComponent {
  def get: String
  def set(v: String)
def enable: Unit
def disable: Unit
}
