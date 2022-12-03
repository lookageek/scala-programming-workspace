package com.lookageek.scala
package adt.scott.payment

// we code up the ADT scenario Scott Wlaschin discusses here
// https://1drv.ms/u/s!AhVsnJZUFlurgbgcm3wjMtLcBp7OqA?e=nr4sPw
// this is from 4 languages talk

case class CardNumber(value: Integer)
case class CheckNumber(value: Integer)

sealed trait CardType
case object Visa extends CardType
case object Mastercard extends CardType

// PaymentMethod is an ADT we composed from multiple things
// ADT make types composable
sealed trait PaymentMethod
case object Cash extends PaymentMethod
case class Check(checkNumber: CheckNumber) extends PaymentMethod
case class CreditCardInfo(cardNumber: CardNumber, cardType: CardType) extends PaymentMethod