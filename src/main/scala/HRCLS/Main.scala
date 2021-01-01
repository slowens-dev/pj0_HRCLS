package HRCLS

import HRCLS.cli.CLI
import HRCLS.utils.mongo.{closeMongoClient, openMongoClient}

object Main extends App {
  openMongoClient();
  new CLI().entryPoint();
  closeMongoClient();
}
