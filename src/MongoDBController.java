
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Projections.*;

import javafx.util.Pair;

public class MongoDBController {

	MongoClient mongo ;
	DB db;
	DBCollection collection;
	
	public MongoDBController(){
		//conexion con mongo
		mongo = new MongoClient("localhost", 27017);
		
	}
	
	public void LoadDB(String dbname){
		db = mongo.getDB(dbname);
	}
	
	public void LoadCollection(String colname){
		collection = db.getCollection("person"); 
	}
	
	public void getAllDocument(){
		DBCursor cursor = collection.find();
		    try {
		       while(cursor.hasNext()) {
		           System.out.println(cursor.next());
		       }
		    } finally {
		       cursor.close();
		    }
	}
	
	
	public  List<Pair<String, Object>> findFieldValue(List<Pair<String, Object>> tuplas, String atributo){
		//Recibe pares de restricciones y el atributo del que se quiere encontrar la informaci�n
		//getAllDocument();
		BasicDBObject query = new BasicDBObject();
		
		for(int i =0; i<tuplas.size(); i++){
			query.append(tuplas.get(i).getKey(), tuplas.get(i).getValue());
			//query.append("{}", "{}");
		}
		
		BasicDBObject exclude_include = new BasicDBObject();
        exclude_include.append(atributo, 1);
        exclude_include.append("_id", 0);
        /* aqui se deberia hacer un for para que no se 
         * muestre ningun atributo exepto el que se quiera mostrar */
       // System.out.println(query);
        DBCursor cursor = collection.find(query, exclude_include);
        //DBCursor cursor = collection.find();
        List<Pair<String, Object>> resultado = new ArrayList<Pair<String, Object>>();
        
		try {
		       while(cursor.hasNext()) {
		        String[] rs = cursor.next().toString().split(":");
		        //System.out.println(cursor.curr().toString());
		        resultado.add(new Pair<String,Object>(rs[0], rs[1].replace(" ", "").replace("}","")));
		        
		       }
		    } finally {
		       cursor.close();
		    }
		
		return resultado;
	}
	
}
