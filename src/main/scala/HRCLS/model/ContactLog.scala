package HRCLS.model

import HRCLS.utils.io.underline
import java.util.Date

import jdk.jfr.Frequency
import org.bson.types.ObjectId


case class ContactLog(_id: ObjectId, start: Date, frequency: String, mode : String, outPower : String,
                    var callSign : String, rst : String, name : String, location : String, notes : String, end: Date ) {
  callSign = callSign.toUpperCase

  def apply(start : Date, frequency: String, mode : String, outPower : String,callSign : String, rst : String , name : String, location : String, notes : String, end: Date ): Unit = {
    ContactLog ( new ObjectId(), start, frequency,  mode, outPower,callSign,rst, name, location, notes, end)
  }

  override def toString(): String = {
    s"${start} - $end : $frequency[$mode]@$outPower : $rst : $callSign  $name : $location"
  }
  def toStringMultiline(): String = {
    s"$start - $end\n $frequency[$mode]@$outPower : $rst\n $callSign  $name : $location\n$notes"
  }
  def printCLIMultiline(){
    println(s"\t$start - $end")
    println(s"\t$frequency[$mode]@$outPower : $rst")
    println(s"\t$callSign  $name : $location\n\t$notes")
    underline(s"\t\tID: ${_id.toString}");
  }
}