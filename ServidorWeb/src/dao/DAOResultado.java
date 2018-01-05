package dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;
import org.json.JSONException;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

import auxiliar.PairComparator;
import dominio.Partida;
import dominio.Usuario;
import hundirflota.HundirFlota;
import javafx.util.Pair;

public class DAOResultado {
	

	public static  ArrayList<Pair<String, Integer>> obtenerRankingVictorias(String juego){
		ArrayList<Pair<String, Integer>> ranking = new  ArrayList<Pair<String, Integer>>();
		BsonDocument documento;
		BsonDocument criterio= new BsonDocument();
		criterio.append("juego", new BsonString(juego));

		 MongoBroker broker=MongoBroker.get();
		 MongoCollection<BsonDocument> resultados = broker.getCollection("Resultados");
				
		 MongoCursor<BsonDocument> cursor = resultados.find(criterio).iterator();
		
		 while(cursor.hasNext()){
			 documento = cursor.next();
			 ranking.add(new Pair<String, Integer>(documento.getString("usuario").getValue(), (Integer) documento.getInt32("gana").getValue()));
		 }
		 
		 ranking.sort(new PairComparator());
		 
		 return ranking;
	}
	
	
	public static ArrayList<Pair<String, Integer>> obtenerRankingDerrotas(String juego){
		ArrayList<Pair<String, Integer>> ranking = new  ArrayList<Pair<String, Integer>>();
		BsonDocument documento;
		BsonDocument criterio= new BsonDocument();
		criterio.append("juego", new BsonString(juego));

		 MongoBroker broker=MongoBroker.get();
		 MongoCollection<BsonDocument> resultados = broker.getCollection("Resultados");
				
		 MongoCursor<BsonDocument> cursor = resultados.find(criterio).iterator();
		
		 while(cursor.hasNext()){
			 documento = cursor.next();
			 ranking.add(new Pair<String, Integer>(documento.getString("usuario").getValue(), (Integer) documento.getInt32("pierde").getValue()));
		 }
		 
		 ranking.sort(new PairComparator());
		 
		 return ranking;
	}	
	
	public static ArrayList<Pair<String, Integer>> obtenerRankingPartidas(String juego){
		ArrayList<Pair<String, Integer>> ranking = new  ArrayList<Pair<String, Integer>>();
		BsonDocument documento;
		BsonDocument criterio= new BsonDocument();
		criterio.append("juego", new BsonString(juego));

		 MongoBroker broker=MongoBroker.get();
		 MongoCollection<BsonDocument> resultados = broker.getCollection("Resultados");
				
		 MongoCursor<BsonDocument> cursor = resultados.find(criterio).iterator();
		
		 while(cursor.hasNext()){
			 documento = cursor.next();
			 ranking.add(new Pair<String, Integer>(documento.getString("usuario").getValue(), (Integer) documento.getInt32("partidas").getValue()));
		 }
		 
		 ranking.sort(new PairComparator());
		 
		 return ranking;
	}
	
	public static void almacenarResultadosGanador(Partida partida){
		BsonDocument criterio= new BsonDocument();
		BsonDocument actualizacion;
		criterio.append("usuario", new BsonString(partida.getGanador().getEmail()));
		criterio.append("juego", new BsonString(partida.getJuego()));
		
		MongoBroker broker=MongoBroker.get();
		MongoCollection<BsonDocument> resultados = broker.getCollection("Resultados");
		FindIterable<BsonDocument> resultado = resultados.find(criterio);
		
		if (resultado.first() == null) {
			criterio.append("gana", new BsonInt32(1));
			criterio.append("partidas", new BsonInt32(1));
			criterio.append("pierde", new BsonInt32(0));
			resultados.insertOne(criterio);
		} else {
			actualizacion= new BsonDocument("$set", new BsonDocument("gana", new BsonInt32(1 +
					((Integer) resultado.first().getInt32("gana").getValue()))));
			resultados.findOneAndUpdate(resultado.first(), actualizacion);
			actualizacion= new BsonDocument("$set", new BsonDocument("partidas", new BsonInt32(1 +
					((Integer) resultado.first().getInt32("partidas").getValue()))));
			resultados.findOneAndUpdate(resultado.first(), actualizacion);
		}
	}
	
	public static void almacenarResultadosPerdedor(Partida partida){
			BsonDocument criterio= new BsonDocument();
			BsonDocument actualizacion;
			criterio.append("usuario", new BsonString(partida.getPerdedor().getEmail()));
			criterio.append("juego", new BsonString(partida.getJuego()));
			
			MongoBroker broker=MongoBroker.get();
			MongoCollection<BsonDocument> resultados = broker.getCollection("Resultados");
			FindIterable<BsonDocument> resultado = resultados.find(criterio);
			if (resultado.first() == null) {
				criterio.append("pierde", new BsonInt32(1));
				criterio.append("partidas", new BsonInt32(1));
				criterio.append("gana", new BsonInt32(0));
				resultados.insertOne(criterio);
			} else {
				actualizacion= new BsonDocument("$set", new BsonDocument("pierde", new BsonInt32(1 +
						((Integer) resultado.first().getInt32("pierde").getValue()))));
				resultados.findOneAndUpdate(resultado.first(), actualizacion);
				actualizacion= new BsonDocument("$set", new BsonDocument("partidas", new BsonInt32(1 +
						((Integer) resultado.first().getInt32("partidas").getValue()))));
				resultados.findOneAndUpdate(resultado.first(), actualizacion);
			}
	}


	public static void almacenarResultadosEmpate(Partida partida, Usuario jugador) {
		BsonDocument criterio= new BsonDocument();
		BsonDocument actualizacion;
		criterio.append("usuario", new BsonString(jugador.getEmail()));
		criterio.append("juego", new BsonString(partida.getJuego()));
		
		MongoBroker broker=MongoBroker.get();
		MongoCollection<BsonDocument> resultados = broker.getCollection("Resultados");
		FindIterable<BsonDocument> resultado = resultados.find(criterio);
		if (resultado.first() == null) {
			criterio.append("pierde", new BsonInt32(0));
			criterio.append("partidas", new BsonInt32(1));
			criterio.append("gana", new BsonInt32(0));
			resultados.insertOne(criterio);
		} else {
			actualizacion= new BsonDocument("$set", new BsonDocument("partidas", new BsonInt32(1 +
					((Integer) resultado.first().getInt32("partidas").getValue()))));
			resultados.findOneAndUpdate(resultado.first(), actualizacion);
		}
		
	}
	
	
}
