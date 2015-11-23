
public class QueryParser
{

	Neo4jController nc;
	MongoDBController mc;
	
	
	public QueryParser()
	{
		 nc = new Neo4jController();
		 mc = new MongoDBController();
	}
	
	public void recieveQuery(String query)
	{
		
		int left = 0;
		int right = 0;
		String new_query = "";
		boolean exists_mongo = false; // verificar si existe consultas mongo dentro de llaves
		String where_clause = "";
		String node = "";
		int return_position = query.indexOf("RETURN");
		int cant_mongo_queries = 1; // esto es para los and's entre consultas mongo
		boolean where = false;
		
		if(!query.contains("WHERE"))
		{
			where_clause += "WHERE ";
		}		
		else
		{
			where = true;
		}
		
		while((left = query.indexOf("{", left+1)) != -1) // verificar cuandos { tiene
		{
			if(right!=0)
			{
				new_query += (query.substring(right+1, left));
				
			}
			else
			{
				new_query += (query.substring(0, left));
			}
			
			exists_mongo = true;
			right = query.indexOf("}", right+1);
			node = getNodeName(query,left, cant_mongo_queries);	
			
			int cant_params = 0;
			if(cant_mongo_queries > 1 || where)
			{
				where_clause += "AND ";
			}
			if(cant_params == 0) // esto es para poner los OR's
			{
				where_clause += node +".id = 1 ";
			}
			else
			{
				where_clause += "OR " + node +".id = 1 ";
			}
			
			cant_mongo_queries ++;
		}
		
		if(!exists_mongo)
		{
			new_query += query;
		}
		else
		{
			new_query += (query.substring(right+1, return_position));
			
			new_query += where_clause;
			
			new_query += query.substring(return_position);
		}
		
		System.out.println(new_query);
		
		//nc.loadDB("./Escritorio");
		//nc.doQuery(query);
		//nc.removeData();
		//nc.shutDown();
	}
	
	String getNodeName(String query, int left, int cant_att) // obtiene el nombre del nodo a que se le asignara la respuesta de mongo
	{
		String node;
		int bracket_position = 0; // posicion del inicio de parentesis
		int type_position = 0;  // posicion de los 2 puntos
		int aux = -1;
		int counter = 0;
		
		while(true)
		{
			type_position = query.indexOf(":", type_position+1);
			
			if(type_position > left || type_position == -1)
			{
				if(counter < cant_att)
				{
					type_position = left;
				}
				else
				{
					type_position = aux;
				}				
				break;
			}
			else
			{
				aux = type_position;
			}			
			counter++;
		}
		aux = -1;
		while(true)
		{
			 bracket_position = query.indexOf("(", bracket_position+1);
			
			if(bracket_position > type_position || bracket_position == -1)
			{
				bracket_position = aux;
				break;
			}
			else
			{
				aux = bracket_position;
			}				
		}
		
		node = query.substring(bracket_position+1, type_position).split(" ")[0];
		
		return node;
	}
	
	
}
