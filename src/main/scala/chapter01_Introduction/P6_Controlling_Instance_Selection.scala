package chapter01_Introduction

import jdk.nashorn.internal.ir.debug.JSONWriter

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._
object P6_Controlling_Instance_Selection {
  sealed trait Shape
  case class Circle(radius: Double) extends Shape

  trait JsonWriter[-T] {
    def write(t: T): String // 如果是协变那么接收的数据则会变得更加得具体,这不是我们想要的情况
  }

  def typeOf2[T: TypeTag](t: T): universe.Type = typeOf[T]

  def write(jsonWriter: JsonWriter[Circle]): Unit = {
    println(s"jsonWriter is ${jsonWriter.getClass}")
  }

  def main(args: Array[String]): Unit = {
    val circle = List(Circle(4.0))
    val shapes: List[Shape] = circle
    shapes.foreach(println)

    val c = Circle(5.0)
    val shapWriter: JsonWriter[Shape] = t => t.toString
    val circleWriter: JsonWriter[Circle] = t => t.toString
    def format[A](value: A, writer: JsonWriter[A]): String = writer.write(value)

//    format(c, shapWriter)
//    format(c, circleWriter)
    write(shapWriter)
    write(circleWriter)

  }

  sealed trait A
  final case object B extends A
  final case object C extends A

  /**
   * The issues are:
   * 1. Will an instance defined on a supertype be selected if one is available?
   * For example, can we define an instance for A and have it work for values
   * of type B and C?
   *
   *
   * 2. Will an instance for a subtype be selected in preference to that of a
   * supertype. For instance, if we define an instance for A and B , and we
   * have a value of type B , will the instance for B be selected in preference
   * to A ?
   */


}
