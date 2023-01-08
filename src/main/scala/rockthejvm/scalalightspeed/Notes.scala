package com.lookageek.scala
package rockthejvm.scalalightspeed

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success, Try}

object Notes extends App {
  // THE ABSOLUTE BASICS
  // In Scala, there are three fundamental constructs
  // values, expressions and functions
  // values are data when an expression is evaluated
  // almost everything is an expression in scala and result in values
  // values are assigned names to be of use later on
  // only things which are not expression definition statements
  // like definition of a val, class or package are not expression
  // everything else is an expression
  // if expression is an expression, code block is an expression
  // when defining a function, the function body is an expression - either a single
  // line or code block
  // when you want to write a loop, you should reach out for recursion, and not use a loop
  // scala has a thing to represent no meaningful value, is the Unit return type, and there
  // is only one value called ()
  // Unit is represented as type of returning after performing side-effects

  // OBJECT ORIENTED PROGRAMMING
  // defining a class in Scala with bare bones
  class Animal

  // instantiate an object of this class
  val aAnimal = new Animal

  // inheritance in scala
  class Dog extends Animal

  // defining members in a class
  class Car {
    val age: Int = 0 // this member should have a value attached,
    // since all vals should have value when we declare them

    var kmDriven: Int = 0 // same for var too actually

    def drive() = println("I'm driving")
  }

  // creating an object of Car class will always have age as 0
  val aCar = new Car
  println(aCar.age)

  // we cannot change the value of age
  // aCar.age = 3

  // but we can change kmDriven
  aCar.kmDriven = 34

  // class having arguments in the paranthesis are called "constructor arguments"
  // a free constructor is provided to fill in these values when instantiating the object
  // but these are NOT same as class fields
  class Table(width: Double, height: Double)

  val aTable = new Table(1, 2)
  // i cannot access the width or height, it is ephemeral, not visible out of class
  // aTable.height

  // table with no values passed in will complain
  // val notATable = new Table

  // to promote a constructor argument to a field, we should add "val" in front of it
  class Chair(val width: Double, val height: Double)
  val aChair = new Chair(1, 2)
  println(aChair.width) // now these are available as fields

  // scala has subtype polymorphism
  val aAnimalVal: Animal = new Dog

  // abstract class
  abstract class WalkingAnimal {
    val hasLegs: Boolean = true // all fields and methods in scala classes are by default public,
    // restrict by using private
    // or "protected" which means all subclasses too have access to the field
    private val numberOfLegs: Int = 4
    def walk(): Unit // unimplemented, any class extending WalkingAnimal has to provide the implementation
  }

  class Cat extends WalkingAnimal {
    override def walk(): Unit = println("Tippity Tap")
  }

  val aCat = new Cat
  println(aCat.hasLegs) // OK
  // println(aCat.numberOfLegs) // Member not visible outside

  // traits, can have default implementations
  // scala allows single class inheritance and multi-trait "mixing"
  // extending a class and then using "with" to extend traits is called "mixing-in" traits
  // for e.g., class Crocodile extends Animal with Carnivore with WalkingAnimal ...
  // Carnivore and WalkingAnimal are the mixins

  // singleton object
  object MySingleton {

  } // there is only one instance of this object

  // COMPANION OBJECTS for classes are made to hold some static methods
  // like apply, or static fields
  // companion objects have access to the private fields on the class
  object Table {

  }

  // CASE CLASSES are lightweight datastructures with some boilerplate
  // - sensible equals and hashcode
  // - companion object with apply method
  // - serialization
  // - pattern matching, this is only available for case classes
  case class Person(name: String, age: Int)
  val aPerson = Person("Smith", 25) // this is possible due to "apply" method declared
  println(aPerson.age) // in a case class we do not need "val" on class parameters to make them a field

  // EXCEPTIONS
  try {
    val x: String = null
    println(x.length)
  } catch {
    case e: Exception => println(s"Exception message ${e.getLocalizedMessage}")
  } finally {
    println("Finally Block executed no matter what")
  }

  // Core Points
  // In scala we usually operate with immutable values
  // any modification of an immutable value leads to creation of a new value with the modification applied
  // for eg list.reverse returns a new list after reversing the order of elements


  // FUNCTIONAL PROGRAMMING
  // Functional Programming is all about
  // - compose functions
  // - pass functions as arguments
  // - return functions as results

  // But scala runs on JVM which is optimised for OO where functions are not treated outside of objects
  // so scala has the concept of FunctionX which are objects but behave as top class functions without nesting
  // under any object
  // for e.g.,
  val simpleIncrementer = new Function[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  // we are calling this object simpleIncrement as a function
  // and no other operations are available to be done on this object
  // except calling it like this
  simpleIncrementer(3)

  // In conclusion, all scala functions are instances of these function_x types
  // This is how a functional programming language is implemented on top of JVM which caters to
  // object oriented programming style

  // one more example of FunctionX
  val stringConcatenator = new Function2[String, String, String] {
    override def apply(v1: String, v2: String): String = v1 + v2
  }

  stringConcatenator("Hey", " You")

  // Now there is scala syntatic sugar called "lambda" to declare such functions
  val stringConcatLambda: Function2[String, String, String] = (s1: String, s2: String) => s1 + s2

  // There is a syntatic sugar for the FunctionX type too
  // Function2[String, String, String] <=> (String, String) => String

  // Higher Order Functions
  val aMappedList = List(1, 2, 3).map(x => x + 1)
  // return is another list
  println(aMappedList)

  val flatMappedList = List(1, 2, 3).flatMap(x => List(x, x * 2))
  // flatMap flattens all the iterables returns from the map operation
  println(flatMappedList)

  // alternative syntax for HOF lambdas .map(x => x + 1) <=> .map(_ + 1), watch that we do not have any stabby arrow syntax anymore

  // note that all of these operations always returns a new list after the operation, and not the original list

  // for comprehension
  // consider this deeply nested code piece
  val allPairs = List(1, 2, 3).flatMap(number => List('a', 'b', 'c').map(letter => s"$number-$letter"))

  // in for comprehension, which will iterate the first list, then for each element in first list, iterate
  // over all elements of second list, then again any further lists
  val allPairsComp = for {
    number <- List(1, 2, 3)
    letter <- List('a', 'b', 'c')
    number1 <- List(1, 2, 3)
  } yield s"$number-$letter-$number1"
  println(allPairsComp)


  // COLLECTIONS
  // Lists
  val aList = List(1, 2, 3, 4, 5)
  println(s"First Element - $aList.head")
  println(s"Rest of the elements in the list - $aList.tail")
  val aPrependedList = 0 :: aList // List(0, 1, 2, 3, 4, 5)
  val anExtendedList = 0 +: aList :+ 6 // List(0, 1, 2, 3, 4, 5, 6), so +: is prepend operator, :+ is append operator

  // Sequences
  val aSequence = Seq(1, 2, 3)

  // Vectors are fast access for large lists
  val aVector = Vector(1, 2, 3)

  // Sets have no duplicates
  val aSet = Set(1, 2, 3, 4, 1, 2) // Set(1, 2, 3, 4)
  println(aSet.contains(5))
  val anAddedSet = aSet + 5 // add an element to the set and return a new set
  val aRemovedSet = aSet - 3 // remove an element from the set and return a new set

  // Ranges
  val aRange = 1 to 1000  // ranges are both boundaries inclusive
  val openEndedRange = 1 until 1000 // until creates a open right boundary range

  val twoByTwo = aRange.map(_ * 2)
  println(twoByTwo)

  // Tuples allow for values of multiple types to get contained together
  // in contrast to List which is a container for only one type of data
  val aTuple = ("Bon Jovi", "Rock", 1982, 29.99)

  // tuples can also be represented using "->" method when we want to represent just a pair
  val aTuple2 = "Bon Jovi" -> "Rock"
  println(s"Arrow Tuple - $aTuple2")

  // Maps
  // To construct literal maps, we use tuples of pairs
  val aPhonebook: Map[String, Int] = Map(
    ("Daniel", 6437812), // regular tuple
    "Jane" -> 327285 // arrow tuple
  )

  // Advanced Scala
  // Lazy Evaluation - an expression is not evaluated until it is first used
  lazy val aLazyValueWithSideEffect = {
    println("I am so lazy")
    43
  }
  // the println does not get printed to console until we use the value
  // println(aLazyValueWithSideEffect)
  // OR
  // val eagerValue = aLazyValueWithSideEffect + 1
  // they are useful in infinite collections

  // "Pseudo-Collections": Option, Try
  // Option has an apply method which can wrap an expression which could possibly give null
  // if expression gives null, the Option will be None, or else it will be Some(value)


  // Try type
  def methodWhichCanThrowException(): String = throw new RuntimeException
  // when calling this method, we need to wrap in try catch, defensive coding
  try {
    methodWhichCanThrowException()
  } catch {
    case e: Exception => "defend against exception"
  }

  // Try can wrap a method which can throw an exception
  val aTry = Try(methodWhichCanThrowException())
  // if the code went well, it will have a value under Success, or an exception if the code threw one under Failure
  val anotherStringProcessing = aTry match {
    case Failure(exception) => s"I have obtained an exception: $exception"
    case Success(value) => s"I have obtained the $value string"
  }

  // Futures for asynchronous programming
  val aFuture = Future {    // calls Future.apply to which this code block expression is passed, we can omit the parenthesis since it is just one argument
    println("Loading...")
    Thread.sleep(1000)
    println("I have computed a value")
    67
  }

  // this future will start evaluating immediately due to the implicit execution context which "apply" expects

  // future is a "collection" which contains a value when it is evaluated
  // future is composable with map, flatMap & filter

  // IMPLICITS
  // 1. Implicit Arguments
  def aMethodWithImplicitArgs(implicit arg: Int) = arg + 1 // given a method which takes an implicit argument
  implicit val myImplicitInt = 46 // we can create a value with implicit tag
  println(aMethodWithImplicitArgs)  // now when you call this method, it will look for an implicit int declared above in the scope
  // and use it

  // 2. Implicit Conversions - adding methods to existing types on which we do not have any control (like a library type)
  implicit class MyRichInteger(n: Int) { // we are wrapping the regular integer into a class with some additional methods
    def isEven(): Boolean = n % 2 == 0
  }

  // now magically on the integer type we will have isEven method even though it is not from standard library of Integer,
  // since the implicit class MyRichInteger is in the scope
  println(123.isEven()) // the compiler will search for an implicit wrapper of Integer in the scope
  // this statement turns to `new MyRichInteger(23).isEven()`



}
