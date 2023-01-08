



object `Chapter-3` {
/*<script>*/
sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {
  def apply[A](as: A*): List[A] = {
    if (as.isEmpty) Nil
    else Cons(as.head, apply(as.tail: _*))
  }

  def sum(ints: List[Int]): Int = ints match {
    case Nil => 0
    case Cons(x, xs) => x + sum(xs)
  }

  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x, xs) => x * product(xs)
  }

  def tail[A](l: List[A]): List[A] = l match {
    case Nil => Nil
    case Cons(x, xs) => xs
  }

  def setHead[A](l: List[A], newHead: A) = l match {
    case Nil => List(newHead)
    case Cons(x, xs) => Cons(newHead, xs)
  }

  @annotation.tailrec
  def drop[A](l: List[A], n: Int): List[A] = l match {
    case Nil => Nil
    case Cons(_, xs) =>
      if (n <= 0) {
        l
      } else if (n == 1) {
        xs
      } else {
        drop(xs, n - 1)
      }
  }

  @annotation.tailrec
  def dropWhile[A](l: List[A], predicate: A => Boolean): List[A] = l match {
    case Nil => Nil
    case Cons(x, xs) =>
      if (predicate(x)) {
        dropWhile(xs, predicate)
      } else {
        l
      }
  }

  def append[A](a1: List[A], a2: List[A]): List[A] = a1 match {
    case Nil => a2
    case Cons(h, t) => Cons(h, append(t, a2))
  }

  def foldRight[A, B](l: List[A], base: B)(f: (A, B) => B): B = {
    l match {
      case Nil => base
      case Cons(x, xs) => f(x, foldRight(xs, base)(f))
    }
  }
}

val x: Any = List(1, 2, 3, 4, 5) match {
  case Cons(x, Cons(2, Cons(4, _))) => x
  case Nil => 42
  case Cons(x, Cons(y, Cons(3, Cons(4, _)))) => x + y
  case Cons(h, t) => h + List.sum(t)
  case _ => 101
}/*</script>*/ /*<generated>*/
def args = `Chapter-3_sc`.args$
  /*</generated>*/
}

object `Chapter-3_sc` {
  private var args$opt0 = Option.empty[Array[String]]
  def args$set(args: Array[String]): Unit = {
    args$opt0 = Some(args)
  }
  def args$opt: Option[Array[String]] = args$opt0
  def args$: Array[String] = args$opt.getOrElse {
    sys.error("No arguments passed to this script")
  }
  def main(args: Array[String]): Unit = {
    args$set(args)
    `Chapter-3`.hashCode() // hasCode to clear scalac warning about pure expression in statement position
  }
}

