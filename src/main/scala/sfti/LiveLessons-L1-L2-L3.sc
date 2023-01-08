import java.net.URL
// ---------------------------------
// VARIABLES AND TYPES, SCALA BASICS
// ---------------------------------

// went over val and var


1.to(10) // this "to" method is in RichInt (if you want to look up the docs)

// java.lang.String is used as is in scala
// but there are over 100 methods on strings defined in StringOps, like this below one called intersect
"hello".intersect("world")


// we can call methods as infix operators which only works for methods which have just one parameter
1 to 10

1 + 10
// is same as
1.+(10)


// so all the arithematic operators are just methods
// ++ and -- operators do not exist in scala, but you can do this
var counter = 10
counter += 1



// scala has methods, functions and static methods
import scala.math._

sqrt(2)  // function
BigInt.probablePrime(100, scala.util.Random)

// calling methods with any parameters, you can omit the calling parenthesis when it is a accessor, but if it is a mutator you put the empty parenthesis
"Hello".distinct
"Hello".length



// in scala, there could be a method on an object which could be called without mentioning the name like

"Hello"(4)

// This is actually called the charAt method of the string
// this method call way is a syntatic sugar for calling apply method
"Hello".apply(4)
// so the "apply" is to take the parameters given in the parenthesis and apply them to the object

// in scala, the import statements can be anywhere, in the right context, like in python, it need not be required at the top of the file like in java

// applying a function to all elements of a range
(1 to 10).map(sqrt(_))
// here _ represents the each element of the range passed into sqrt

"Mississippi".permutations.toArray
// just calling permutations is a lazy op and returns an iterator
// we can call toArray like we call collect() in java



// weird flex in scala
"ABC".sum
// vs
'A' + 'B' + 'C'
// here the sum method returns the character at 198, but adding individual chars gives integer back
('A' + 'B' + 'C').toChar



// --------------------------------
// Control Structures and Functions
// --------------------------------

// if is an expression
if (true) 1 else -1

// the expression return value will be supertype of both the branches, which in this case is Any
if (true) "hello" else 1

// but if there is only one branch
if (true) "something"
// it is still Any, because, the if expression has to return something if it evaluates to false
if (false) "something"
// it returns Unit value - ().
// Unit is a type which has just one value - ()


// Block expressions
// the value of the block expression will be the value of last expression in the block
val blockExp = {
  val x = 1
  val y = 5
  x * y
}

// if the block has an assignment as the last statement, the assignment statement returns a unit value, that will be the value of that entire block expression
val blockExp2 = {
  val x = 1
  val y = 5
  val z = x * y
}


// For Loops
// scala only has the for-each loop
val n = 10
for (i <- 1 to n) println(i)
// note that we do not have to use "val" before i variable

// we can put multiple generators inside the for loop
// here for each of i, we will iterate over all of j
for (i <- 1 to 3; j <- 1 to 3) println("i: " + i + " j: " + j)

// if guard clause in for loop
for (i <- 1 to 3; j <- 1 to 3 if i != j) println(i + j)

// yield in the for loop as the expression returns a list
for (i <- 1 to 10) yield i % 10


// Defining Functions
// the return type is inferred here
def abs(x: Double) = if (x >= 0) x else -x

// but for a recursive function, it will not be able to infer and leads to a compile error
// check by removing the function return type you will see
def fac(n: Int): Int = if (n <= 0) 1 else n * fac(n - 1)


// a procedure in scala, which is used for its effects rather than a return value need not have at = sign
// actually the good practice I am seeing everywhere is to never use this way of declaring a function, even IDE autocompletes to use :Unit =
// so this is a bad thing from the SFTI
// OK he tells to use the best practice
def printString(s: String) {
  println(s)
}

printString("h")


// BUT if you do a mistake of omitting = sign when you need it like here
def fac2(n: Int) {
  var r = 1
  for (i <- 1 to n) r = r * i
  r
}
// here the return type is marked as Unit and it will return unit value
fac2(4)


// Named Arguments
fac2(n = 4)

def threeArguments(arg1: String, arg2: String, arg3: String): Unit = {
  println(s"$arg1 $arg2 $arg3")
}

threeArguments(arg3 = "Here", arg2 = "There", arg1 = "Where")
// Standard Stuff


// Default Arguments
def defaultArgs(arg1: String = "Hello", arg2: String): Unit = {
  println(s"$arg1, $arg2")
}

defaultArgs(arg2 = "Aparna")
// Please do not do this


// Varargs
def sum(args: Int*): Int = {
  var result = 0
  for (arg <- args) result += arg
  result
}

sum(1, 2, 3)

// unpack a array to pass to varags
val arr = Array(1, 2, 3)

sum(arr: _*)

// more apparent need in recursive calls
def recursiveSum(args: Int*): Int = {
  if (args.length == 0) 0
  else args.head + recursiveSum(args.tail: _*)
}


// Exercises
// Write a function that tests whether a character is a vowel
def isVowel(ch: Char): Boolean = {
  val allVowels = "aeiou"
  allVowels.contains(ch)
}

isVowel('t')
isVowel('a')

// Write a function that, given a string, returns a string of all of its vowels. Use a for loop
def vowels(s: String): String = {
  val allVowels = "aeiou"
  s.filter(ch => allVowels.contains(ch))
}

vowels("hello")


// Use a for ... yield
def vowelsYield(s: String): String = {
  val allVowels = "aeiou"
  for (ch <- s if allVowels.contains(ch)) yield ch
}

vowelsYield("hello")

// something interesting to note here is in both vowels and vowelsYield, the language converted the resulting iterator into the string
// - in filter case it took the 'stream' and made a string out of it
// - in yield case where it returns a sequence, it created a string


// ------------------------
// Arrays, Maps, and Tuples
// ------------------------


// empty array
val nums = new Array[Int](10)


// with literals
val strings = Array("Hello", "World")

// array items are accessed by parenthesis
strings(0)

// val is really just for the binding, you can modify the contents of the array, but its a bad idea to do so
strings(0) = "Goodbye"
// no one is stopping from above from happening

// for comprehension to iterate over elements for (a <- element)
// to traverse array indices, use for (i <- 0 until a.length)
// "to" method is both ends inclusive
// "until" is right end open

// Array is of fixed length, in Java you would use ArrayList
// in scala you have ArrayBuffer

import scala.collection.mutable.ArrayBuffer
val b = new ArrayBuffer[Int]
b += 1
b += (1, 2, 3, 5)
b ++= Array(8, 13, 21)

// toArray and toBuffer methods to convert iterators into array and arraybuffer

// Maps
// immutable map
val scores = Map("Alice" -> 10, "Bob" -> 3, "Cindy" -> 8)

// not possible to add or modify anything in the immutable map
// scores("Cindy") = 4

// to modify an immutable map use the + sign
scores + ("Cindy" -> 4)

// the arrow operator creates a pair
"Me" -> "Awesome"
// so a Map is a collection of pairs, but with their first elements indexed into a hashtable for O(1) lookup

// if a key is not present, it will raise an Exception, so better to use getOrElse method

// mutable map
val scoresMutable = scala.collection.mutable.Map("Alice" -> 10, "Bob" -> 3, "Cindy" -> 8)
scoresMutable("Jack") = 5 // add a new element to the map
scoresMutable += ("Bob" -> 10, "Fred" -> 7, "6" -> 8) // add a bunch of elements to the map
scoresMutable -= "Bob" // remove an element


// for comprehension for ((k, v) <- map)
// for yield to get a new map for ((k, v) <- map) yield (v, k)
// for yield always returns the same type as the input collection


// Tuples
// can collect values of different types, compared to array / arraybuffer where all the values need to be of same type
// but tuples are of fixed size
val t = (1, 3.14, "Fred")
// access the components with ._n
t._2
// note that the tuples are 1-indexed

// tuple destructuring
val (_, second, third) = t

// Exercises
// Given an arraybuffer of positive and negative integers, remove all but the first negative number

// using two tracking variables
def removeExceptFirstNegative(arr: ArrayBuffer[Int]) = {
  var needToRemove = false
  var index = 0
  while (index < arr.length) {
    if (arr(index) < 0) {
      if (needToRemove) {
        arr.remove(index)
      } else {
        needToRemove = true
        index += 1
      }
    } else {
      index += 1
    }
  }
}

val arrBuf = ArrayBuffer(1, -1, 2, -2)
removeExceptFirstNegative(arrBuf)
arrBuf

// using for ... yield to get all the negative indexes first
def removeExceptFirstNegativeYield(arr: ArrayBuffer[Int]) = {
  val negativeIndexes = for (idx <- 0 until arr.length; if arr(idx) < 0) yield idx

  // drop will remove the number of elements and returns a new immutable sequence
  val indexesToRemove = negativeIndexes.drop(1)

  for (i <- indexesToRemove.reverse) arr.remove(i)
}

val arrBuf1 = ArrayBuffer(1, -1, 2, -2)
removeExceptFirstNegative(arrBuf1)
arrBuf1


// not an inplace modification of the array buffer
def removeExceptFirstNegativeYieldNotInplace(arr: ArrayBuffer[Int]): Seq[Int] = {
  val negativeIndexes = for (idx <- 0 until arr.length; if arr(idx) < 0) yield idx

  // drop will remove the number of elements and returns a new immutable sequence
  val indexesToRemove = negativeIndexes.drop(1)

  for (i <- indexesToRemove.reverse) yield arr(i)
}

// Word Count
// count words from a text files using a mutable map
val in = new java.util.Scanner(new URL("http://horstmann.com/presentations/livelessons-scala-2016/alice30.txt").openStream)

// this using a mutable map
val countMutable = scala.collection.mutable.Map[String, Int]()

while (in.hasNext) {
  val word = in.next
  countMutable(word) = countMutable.getOrElse(word, 0) + 1
}

countMutable


// this using a immutable map
val in2 = new java.util.Scanner(new URL("http://horstmann.com/presentations/livelessons-scala-2016/alice30.txt").openStream)

var countImmutable = scala.collection.immutable.Map[String, Int]()  // if the map is immutable we have to make the variable var

while (in2.hasNext) {
  val word = in2.next
  countImmutable = countImmutable + (word -> (countImmutable.getOrElse(word, 0) + 1))
}

countImmutable

// Group By
// Cluster an array of words with their first letter
val words = Array("Mary", "had", "a", "little", "lamb")
words.groupBy(_.substring(0, 1))
words.groupBy(_.length)


// Partition function, returns tuples
val (first, last) = "New York".partition(_.isUpper)

// rewrite the remove all negatives except first using partition
// by doing this we will lose out on the ordering
val arrBuf1 = ArrayBuffer(1, -1, 2, -2)
val (negativePart, positivePart) = arrBuf1.partition(_ < 0)
positivePart += negativePart(0)

positivePart

// Zips
// zips two collections to produce pairs
val col1 = Array(1, 2, 3)
val col2 = Array(5, 4, 6, 7)

col1.zip(col2)