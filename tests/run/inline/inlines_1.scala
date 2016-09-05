package p
import collection.mutable

object inlines {

  final val monitored = false

  @dotty.annotation.inline
  def f(x: Int): Int = x * x

  val hits = new mutable.HashMap[String, Int] {
    override def default(key: String): Int = 0
  }

  def record(fn: String, n: Int = 1) = {
    if (monitored) {
      val name = if (fn.startsWith("member-")) "member" else fn
      hits(name) += n
    }
  }

  @volatile private var stack: List[String] = Nil

  @dotty.annotation.inline
  def track[T](fn: String)(op: => T) =
    if (monitored) {
      stack = fn :: stack
      record(fn)
      try op
      finally stack = stack.tail
    } else op

  class Outer {
    def f = "Outer.f"
    class Inner {
      val msg = " Inner"
      @dotty.annotation.inline def m = msg
      @dotty.annotation.inline def g = f
      @dotty.annotation.inline def h = f ++ m
    }
    val inner = new Inner
  }
}
