package HRCLS.cli
import HRCLS.utils.io._
import HRCLS.dao.userDAO
import HRCLS.model.User

import scala.io.{Source, StdIn}

object infoMenu {
  def showInfoCommands(): Unit = {
    printTitle("<<>> Personal Information <<>>");
    println("<> view \t\t::\t\tview your saved personal information");
    println("<> edit \t\t::\t\tedit your saved personal information");
    println("<> edit_file \t::\t\tupload personal information from a csv");
    println("<> help \t\t::\t\tlist all available commands");
    underline("<> exit \t\t::\t\treturn to the main menu");
  }
  def showInfoCommandsExtd(): Unit = {
    printTitle("<<>> Personal Information <<>>");
    println("<> view \t\t::\t\tview your saved personal information");
    println("<> edit \t\t::\t\tedit your saved personal information");
    println("<> edit [firstName] [lastName] [callSign] [city] [state]");
    println("        \t\t::\t\tedit personal information without the prompt");
    println("<> edit_file\t::\t\tupload personal information from a CSV");
    println("\t\t\t\t::\t\tfirstName, lastName, callSign, City, State");
    println("<> help \t\t::\t\tlist all available commands");
    underline("<> exit \t\t::\t\treturn to the main menu");
  }
  def showInfoShortcuts(): Unit ={
    printTitle("<<>> Personal Information Shortcuts <<>>");
    println("<> info view \t::\t\tview your saved personal information");
    println("<> info edit \t::\t\tedit your saved personal information");
    println("<> info edit [firstName] [lastName] [callSign] [city] [state]");
    println("\t\t\t\t::edit personal information without the prompt");
    println("<> info edit_file \t::\t\tupload personal information from a CSV");
    underline("\t\t\t\t::\t\tfirstName, lastName, callSign, City, State");
  }
  def infoMenu(arg: String): Unit = {
    if (arg != ""){
      arg match{
        case cmdPattern(cmd, arg) if cmd.equals("view") => {
          viewInfo();
        }
        case cmdPattern(cmd, arg) if cmd.equals("edit") => {
          editInfo(arg);
        }
        case cmdPattern(cmd, arg) if cmd.equals("edit_file") => {
          editInfoFromFile(arg);
        }
        case default => {
          println(s"\t$default isn't a recognized info shortcut");
          showInfoShortcuts();
        }
      }
    }
    else{
      var loop = true;
      showInfoCommands()
      while (loop) {
        StdIn.readLine(">:") match {
          case cmdPattern(cmd, arg) if cmd.equals("view") => {
            viewInfo();
            showInfoCommands()
          }
          case cmdPattern(cmd, arg) if cmd.equals("edit") => {
            editInfo(arg);
            showInfoCommands()
          }
          case cmdPattern(cmd, arg) if cmd.equals("edit_file") => {
            editInfoFromFile(arg);
            showInfoCommands()
          }
          case cmdPattern(cmd, arg) if cmd.equals("exit") => {
            loop = false;
          }
          case cmdPattern(cmd, arg) if cmd.equals("help") => {
            showInfoCommandsExtd()
          }
          case "" => {};
          case default => {
            println(s"\t$default isn't a recognized command, try again");
            showInfoCommands();
          }
        }
      }
    }
  }
  def viewInfo(): Unit = {
    val user: User = userDAO.getFromMongo()
    println(s"\tCall Sign: ${user.callSign}");
    println(s"\tName: ${user.firstName} ${user.lastName}");
    println(s"\tLocation: ${user.city}, ${user.state}");
  }
  def editInfoFromFile(arg: String): Unit = {
    val file = if( arg == "") StdIn.readLine("Enter a CSV file to import your information from: ") else arg;
    try {
      val doc = Source.fromFile(file)
      val lines = doc.getLines().toArray
      doc.close()
      val fields = lines(0).split(",");
      if (fields.length == 5) {
        userDAO.pushToMongo(User(fields(0), fields(1), fields(2), fields(3), fields(4)))
      }
      else {
        println(s"$file isn't a properly formatted file");
      }
    }
    catch {
      case e: ClassNotFoundException => {
        println(s"$file isn't a properly formatted file");
      }
    }

  }
  def editInfo(arg: String): Unit = {
    val user: User = userDAO.getFromMongo()
    if( arg == "" ){
      println("\tenter a new value or press [ENTER] to skip");
      var in = StdIn.readLine(s"\tFirst Name: ${user.firstName} -> : ");
      if (in != "") user.firstName = in.capitalize;
      in = StdIn.readLine(s"\tLast Name: ${user.lastName } -> : ");
      if (in != "") user.lastName = in.capitalize;
      in = StdIn.readLine(s"\tCall Sign: ${user.callSign} -> : ");
      if (in != "") user.callSign = in.toUpperCase;
      in = StdIn.readLine(s"\tCity: ${user.city} -> : ");
      if (in != "") user.city = in.capitalize;
      in = StdIn.readLine(s"\tState: ${user.state} -> : ");
      if (in != "") user.state = in.toUpperCase;
    }
    else{
      val args: Array[String] = arg.split(" ");
      if (args.length == 5){
        user.firstName = args(0).capitalize
        user.lastName = args(1).capitalize
        user.callSign = args(2).toUpperCase
        user.city = args(3).capitalize
        user.state = args(4).toUpperCase
      }else{
        println("you have used an incorrect syntax for the info edit shortcut");
        println("<> edit [firstName] [lastName] [callSign] [city] [state]");
        println("   : edit personal information without the prompt");
      }
    }
    userDAO.pushToMongo(user);
    println(user.toString())
  }

}
