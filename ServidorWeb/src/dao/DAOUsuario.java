package dao;

import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonString;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import dominio.Usuario;

public class DAOUsuario {

		public static void registrarse(Usuario usuario) throws Exception{
			BsonDocument bso = new BsonDocument();
			
			bso.append("_id", new BsonString(usuario.getEmail()));
			bso.append("password", new BsonString(usuario.getPassword()));
			
			MongoBroker broker = MongoBroker.get();
			MongoCollection<BsonDocument> usuarios = broker.getCollection("Usuarios");
			usuarios.insertOne(bso);
		}

		public static void login(Usuario usuario) throws Exception {
			BsonDocument criterio= new BsonDocument();
			criterio.append("_id", new BsonString(usuario.getEmail()));
			criterio.append("password", new BsonString(usuario.getPassword()));
			
			MongoBroker broker=MongoBroker.get();
			MongoCollection<BsonDocument> usuarios = broker.getCollection("Usuarios");
			FindIterable<BsonDocument> resultado = usuarios.find(criterio);
			if (resultado.first() == null) throw new Exception("El usuario no existe");
		}
		
		public static void cambiarPassword(Usuario usuario){
			BsonDocument criterio = new BsonDocument();
			criterio.append("_id", new BsonString(usuario.getEmail()));
			
			MongoBroker broker=MongoBroker.get();
			MongoCollection<BsonDocument> usuarios = broker.getCollection("Usuarios");
			FindIterable<BsonDocument> resultado = usuarios.find(criterio);
			
			BsonDocument actualizacion= new BsonDocument("$set", new BsonDocument("password", new BsonString(usuario.getPassword())));
			usuarios.findOneAndUpdate(resultado.first(), actualizacion);
		}

		public static void existe(Usuario usuario) throws Exception {
			BsonDocument criterio= new BsonDocument();
			criterio.append("_id", new BsonString(usuario.getEmail()));
			
			MongoBroker broker=MongoBroker.get();
			MongoCollection<BsonDocument> usuarios = broker.getCollection("Usuarios");
			FindIterable<BsonDocument> resultado = usuarios.find(criterio);
			if (resultado.first() == null) throw new Exception("El usuario no existe");
		}

}
