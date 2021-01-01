package HRCLS.cli
import HRCLS.dao.callSignDAO
import HRCLS.model.CallSign
import HRCLS.utils.io._

import scala.io.StdIn

object callSignMenu {

  def showCallSignCommands(): Unit = {
    printTitle("<<>> Call Signs <<>>");
    println("<> add  \t\t::\t\tadd a new call sign to the directory");
    println("<> list  \t\t::\t\tlist all call signs in the directory");
    println("<> remove\t\t::\t\tremove a call sign from the directory");
    println("<> edit  \t\t::\t\tedit the information for a specific call sign");
    println("<> view  \t\t::\t\tview the information for a specific call sign");
    println("<> help  \t\t::\t\tlist all available commands");
    underline("<> exit  \t\t::\t\treturn to the main menu");
  }
  def showCallSignCommandsExtd(): Unit = {
    printTitle("<<>> Call Signs <<>>");
    println("<> add   \t\t::\t\tadd a new call sign to the directory");
    println("<> add [firstName] [lastName] [callSign] [city] [state]")
    println("\t\t\t\t::\t\tadd a new call sign to the directory without prompt");
    println("<> list  \t\t::\t\tlist all call signs in the directory");
    println("<> remove\t\t::\t\tremove a call sign from the directory");
    println("<> remove [callSign]");
    println("\t\t\t\t::\tremove a call sign from the directory without prompt");
    println("<> edit  \t\t::\t\tedit the information for a specific call sign");
    println("<> edit [callSign]");
    println("\t\t\t\t::\t\tedit the information for a call sign without first prompt");
    println("<> view  \t\t::\t\tview the information for a specific call sign");
    println("<> view [callSign]");
    println("\t\t\t\t::\t\tview the information for a specific call sign without prompt");
    println("<> help  \t\t::\t\tlist all available commands");
    underline("<> exit  \t\t::\t\treturn to the main menu");
  }
  def showCallSignShortcuts(): Unit = {
    printTitle("<<>> Call Signs Shortcuts <<>>");
    println("<> calls add\t::\t\tadd a new call sign to the directory");
    println("<> calls add [firstName] [lastName] [callSign] [city] [state]")
    println("\t\t\t\t::\t\tadd a new call sign to the directory without prompt");
    println("<> calls list\t::\t\t list all call signs in the directory");
    println("<> calls remove\t::\t\tremove a call sign from the directory");
    println("<> calls remove [callSign]");
    println("\t\t\t\t::\t\tremove a call sign from the directory without prompt");
    println("<> calls edit\t::\t\tedit the information for a specific call sign");
    println("<> calls edit [callSign]");
    println("\t\t\t\t::\t\tedit the information for a call sign without first prompt");
    println("<> calls view\t::\t\tview the information for a specific call sign");
    println("<> calls view [callSign]");
    println("\t\t\t\t::\tview the information for a specific call sign without prompt");
  }
  def callSignMenu(arg: String): Unit = {
    if (arg != "") {
      arg match {
        case cmdPattern(cmd, arg) if cmd.equals("add") => addCallSign(arg);
        case cmdPattern(cmd, arg) if cmd.equals("list") => listCallSigns(arg);
        case cmdPattern(cmd, arg) if cmd.equals("remove") => removeCallSign(arg);
        case cmdPattern(cmd, arg) if cmd.equals("edit") => editCallSign(arg);
        case cmdPattern(cmd, arg) if cmd.equals("view") => viewCallSign(arg);
        case default => {
          println(s"\t$default is not a recognized calls shortcut");
          showCallSignShortcuts();
        }
      }
    }
  else {
      var loop = true;
      showCallSignCommands();
      while (loop) {
        StdIn.readLine(">:") match {
          case cmdPattern(cmd, arg) if cmd.equals("add") => {
            addCallSign(arg);
            showCallSignCommands();
          }
          case cmdPattern(cmd, arg) if cmd.equals("list") => {
            listCallSigns(arg);
            showCallSignCommands();
          }
          case cmdPattern(cmd, arg) if cmd.equals("remove") => {
            removeCallSign(arg);
            showCallSignCommands();
          }
          case cmdPattern(cmd, arg) if cmd.equals("edit") => {
            editCallSign(arg);
            showCallSignCommands();
          }
          case cmdPattern(cmd, arg) if cmd.equals("view") => {
            viewCallSign(arg);
            showCallSignCommands();
          }
          case cmdPattern(cmd, arg) if cmd.equals("exit") => {
            loop = false;
          }
          case cmdPattern(cmd, arg) if cmd.equals("help") => {
            showCallSignCommandsExtd();
          }
          case "" => {};
          case default => {
            println(s"\t$default isn't a recognized command, try again");
            showCallSignCommands();
          }
        }
      }
    }
  }
  def viewCallSign(arg: String) {
    var call: CallSign = null;
    var str: String = null;
    if (arg == "")
      str = StdIn.readLine("\tEnter a call sign to view: ")
    else
      str = arg.toUpperCase
    call = callSignDAO.getCallSign(str);
    if (call == null)
      println(s"${str} could not be found in the list")
    else
      println("\t"+call.toStringMultiline());
  }
  def listCallSigns(arg: String): Unit = {
    val calls = callSignDAO.getAllCallSigns()
    if(calls.nonEmpty) {
      for (i <- 0 to calls.length - 2) {
        println(s"\t} ${calls(i).toString()}");
      }
      underline(s"\t} ${calls.last.toString()}");
    }else println("no call signs have been added to directory");
  }
  def editCallSign(arg: String): Unit = {
    var call: CallSign = null;
    var str: String = null;
    if (arg == "")
      str = StdIn.readLine("\tEnter a call sign to edit: ").toUpperCase
    else
      str = arg.toUpperCase
    call = callSignDAO.getCallSign(str);
    if (call == null)
      println(s"${str} could not be found in the directory")
    else {
      var in = StdIn.readLine(s"\tFirst Name: ${call.firstName} -> : ");
      if (in != "") call.firstName = in;
      in = StdIn.readLine(s"\tLast Name: ${call.lastName} -> : ");
      if (in != "") call.lastName = in;
      in = StdIn.readLine(s"\tCall Sign: ${call.callSign} -> : ");
      if (in != "") call.callSign = in;
      in = StdIn.readLine(s"\tCity: ${call.city} -> : ");
      if (in != "") call.city = in;
      in = StdIn.readLine(s"\tState: ${call.state} -> : ");
      if (in != "") call.state = in;
      callSignDAO.editCallSign(str, call)
      println(s"the information for ${call.callSign.toUpperCase} is being updated");
    }
  }
  def removeCallSign(arg: String): Unit = {
    var str: String = null;
    if (arg == "")
      str = StdIn.readLine("\tEnter a call sign to edit: ")
    else
      str = arg
    val call = callSignDAO.getCallSign(str);
    if (call == null)
      println(s"\t${str} could not be found in the directory")
    else {
      callSignDAO.removeCallSign(call.callSign)
      println(s"\t${call.callSign.toUpperCase} is being removed from the directory");
    };
  }
  def addCallSign(arg: String): Unit = {
    var first, last, callSign, city, state:String = "";
    var call: CallSign = null;
    if(arg == "") {
      first = StdIn.readLine(s"\tFirst Name: ");
      last = StdIn.readLine(s"\tLast Name: ");
      callSign = StdIn.readLine(s"\tCall Sign: ");
      city = StdIn.readLine(s"\tCity: ");
      state = StdIn.readLine(s"\tState: ");
      if(callSign == "") {
        println("call sign cannot be empty");
        return;
      }
    }
    else {
      val args: Array[String] = arg.split(" ");
      if (args.length == 5){
        first = args(0);
        last = args(1);
        callSign = args(2);
        city = args(3);
        state = args(4);
      }else{
        println("\tyou have used an incorrect syntax for the calls add shortcut");
        println("\t<> add [firstName] [lastName] [callSign] [city] [state]");
        println("\t\t: add a new call sign to the directory without prompt");
      }
    }
    call = callSignDAO.getCallSign(callSign);
    if (call == null) {
      callSignDAO.addCallSign( CallSign(first, last, callSign, city, state) )
      println(s"adding information for ${callSign.toUpperCase} to the directory")
    } else
      addExistingCallSign(call, first, last, callSign, city, state);
  }
  def addExistingCallSign(call: CallSign, first: String, last: String, callSign: String, city: String, state: String): Unit = {
    println(s"\t\tan entry already exists for ${call.callSign}")
    println("\t\tdo you want to update it with the new information?");
    StdIn.readLine("\t\t[Y/y] = yes || [N/n] = no: ") match {
      case s if ( s == "Y" || s == "y") => {
        call.firstName = first;
        call.lastName = last
        call.city = city;
        call.state = state;
        callSignDAO.editCallSign(call.callSign, call);
        println(s"\tthe information for ${call.callSign.toUpperCase} is being updated");
      }
      case default => {
        println(s"\tnot changing any information for ${call.callSign.toUpperCase}");
      }
    }
  }
}
