



object `Chapter-4` {
/*<script>*/import scala.{Option => _, Either => _, _}

sealed trait Option[+A] {
  // map on Option
  def map[B](f: A => B): Option[B] = this match {
    case Some(a) => Some(f(a))
    case None => None
  }

  // flatMap peels off one layer of Option when an option is passed to another method
  def flatMap[B](f: A => Option[B]): Option[B] = this match {
    case Some(a) => f(a)
    case None => None
  }

  def flatMap1[B](f: A => Option[B]): Option[B] = {
    // call map to get Option[Option[B]] unwrap one option using getOrElse
    // hence "flatMap" we are flattening out one Option
    map(f) getOrElse None
  }

  // getOrElse will unpack the option and if it is None then provide a default value
  // getOrElse returns the result inside the Some case of the Option, or if the Option
  // is None, returns the given default value.
  // B is equal to or a supertype of A here with "B >: A",
  // hence the default value can be either of type A
  // or of its supertype
  // default: => B means that default is evaluated to a value only when it is needed
  def getOrElse[B >: A](default: => B): B = this match {
    case Some(a) => a
    case None => default
  }

  // orElse will provide the Option if it is not None, else a default
  // orElse returns the first Option if it’s defined; otherwise, it returns the second
  // Option.
  // in the implementation, we map the current instance to Some(instance)
  // at this point after map executes we either have
  // - Some(Some(value))
  // - None
  // getOrElse will peel out the outer "Some"
  // if not it will provide the default option
  def orElse[B >: A](ob: => Option[B]): Option[B] =
    this map (Some(_)) getOrElse ob

  // the above implementation is quite understandable here
  def orElse1[B >: A](ob: => Option[B]): Option[B] = this match {
    case None => ob
    case _ => this
  }

  // convert a Some to None if value does not satisfy the predicate
  def filter(f: A => Boolean): Option[A] = this match {
    case Some(a) if f(a) => this
    case _ => None
  }

  // filter can also be written in terms of flatMap
  def filter1(f: A => Boolean): Option[A] =
    flatMap(a => if (f(a)) Some(a) else None)
}
case class Some[+A](get: A) extends Option[A]
case object None extends Option[Nothing]


// 4.2 implementing variance function in terms of flatmap
def variance(xs: Seq[Double]): Option[Double] = {

  def mean(ys: Seq[Double]): Option[Double] = {
    if (ys.isEmpty) None
    else Some(ys.sum / ys.length)
  }

  mean(xs) flatMap (m => mean(xs.map(x => math.pow(x - m, 2))))
}

// 4.3 map2 to call a non-option function from a option context
// for a two-argument method
def map2[A,B,C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] = (a, b) match {
    case (Some(x), Some(y)) => Some(f(x, y))
    case _ => None
}

// this is map2 implementation from book
def map21[A,B,C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
  a flatMap (aa => b map (bb => f(aa, bb)))

// map2 using for comprehensions
def map22[A,B,C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
  for {
    aa <- a
    bb <- b
  } yield f(aa, bb)

// 4.4 List[Option[A]] -> Option[List[A]] called sequence, a common pattern
def sequence[A](a: List[Option[A]]): Option[List[A]] = a match {
  case Nil => Some(Nil)
  case h :: t => h flatMap (hh => sequence(t) map (hh :: _))
}

// 4.5 traverse, to avoid a 2-pass scenario when applying a function a list and then
// using sequence
def traverse[A, B](a: List[A]) (f: A => Option[B]): Option[List[B]] = a match {
  case Nil => Some(Nil)
  case h :: t => map2(f(h), traverse(t)(f))(_ :: _)
}

def sequenceViaTraverse[A](a: List[Option[A]]): Option[List[A]] = traverse(a)(x => x)

// ---------- EITHER TYPE ----------
sealed trait Either[+E, +A]
case class Left[+E](value: E) extends Either[E, Nothing]
case class Right[+A](value: A) extends Either[Nothing, A]

// mean using Either
def meanEither(xs: IndexedSeq[Double]): Either[String, Double] =
  if (xs.isEmpty)
    Left("mean of empty List!")
  else
    Right(xs.sum / xs.length)/*</script>*/ /*<generated>*/
def args = `Chapter-4_sc`.args$
  /*</generated>*/
}

object `Chapter-4_sc` {
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
    `Chapter-4`.hashCode() // hasCode to clear scalac warning about pure expression in statement position
  }
}

