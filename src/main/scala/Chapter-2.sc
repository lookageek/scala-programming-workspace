// writing loops as tail recursions which compile down to loops without recursion call stack
// we write loops as recursions to avoid data mutation inside a loop
def fib(n: Int): Int = {
  @annotation.tailrec
  def go(n: Int, prev: Int, curr: Int): Int = {
    if (n == 0)
      prev
    else if (n == 1)
      curr
    else
      go(n - 1, curr, prev + curr)
  }

  go(n, 0, 1)
}

object MyModule {
  def factorial(n: Int): Int = {
    @annotation.tailrec
    def go(n: Int, acc: Int): Int = {
      if (n == 0)
        acc
      else
        go(n - 1, acc * n)
    }

    go(n, 1)
  }

  def abs(n: Int): Int =
    if (n < 0)
      -n
    else
      n

  private def formatAbs(x: Int): String = {
    val msg = "The absolute value of %d is %d"
    msg.format(x, abs(x))
  }

  private def formatFactorial(x: Int): String = {
    val msg = "The factorial of %d is %d."
    msg.format(x, factorial(x))
  }

  private def formatResult(x: Int, fnName: String, f: Int => Int): String = {
    val msg = "The %s of %d is  %d"
    msg.format(fnName, x, f(x))
  }

  def main(args: Array[String]): Unit =  {
    println(formatAbs(-42))
    println(formatResult(5, "factorial", factorial))
  }
}

// monomorphic function
def findFirst(ss: Array[String], key: String): Int = {
  @annotation.tailrec
  def loop(n: Int): Int = {
    if (n >= ss.length)
      -1
    else if (ss(n) == key)
      n
    else
      loop(n + 1)
  }

  loop(0)
}

// polymorphic function of the same, here we will extract the logic of comparison
// to a function to be passed in
def findFirstPoly[T](ss: Array[T], p: T => Boolean): Int = {
  @annotation.tailrec
  def loop(n: Int): Int = {
    if (n >= ss.length)
      -1
    else if (p(ss(n)))
      n
    else
      loop(n + 1)
  }

  loop(0)
}

// one more polymorphic function which takes a function as its parameter
def isSorted[A](ss: Array[A], ordered: (A, A) => Boolean): Boolean = {
  @annotation.tailrec
  def loop(a: Int, b: Int): Boolean = {
    if (b >= ss.length)
      true
    else if (ordered(ss(a), ss(b)))
      loop(a + 1, b + 1)
    else
      false
  }

  loop(0, 1)
}

// calling higher-order functions with anonymous functions
isSorted(Array(1, 2, 3, 4), (x: Int, y: Int) => x < y)
findFirstPoly(Array("ab", "bc", "cd"), (x: String) => x == "ab")

// anonymous functions and "apply method"
// whenever we declare an anonymous function, we are declaring an object with apply method
// this apply method gets calls when we are using the object as if it is a function
val lessThan = new Function2[Int, Int, Boolean] {
  def apply(a: Int, b: Int): Boolean = a < b
}


// partial application of a function
// a function taking 3 type parameters returns a function which takes 2 type parameters
// the implementation follows the types that's all, because in many polymorphic functions
// there is only one implementation possible
def partial1[A,B,C](a: A, f: (A, B) => C): B => C = {
  (b: B) => f(a, b)
}


// currying is taking a function of 2 arguments and returning a function of one argument
// which will partially apply the function that is provided
def curry[A,B,C](f: (A,B) => C): A => B => C = {
  (a: A) => (b: B) => f(a, b)
}

// uncurry is the reverse, take a function and reverse the transformation
// again, the type constraints allow for only one implementation
def uncurry[A,B,C](f: A => B => C): (A, B) => C = {
  (a: A, b: B) => f(a)(b)
}

// composing two functions
// this is written as g compose f, mathematically written g . f read right to left
// read as g after f, where output of f is passed on to g
def compose[A,B,C](f: A => B, g: B => C): A => C = {
  g compose f
}

def composeBare[A,B,C](f: A => B, g: B => C): A => C = {
  (a: A) => g(f(a))
}