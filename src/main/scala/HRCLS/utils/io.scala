package HRCLS.utils

import java.time.format.DateTimeFormatter.ofPattern

import scala.util.matching.Regex

object io {
  val timeFormat = ofPattern("HH:mm")
  val cmdPattern: Regex = "(\\w+)\\s*(.*)".r();

  def printTitle(str: String): Unit = {
    val bracket = "=" * str.length
    printf("%s\n%s\n%s\n", bracket, str, bracket);
  }
  def upperline(str: String): Unit = {
    val bracket = "=" * str.length
    printf("%s\n%s\n", bracket, str);
  }
  def underline( str: String): Unit = {
    val bracket = "=" * str.length
    println(s"$str\n$bracket");
  }
}
