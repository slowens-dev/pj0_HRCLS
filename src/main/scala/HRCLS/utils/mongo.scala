package HRCLS.utils

import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros.createCodecProvider
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import scala.reflect.ClassTag
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.collection.mutable.Document
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase, Observable}

import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}

object mongo {
  private var CLIENT : MongoClient = null;
  java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(java.util.logging.Level.SEVERE)
  def openMongoClient() {
    CLIENT = MongoClient();
  }
  def getMongoDatabase(str: String) : MongoDatabase = {
    CLIENT.getDatabase(str);
  }
  def getMongoDatabaseWithCodec(str: String, codec : CodecRegistry): MongoDatabase = {
    CLIENT.getDatabase(str).withCodecRegistry(codec);
  }
  def getMongoCollection(db:MongoDatabase, collection: String) : MongoCollection[Document] = {
    db.getCollection(collection)
  }
  def getMongoCollectionWithCodec[T:ClassTag](db:MongoDatabase, collection: String, codec : CodecRegistry) : MongoCollection[T] = {
    val res : MongoCollection[T] = db.withCodecRegistry(codec).getCollection(collection);
    res
  }
  def getMongoCollectionWithCodec[T:ClassTag](db: String, collection: String, codec : CodecRegistry) : MongoCollection[T] = {
    CLIENT.getDatabase(db).withCodecRegistry(codec).getCollection(collection);
  }
  def closeMongoClient(): Unit = {
    CLIENT.close();
  }
  def getResults[T](obs: Observable[T]): Seq[T] = { Await.result(obs.toFuture(),Duration(10, SECONDS)) }
  def printResults[T](obs: Observable[T]): Unit = { getResults(obs).foreach(println) }
}
