
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

		
		if(!query.contains("WHERE") || !query.contains("where"))
		{
			where_clause += "WHERE ";
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
			node = getNodeName(query,left);	
			
			
						
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
	
	String getNodeName(String query, int left) // obtiene el nombre del nodo a que se le asignara la respuesta de mongo
	{
		String node;
		int bracket_position = 0; // posicion del inicio de parentesis
		int type_position = 0;  // posicion de los 2 puntos
		int aux = 0;
		
		while(true)
		{
			type_position = query.indexOf(":", type_position+1);
			
			if(type_position > left || type_position == -1)
			{
				type_position = aux;
				break;
			}
			else
			{
				aux = type_position;
			}				
		}
		aux = 0;
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
		
		node = query.substring(bracket_position+1, type_position);
		
		return node;
	}
	
	
}
