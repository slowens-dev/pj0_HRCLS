package HRCLS.cli
import HRCLS.cli.callSignMenu.callSignMenu
import HRCLS.cli.infoMenu.infoMenu
import HRCLS.cli.logsMenu.logsMenu
import HRCLS.utils.io._

import scala.io.StdIn

object mainMenu {
  def showMainCommands(): Unit = {
    printTitle("<<>> Main Menu <<>>");
    println("<> calls\t\t::\t\tgo to the call signs directory");
    println("<> info \t\t::\t\tupdate personal information");
    println("<> logs \t\t::\t\tlog a contact or review previous contacts");
    println("<> help \t\t::\t\tlist all available commands");
    underline("<> exit \t\t::\t\texit the application");
  }
  def showMainCommandsExtd(): Unit = {
    printTitle("<<>> Main Menu <<>>");
    println("<> calls    \t::\t\tgo to the call signs directory");
    println("<> calls add\t::\t\tadd a new call sign to the directory");
    println("<> calls add [firstName] [lastName] [callSign] [city] [state]")
    println("\t\t\t\t::\t\tadd a new call sign to the directory without prompt");
    println("<> calls list\t::\t\tlist all call signs in the directory");
    println("<> calls remove\t::\t\tremove a call sign from the directory");
    println("<> calls remove [callSign]")
    println("\t\t\t\t::\t\tremove a call sign from the directory without prompt");
    println("<> calls edit\t::\t\tedit the information for a specific call sign");
    println("<> calls edit [callSign]")
    println("\t\t\t\t::\t\tedit the information for a call sign without first prompt");
    println("<> calls view\t::\t\tview the information for a specific call sign");
    println("<> calls view [callSign]")
    println("\t\t\t\t::\t\tview the information for a specific call sign without prompt");
    println("<> info     \t::\t\tupdate personal information");
    println("<> info view\t::\t\tview your saved personal information");
    println("<> info edit\t::\t\tedit your saved personal information");
    println("<> info edit [firstName] [lastName] [callSign] [city] [state]");
    println("\t\t\t\t::\t\tedit personal information without the prompt");
    println("<> logs\t\t\t::\t\tlog a contact or review previous contacts");
    println("<> help\t\t\t::\t\tlist all available commands");
    underline("<> exit\t\t\t::\t\texit the application");
  }
  def mainMenu(): Unit = {
    var loop = true;
    println("============================================");
    println("       <<>> Welcome to H.R.C.L.S <<>>       ");
    println("<<>> Ham Radio Contact Logging Software <<>>")
    println("         <<>> Created by KO4FNN <<>>        ")
    println("============================================");
    showMainCommands();
    while( loop ) {
      StdIn.readLine(">:") match {
        case cmdPattern(cmd, arg) if cmd.equals("calls") => {
          callSignMenu(arg);
          showMainCommands();
        }
        case cmdPattern(cmd, arg) if cmd.equals("info") => {
          infoMenu(arg);
          showMainCommands();
        }
        case cmdPattern(cmd, arg) if cmd.equals("logs") => {
          logsMenu(arg);
          showMainCommands();
        }
        case cmdPattern(cmd, arg) if cmd.equals("help") => {
          showMainCommandsExtd();
        }
        case cmdPattern(cmd, arg) if cmd.equals("exit") => {
          printTitle("<<>> exiting H.R.C.L.S goodbye <<>>");
          loop = false;
        }
        case "" => {};
        case cmdPattern(cmd, arg)  => {
          println(s"\t$cmd isn't a recognized command, try again");
          showMainCommands();
        }
      }
    }
  }
}
