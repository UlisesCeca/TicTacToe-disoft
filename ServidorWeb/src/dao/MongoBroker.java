package dao;

import org.bson.BsonDocument;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import dominio.Manager;
import dominio.Manager;

public class MongoBroker {

	private MongoClient mongoClient;
	private MongoDatabase db;
	
	private MongoBroker(){
		this.mongoClient= new MongoClient("localhost", 27017);
		this.db=mongoClient.getDatabase("Juegos");
	}
	/*
	 * 
	 * La clase interna SingletonHolder me permite implementar un singleton que no falle con concurrencia.
	 * 
	 */
	public static class SingletonHolder{
		static MongoBroker singleton=new MongoBroker();
	}
	
	public static MongoBroker get(){
		return SingletonHolder.singleton;
	}
	public MongoCollection<BsonDocument> getCollection (String collection){
		MongoCollection <BsonDocument> result=db.getCollection(collection, BsonDocument.class);
		if(result==null){
			db.createCollection(collection);
			result=db.getCollection(collection, BsonDocument.class);
		}
		return result;
	}
}
