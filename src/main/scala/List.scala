package com.lookageek.scala

sealed trait List[+A]
case object Nil extends List[Nothing]
case class Cons[+A](head: A, tail: List[A]) extends List[A]

object List {
  def sum(ints: List[Int]): Int = ints match {
    case Nil => 0
    case Cons(x, xs) => x + sum(xs)
  }

  def product(ds: List[Double]): Double = ds match {
    case Nil => 1.0
    case Cons(0.0, _) => 0.0
    case Cons(x, xs) => x * product(xs)
  }

  def tail[A](ds: List[A]): List[A] = ds match {
    case Cons(_, t) => t
    case Nil => Nil
  }

  def setHead[A](ds: List[A], newHead: A): List[A] = ds match {
    case Cons(_, t) => Cons(newHead, t)
    case Nil => Nil
  }

  def drop[A](ds: List[A], n: Int): List[A] = {
    @annotation.tailrec
    def loop(cs: List[A], i: Int): List[A] =
      if (i == 0) cs
      else loop(tail(cs), i - 1)

    loop(ds, n)
  }

  def dropWhile[A](ds: List[A], f: A => Boolean): List[A] = ds match {
    case Cons(h, t) if f(h) => dropWhile(t, f)
    case _ => ds
  }
}