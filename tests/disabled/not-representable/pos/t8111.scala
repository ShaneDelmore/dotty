// structural types, cannot represent
trait T {

  def crashy(ma: Any): Unit = {
    // okay
    val f1 = (u: Unit) => ma
    foo(f1)()
    foo((u: Unit) => ma)
    foo(0, (u: Any) => ma) apply ()

    // crash due to side effects on the onwer of the symbol in the
    // qualifier or arguments of the application during an abandoned
    // names/defaults transform. The code type checkes because of
    // autp-tupling which promotes and empty parmater list to `(): Unit`
    foo((u: Any) => ma)()

    {{(u: Any) => ma}; this}.foo(0)()

    foo({def foo = ma; 0})()

    {def foo = ma; this}.foo(0)()
  }

  def foo(f: Any): Any => Any
}
