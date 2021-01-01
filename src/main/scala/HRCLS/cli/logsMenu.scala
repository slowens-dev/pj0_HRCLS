package HRCLS.cli

import java.time.{LocalDate, ZoneId}
import java.util.Date

import HRCLS.dao.contactLogDAO
import HRCLS.model.ContactLog
import HRCLS.utils.io._
import org.bson.types.ObjectId

import scala.io.StdIn

object logsMenu {
  def showLogsCommands(): Unit = {
    printTitle("<<>> Contact Logs <<>>");
    println("<> add : log a new contact");
    println("<> list: list all contacts in the log");
    println("<> list_call [callSign] : list all contacts in the log with the given call sign");
    println("<> remove : remove a contact from the log");
    println("<> remove_call [callSign] : remove all contacts from the log with the given call sign");
    println("<> update [ref#] : update a contact in the log");
    println("<> help : list all available commands");
    underline("<> exit : return to the main menu");
  }
  def showLogsCommandsExtd(): Unit = {
    printTitle("<<>> Contact Logs <<>>");
    println("<> add : log a new contact");
    println("<> list: list all contacts in the log");
    println("<> list_call [callSign] : list all contacts in the log with the given call sign");
    println("<> remove [log ID]: remove a contact from the log");
    println("<> remove_call [callSign] : remove all contacts from the log with the given call sign");
    println("<> update [log ID] : update a contact in the log");
    println("<> help : list all available commands");
    underline("<> exit : return to the main menu");
  }
  def logsMenu(arg: String): Unit = {
    if (arg == "") {
      //no shortcut command passed => start loop
      var loop = true;
      showLogsCommands();
      while (loop) {
        StdIn.readLine(">:") match {
          case cmdPattern(cmd, arg) if cmd.equals("add") => {
            addContactLog(arg);
            showLogsCommands();
          }
          case cmdPattern(cmd, arg) if cmd.equals("list") => {
            listLogs(arg);
            showLogsCommands();
          }
          case cmdPattern(cmd, arg) if cmd.equals("list_call") => {
            listLogsByCall(arg);
            showLogsCommands();
          }
          case cmdPattern(cmd, arg) if cmd.equals("remove") => {
            removeLog(arg);
            showLogsCommands();
          }
          case cmdPattern(cmd, arg) if cmd.equals("update") => {
            updateLog(arg);
            showLogsCommands();
          }
          case cmdPattern(cmd, arg) if cmd.equals("help") => {
            showLogsCommandsExtd()
          }
          case cmdPattern(cmd, arg) if cmd.equals("exit") => {
            loop = false;
          }
          case cmdPattern(cmd, arg)  => {
            println(s"\t$cmd isn't a recognized command, try again");
            showLogsCommands();
          }
          case "" => {}
        }
      }
    }
    else {
      println(s"shortcuts logbook + $arg")
    }
  }
  def addContactLog(arg: String): Unit = {
    var in, freq, mode, power, call, rst, name, location, notes : String = null;
    var next : Boolean = false;
    var y, m, d, hr, mn : Int = 0;
    var start, end : Date = null;
    println("\tPress [ENTER] to leave a value blank");
    while ( !next ){ // GET DATE
      in = StdIn.readLine("\tDate MM/DD/YYYY (TODAY if left blank): ");
      if (in == "") {
        val date = java.time.LocalDate.now();
        y = date.getYear-1900
        m = date.getMonthValue-1
        d = date.getDayOfMonth
        next = true;
      } else {
        val arr = in.split("/")
        if (arr.length == 3) {
          y = arr(2).toInt
          m = arr(1).toInt
          d = arr(1).toInt
          next = true;
        } else {
          println("\t\tinvalid date entered");
        }
      }
    } ;next = false;
    while ( !next ) {// GET START TIME
      in =   StdIn.readLine("\tStart Time HH:mm [0-24]:[0-60] (NOW if left blank): ");
      if (in == "") {
        val time = java.time.LocalTime.now();
        hr = time.getHour
        mn = time.getMinute
        next = true;
      }else {
        val arr = in.split(":");
        if (arr.length == 2) {
          hr = arr(0).toInt
          mn = arr(1).toInt
          next = true;
        }else {
          println("\t\tinvalid time entered");
        }
      }
    } ;next = false;
    start = new Date(y, m, d, hr, mn)
    in = StdIn.readLine("\tFrequency : ");
    freq = if ( in != "" )in  else "none";
    in = StdIn.readLine("\tMode : ");
    mode = if ( in != "" )in  else "none";
    in =  StdIn.readLine("\tOutput Power (Watts) : ");
    power = if ( in != "" )in  else "none";
    in =   StdIn.readLine("\tCall Sign : ");
    if ( in != "" ) call = in;
    else{ println("\tCall Sign cannot be empty"); return;}
    in =   StdIn.readLine("\tReadability/Strength/Tone (5-9 if blank) : ");
    rst = if ( in != "" )in  else "5-9";
    in =  StdIn.readLine("\tName : ");
    name = if ( in != "" )in  else "none";
    in =   StdIn.readLine("\tLocation : ");
    location = if ( in != "" )in  else "none";
    in =   StdIn.readLine("\tNotes : ");
    notes = if ( in != "" )in  else "none";
    while ( !next ) {// GET START TIME
      in =   StdIn.readLine("\tEnd Time HH:mm [0-24]:[0-60] (NOW if left blank): ");
      if (in == "") {
        val time = java.time.LocalTime.now();
        hr = time.getHour
        mn = time.getMinute
        next = true;
      }else {
        val arr = in.split(":");
        if (arr.length == 2) {
          hr = arr(0).toInt
          mn = arr(1).toInt
          next = true;
        }else {
          println("\t\tinvalid time entered");
        }
      }
    } ;next = false;
    end = new Date(y, m, d, hr, mn)
    val log = ContactLog (new ObjectId(), start , freq,  mode, power,call, rst, name, location, notes, end)
    contactLogDAO.addLog(log);
    println(s"Logging a contact with ${log.callSign} at ${log.start} at ${log.start}");
  }
  def listLogs(arg: String): Unit = {
    val logs = contactLogDAO.getAllLogs();
    for( log <- logs ) {
      log.printCLIMultiline()
    }
  }
  def listLogsByCall(arg: String): Unit = {
    val logs = contactLogDAO.getLogsByCall(arg);
    for( log <- logs ) {
      log.printCLIMultiline()
    }
  }
  def removeLog(arg: String): Unit = {
    val log = contactLogDAO.removeLog(arg);
    if (log == null) {
      underline(s"$arg is not associated with any logs");
    }else {
      println(s"\t$log")
      underline(s"\tdeleting ${log._id}");
    }
  }
  def updateLog(arg: String): Unit = {
    val inLog = contactLogDAO.getLog(arg);
    if (inLog == null)
      underline(s"$arg is not associated with any logs");
    else {
      var freq = inLog.frequency; var mode = inLog.mode;
      var power =  inLog.outPower; var call = inLog.callSign;
      var rst = inLog.rst;  var name = inLog.name;
      var location = inLog.location;  var notes = inLog.notes;
      var start = inLog.start; var end = inLog.`end`;
      var in : String = null;
      var startLDT = start.toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime;
      val endLDT = end.toInstant.atZone(ZoneId.systemDefault()).toLocalDateTime;
      var y : Int = startLDT.getYear-1900;
      var m : Int= startLDT.getMonthValue-1;
      var d : Int= startLDT.getDayOfMonth;
      var hr, mn : Int = 0;
      var next : Boolean = false;
      println("\tPress [ENTER] to leave a value unchanged");
      while ( !next ){ // GET DATE
        in = StdIn.readLine(s"\tDate MM/DD/YYYY : ${m+1}/$d/${y+1900} =>:  ");
        if (in == "")
          next = true;
        else {
          val arr = in.split("/")
          if (arr.length == 3) {
            y = arr(2).toInt
            m = arr(1).toInt
            d = arr(1).toInt
            next = true;
          } else {
            println("\t\tinvalid date entered");
          }
        }
      }; next = false;
      hr = startLDT.getHour;
      mn = startLDT.getMinute;
      while ( !next ) {// GET START TIME
        in =   StdIn.readLine(s"\tStart Time HH:mm [0-24]:[0-60]: $hr:$mn =>: ");
        if (in == "")
          next = true;
        else {
          val arr = in.split(":");
          if (arr.length == 2) {
            hr = arr(0).toInt
            mn = arr(1).toInt
            next = true;
          }else {
            println("\t\tinvalid time entered");
          }
        }
      } ;next = false;
      start = new Date(y, m, d, hr, mn);
      in = StdIn.readLine(s"\tFrequency : $freq =>: ");
      if ( in != "" ) freq = in;
      in = StdIn.readLine(s"\tMode : $mode =>: ");
      if ( in != "" ) mode = in;
      in = StdIn.readLine(s"\tOutput Power (Watts) : $power =>: ");
      if ( in != "" ) power = in;
      in = StdIn.readLine(s"\tCall Sign : $call =>: ");
      if ( in != "" ) call = in;
      in = StdIn.readLine(s"\tReadability/Strength/Tone : $rst =>: ");
      if ( in != "" ) rst = in;
      in =  StdIn.readLine(s"\tName : $name =>: ");
      if ( in != "" ) name = in;
      in = StdIn.readLine(s"\tLocation : $location =>: ");
      if ( in != "" ) location = in;
      in = StdIn.readLine("\tNotes :");
      if ( in != "" ) notes = in;
      hr = endLDT.getHour
      mn = endLDT.getMinute
      while ( !next ) {// GET START TIME
        in =   StdIn.readLine(s"\tEnd Time HH:mm [0-24]:[0-60] : $hr:$mn =>: ");
        if (in == "") {
          next = true;
        }else {
          val arr = in.split(":");
          if (arr.length == 2) {
            hr = arr(0).toInt
            mn = arr(1).toInt
            next = true;
          }else {
            println("\t\tinvalid time entered");
          }
        }
      }
      end = new Date(y, m, d, hr, mn);
      val outLog = ContactLog (inLog._id, start , freq,  mode, power,call, rst, name, location, notes, end)
      contactLogDAO.updateLog(outLog)
      upperline(s"\t$outLog");
      println(s"\t$arg has been updated");
    }
  }
}
