package HRCLS.dao

import HRCLS.model.ContactLog
import HRCLS.utils.mongo.{getMongoCollectionWithCodec, getResults}
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.bson.codecs.Macros.createCodecProvider
import org.mongodb.scala.{MongoClient, MongoCollection}
import org.mongodb.scala.model.Filters.equal

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object contactLogDAO {
  val codec  = fromRegistries(fromProviders(classOf[ContactLog]), MongoClient.DEFAULT_CODEC_REGISTRY);
  val collection : MongoCollection[ContactLog] = getMongoCollectionWithCodec[ContactLog]("HRCLS", "logs", codec);

  def getLog(id : String): ContactLog = {
    try {
      getResults(collection.find(equal("_id", new ObjectId(id))).first)(0)
    }catch {
      case e => null
    }
  }
  def getAllLogs(): Seq[ContactLog] = {
    getResults(collection.find());
  }
  def getLogsByCall(call: String ): Seq[ContactLog] = {
    getResults(collection.find(equal("callSign", call.toUpperCase)));
  }
  def addLog(log: ContactLog): Unit = {
    collection.insertOne(log).toFuture().onComplete {
      case Success(v) => "add success";
      case Failure(e) => e.printStackTrace();
    }
  }
  def removeLog(id : String) = {
    try {
      getResults(collection.findOneAndDelete(equal("_id", new ObjectId(id) ) ) ).head
    }catch {
      case e => null
    }
  }
  def updateLog(log: ContactLog): Unit = {
    collection.findOneAndReplace( equal("_id", log._id), log).toFuture().onComplete {
      case Success(v) => "update success";
      case Failure(e) => e.printStackTrace();
    }
  }

}
