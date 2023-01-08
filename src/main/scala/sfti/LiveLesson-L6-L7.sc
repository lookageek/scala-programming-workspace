import java.lang.Math.{ceil, sqrt}
// ----------------------
// Functional Programming
// ----------------------

// Anonymous Functions
Array(3.14, 1.42, 2.0).map((x: Double) => 3 * x)
// map has been given an anonymous function

// we can define functions with val or def
val tripleValFunc = (x: Double) => 3 * x
// OR
def tripleFunc(x: Double) = 3 * x

// Both can be passed into a function accepting another function
Array(3.14, 1.42, 2.0).map(tripleValFunc)
Array(3.14, 1.42, 2.0).map(tripleFunc)
// practice is to give function a name if it is needed multiple times

// one more example of a higher-order function
// this function takes a function, and evaluates it with 0.25
def valueAtOneQuarter(f: Double => Double): Double = f(0.25)

valueAtOneQuarter(ceil)
valueAtOneQuarter(sqrt)

// function that produces another function
def mulBy(factor: Double) = (x: Double) => factor * x


// parameter replace with underscore
valueAtOneQuarter(x => 3 * x)
// can be written as
valueAtOneQuarter(3 * _)

// see this difference between foldLeft and reduceLeft
// https://stackoverflow.com/questions/7764197/difference-between-foldleft-and-reduceleft-in-scala
// Key point is foldLeft takes the initial value
// for reduceLeft the first element is the initial value
// reduceLeft produces a single value of the same type OR supertype of the sequence you pass
// foldLeft need not produce the single value of the same type of the sequence, it produces a value of initial element


// map function is a for ... yield statement
// filter function is a for ... yield statement with guard clause and yield doing no op on the elements

// a closure enclosing other variables, those other variables are called free variables
// closures are implemented as objects, with captured variables as instance variables in those objects


// Currying - turning a function that takes 2 arguments into a function that takes one argument, by fixing the first argument
// to a value
def mul(x: Int, y: Int) = x * y
// the curried version
def mulOneAtATime(x: Int) = (y: Int) => x * y
mulOneAtATime(3) // returns a function
mulOneAtATime(3)(12)

// syntactic sugar for the curried version in scala
def mulOneAtATime(x: Int)(y: Int) = x * y

// currying helps in the type inference for the methods which take in a function, when you pass an anonymous function
// for example correspondes function
// def corresponds[B](that: Seq[B])(p: (A, B) => Boolean): Boolean
// Here corresponds is called on a Array like this
// val a = Array("this", "world")
// val b = Array("is", "crazy")
// a.corresponds(b)(_.equalsIgnoreCase(_))
// for the compiler once you have called a.corresponds(b), the A, B types are fixed
// and then for the next parameter, which is a function, it can then do type inference, and we can call with _ for both the arguments

// call by name syntatic sugar
// lets say we have runInThread method
def runInThread(block: () => Unit): Unit = {
  new Thread {
    override def run() { block() }
  }.start()
}

// this method can be called as
runInThread(() => {
  println("Hi")
  Thread.sleep(2000)
  println("Bye")
})

// we can get rid of unsightly "() =>" when calling the runInThread, by changing the parameter to call by name
def runInThread2(block: => Unit): Unit = {
  new Thread {
    override def run() { block }
  }.start()
}

runInThread2 {
  println("Hi")
  Thread.sleep(2000)
  println("Bye")
}

// Exercises
// Take all the available time zones, then remove the continent part before the "/"
// then take a sample of it
val zones = java.util.TimeZone.getAvailableIDs

val x = zones.map(_.split("/"))
  .filter(_.length == 2)
  .map(_(1))
  .grouped(10)
  .map(_(0))
  .toArray


// writing a while loop in DIY
def While(condition: => Boolean)(block: => Unit): Unit = {
  if (condition) {
    block
    While(condition)(block)
  }
}

var i = 0
val n = 10
var f = 1
While(i < n) {
  f *= i
  i += 1
}


// -------------------------------
// Case Class And Pattern Matching
// -------------------------------

// case classes making case matching work
// they provide apply, unapply (which makes the extractors work)



// Case classes vs Polymorphism
// Consider representing an arthematic expression as a set of case classes in scala
// this has been represented as recursive data structures
abstract class Expr
case class Num(value: Int) extends Expr
case class Sum(left: Expr, right: Expr) extends Expr
case class Product(left: Expr, right: Expr) extends Expr

object Expr {
  def eval(exp: Expr): Int = exp match {
    case Num(v) => v
    case Sum(left, right) => eval(left) + eval(right)
    case Product(left, right) => eval(left) + eval(right)
  }
}

// If we have these match statements everywhere we want to do anything with an Expr
// It will lead to a lot of churn in code
// So, is polymorphic code better?
// Same eval function as a polymorphic behaviour can be implemented like this
abstract class Exprr {
  def eval: Int
}

class Num(val value: Int) extends Exprr {
  override def eval: Int = value
}

class Sum(val left: Exprr, val right: Exprr) extends Exprr {
  override def eval: Int = {
    left.eval + right.eval
  }
}

class Product(val left: Exprr, val right: Exprr) extends Exprr {
  override def eval: Int = {
    left.eval * right.eval
  }
}

// Which one you would choose?
// The general guidance is
// - Polymorphism is appropriate for open-ended collection of subclasses, like geometric shapes
// - Case classes and pattern matching are best for a bounded collection of subclasses and all the case classes are in one place

// Exercises
// Using Pattern Matching, write a function "swap" that receives a pair of integers and returns the pair with components swapped
def swap(pair: (Int, Int)): (Int, Int) = pair match {
  case (x, y) => (y, x)
}
// you could just return (pair._2, pair._1), but this way is preferred by the author


// Using Pattern Matching, write a function "swap" that receives an Array[Int] and returns the array with the first
// two elements swapped, provided the length is at least two
def swap2(a: Array[Int]): Array[Int] = a match {
  case Array(left, right, rest @ _*) => Array(right, left) ++ rest
  case _ => a
}

// Articles and Bundles
// A store sells articles which have a name and a price
// Also, it can sell a bundle of articles for a discount
abstract class Item
case class Article(description: String, price: Double) extends Item
case class Bundle(description: String, discount: Double, items: Item*) extends Item   // note the recursive nature of this modeling

val book = Article("Scala for the Impatient", 39.95)
val bundle = Bundle("xmas special", 10, book, Article("Old Potrero Straight Rye Whiskey", 79.95))

// write a function price(it: Item): Double to compute the price of the Item - which could be an article or a bundle
def price(it: Item): Double = it match {
  case Article(desc, price) => price
  case Bundle(_, disc, items @ _*) => items.map(price).sum - disc
}


// Option Type
// Reimplement the Option type for Double values. Provide classes of DoubleOption including SomeDouble and NoDouble
// for the two Option variants
abstract class DoubleOption {
  // define isEmpty and get as polymorphic functions
  def isEmpty: Boolean
  def get: Double
}
case class SomeDouble(value: Double) extends DoubleOption {
  override def isEmpty: Boolean = false

  override def get: Double = value
}
case object NoDouble extends DoubleOption {
  override def isEmpty: Boolean = true

  override def get: Double = throw new NoSuchElementException
}


// define a method isEmpty which returns true if the value is a NoDouble, or else true
// define a method get which returns the wrapped value in case of SomeDouble, or throws a NoSuchElementException
object DoubleOption {
  def isEmpty(doubleOption: DoubleOption): Boolean = doubleOption match {
    case NoDouble => true
    case _ => false
  }

  def get(doubleOption: DoubleOption): Double = doubleOption match {
    case NoDouble => throw new NoSuchElementException
    case SomeDouble(value) => value
  }
}

// write a function inv which does 1 / x on x parameter which is taken into the function, if x is 0, return NoDouble, else
// the double value wrapped in SomeDouble
def inv(x: Int): DoubleOption = x match {
  case 0 => NoDouble
  case _ => SomeDouble(1D / x)
}

// write a function that composes two functions which are of the form Double => DoubleOption
def compose(f: Double => DoubleOption, g: Double => DoubleOption): Double => DoubleOption = {
  (x: Double) => g(x) match {
    case SomeDouble(value) => f(value)
    case NoDouble => NoDouble
  }
}

// What is not covered
// - Files and Regular Expressions
// - Collections
// - Annotations
// - Type Parameters - Generics & Variance
// - Advanced Types
// - Implicits
// - Futures & Actors