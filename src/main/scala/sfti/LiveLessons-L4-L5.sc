import java.awt.geom.Ellipse2D
import java.io.{FileInputStream, InputStream}
// -------------------
// Classes and Objects
// -------------------


// if the class fields are declared as val, they cannot be modified after class instantiation
// if they are declared as var, they can be modified later
// declaring instance variables in the parenthesis like this in a class provides us with
// a free constructor for the class to init those instance variables
// also if there is any code in the class body not part of any method definition
// that code gets executed within the object constructor AFTER the initialisation of the instance variables
// val before the instance variable provides an accessor method, if you remove val, no accessor method
class Point(val x: Double, private val y: Double) {

  println(s"A new Point is born - $x, $y")
  def move(dx: Double, dy: Double) = new Point(x + dx, y + dy)
  // if a method does not take any parameters, you do not have to supply parameter parethesis
  def distanceFromOrigin = math.sqrt(x * x + y * y)
  override def toString = s"($x, $y)"

  // you can define any method with a symbol, like an operator
  // this works as a operator
  // the precedence of the operator is determined by the first character
  // and only operators which ends with colon :, it is right associative, everything
  // else is left associative
  // 1 :: 2 :: 3 :: Nil -> 1 :: (2 :: (3 :: Nil))
  // 1 * 2 * 3 * 4 -> ((1 * 2) * 3) * 4
  def *(factor: Int): Point = new Point(x * factor, y * factor)
}

val point = new Point(3, 4)

point.move(10, 20)

// if a method does not take any parameters & it is not a effectful procedure, but returns a value
// we do not have to supply the empty parenthesis
point.distanceFromOrigin


// free accessors for the class instance values
point.x

// if the instance variable is private, as y, no accessor
// point.y

// Objects
// objects are singletons, anything you declare inside object will have static values

object Accounts {
  private var lastNumber = 0
  // Note: Since we are modifying the state in this function, we are leaving
  // the empty parenthesis to be there
  def newUniqueNumber() = {
    lastNumber += 1
    lastNumber
  }
}

Accounts.newUniqueNumber()
Accounts.newUniqueNumber()

// An object extending App is like main which is executable entrypoint
object MyApp extends App {
  println("Hello")
}

// Companion Object
// a companion object for a class can hold any of the static methods, or static variables for that class
// it can also have factory methods
// it is good practice to place the companion object in the same file as its class
object Point {
  // this apply method will allow us to create Point objects without the 'new' keyword
  def apply(x: Double, y: Double) = new Point(x, y)
}

Point(3, 4)

// Exercises
// Write a class Time with read-only fields, hours and minutes, a toString method
// a before(other:Time): Boolean that checks whether this time comes before other.
// A Time object should be constructed as new Time(h, m), where h is between 0 and 23
// and m between 0 and 59. If they aren't, call throw new IllegalArgumentException
class Time(val hours: Int, val minutes: Int = 0) {
  // auxilliary constructor
  def this(h: Int) = {
    this(h, 0)
  }


  // validation when constructing the object can be put as statements
  // in the class body. But is it a right thing to do?
  if (hours < 0 || hours >= 24 || minutes < 0 || minutes >= 60) throw new IllegalArgumentException

  // all of the methods in the class body are instance methods, there is no way
  // to declare a static method here, you need to do that in companion object
  def before(other: Time): Boolean = hours < other.hours || hours == other.hours && minutes < other.minutes

  // implementing a method as a operator to get the difference between two times in minutes
  // we should actually give a time object back here? or a separate object, called Duration, as java time library does
  def -(other: Time) = hours * 60 + minutes - other.hours * 60 - other.minutes

  // this is reimplementation of before method as an operator
  def <(other: Time) = this - other < 0

  override def toString = f"${hours}%02d:${minutes}%02d"
}

object Time {
  def apply(hours: Int, minutes: Int): Time = new Time(hours, minutes)
}

val morning = new Time(9, 0)
//val crazy = new Time(3, -222)
val afternoon = new Time(16, 30)
morning.before(afternoon)
afternoon.before(morning)

// having auxilliary constructor OR default value
val noon = new Time(12)


// -----------------------------
// Package, Inheritance & Traits
// -----------------------------

// In Java, the package name is just a string, they do not have a meaning, the dots do not mean anything
// we think packages nest, but they just nest
// In Scala, the packages truly nest
// We can have code of multiple packages in a single source file
// As in java, we do not have to worry about placing the files of a package in a source directory
// of that package, but what is the best practice here?
// That means, the package declaration comes from the package statement on the top of
// source file, thats the only place the compiler will look for declared packages

// Import Statements
// import java.awt.{Color, Font} -> Import only two classes
// import java.util.{HashMap => JavaHashMap} -> Alias an import
// import java.util.{HashMap => _, _} // Hide a class when importing with wildcard
// also imports can be anywhere, and it will be only in the scope where you want it
// it is always better to limit the scope of the import


// Inheritance
// using extends keyword
// check if an object is instance of .isInstanceOf[Class]
// to cast to subclass .asInstanceOf[Class]
// classOf[Manager] for Manager.class of Java

// Superclass constructor
// class Employee(name, age, salary) extends Person(name, age)


// Traits
// Traits can have absolutely everything a class can have except constructor parameters
// there is only "extends" keyword in scala, no "implements" as differtiating between
// trait and class extending

// Mixins
// Mixins are traits with small bits of functionality which can mixed in to classes
// and even when constructing objects

// trait Logging ...
// trait FileLogging extends Logging ...
// trait ConsoleLogging extends Logging ...
// val account = SavingsAccount(balance = 50) with FileLogging
// val account2 = SavingsAccount(balance = 50) with ConsoleLogging

// Layered Traits
// Traits can have "super" call in their default implementation
// which trait will receive that "super" depends on the order in which traits are mixed in
// and order is from outer most to inner most
// val acc1 = SavingsAccount(balance = 50) with ConsoleLogger with TimestampLogger with ShortLogger


// Exercises
// Mixing the Missing Methods
// The java.awt.Rectangle has method translate, which moves rectangle by a given amount
import java.awt._
val r = new Rectangle(5, 10, 20, 30)
r.translate(10, 20)

// To make ellipses, you use the class java.awt.geom.Ellipse2D.Double
val ellipse = new Ellipse2D.Double(5, 10, 20, 30)

// but there is no translate method for ellipse!
// ellipse.translate(10, 20)
// we can fix this easily with an object mixin in scala
// we will define a trait
// there is a lot of code here, do not worry a lot about the code
trait RectangleLike {
  def setFrame(x: Double, y: Double, w: Double, h: Double): Unit
  def getX: Double
  def getY: Double
  def getWidth: Double
  def getHeight: Double
  def translate(dx: Double, dy: Double): Unit = {
    setFrame(getX + dx, getY + dy, getWidth, getWidth)
  }
  // ellipse do not have a decent toString method too
  override def toString(): String = f"${getX}:${getY}"
}

val ellipseEnhanced = new Ellipse2D.Double(5, 10, 20, 30) with RectangleLike
ellipseEnhanced.translate(10, 20) // <- mixed in the method
// this is the power of object mixins
ellipseEnhanced


// Exercise - Buffering
// In the java.io Library, one has to work hard to get services such as buffering
// new BufferedReader(new InputStreamReader(new FileInputStream("/usr/share/dict/words")))
// It should not be so hard to add buffering. Buffering is to read a chunk of bytes from the
// disk, store it into a buffer, and read from that buffer. Running out of data to read in the buffer
// should cause one more chunk read into buffer
trait Buffered extends InputStream {
  // In this trait we can see all kinds of things going on, like a class
  // all of the statements here are executed at the object construction when
  // this trait is mixed in
  private val SIZE = 1024
  private var end = 0
  private val buffer = new Array[Byte](SIZE)
  private var pos = 0

  override def read() = {
    if (pos == end) {
      end = super.read(buffer, 0, SIZE)
      pos = 0
    }

    if (pos == end) -1 else {
      pos += 1
      buffer(pos - 1)
    }
  }
}

// mixing in Buffered to FileInputStream
val wordsReader = new FileInputStream("/usr/share/dict/words") with Buffered
wordsReader.read()
wordsReader.read()

// To see when the buffer fill gets triggered, lets create a log trait and mix that in
trait Logged {
  def log(s: String) = {}
}

trait ConsoleLogger extends Logged {
  override def log(s: String) = {
    println(s)
  }
}

trait BufferedWithLogger extends InputStream with Logged {
  private val SIZE = 1024
  private var end = 0
  private val buffer = new Array[Byte](SIZE)
  private var pos = 0

  override def read() = {
    if (pos == end) {
      end = super.read(buffer, 0, SIZE)
      log("Filling up the Buffer")
      pos = 0
    }

    if (pos == end) -1 else {
      pos += 1
      log("Reading from the Buffer")
      buffer(pos - 1)
    }
  }
}

val wordsReaderLogger = new FileInputStream("/usr/share/dict/words") with BufferedWithLogger with ConsoleLogger
wordsReaderLogger.read()
wordsReaderLogger.read()