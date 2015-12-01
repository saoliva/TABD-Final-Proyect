import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

public class QueryParser
{

	Neo4jController nc;
	MongoDBController mc;
	
	
	public QueryParser()
	{
		 nc = new Neo4jController();
		 mc = new MongoDBController();
		 mc.LoadDB("DBVecinos");
		 mc.LoadCollection("nombreColeccion");
		 nc.loadDB("grafos");
	}
	
	public void recieveQuery(String query)
	{		
		long start = System.currentTimeMillis( );
		
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
			String mongo_query = "";
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
			mongo_query = query.substring(left+1,right);
			String[] attributes = mongo_query.split(",");
			List<Pair<String, Object>> atts = new ArrayList<Pair<String, Object>>();
			for(int k = 0; k<attributes.length; k++)
			{
				String[] tuple = attributes[k].split(":");
				atts.add(new Pair<String,Object>(tuple[0].replace(" ", ""), tuple[1].replace(" ", "").replace("\"", "")));
				
			}
			
			List<Pair<String, Object>> response = mc.findFieldValue(atts, "id");
			int cant_params = 0;
			if(cant_mongo_queries > 1 || where)
			{
				where_clause += "AND ";
			}
			for(int k = 0; k<response.size(); k++)
			{			
				if(cant_params == 0) // esto es para poner los OR's
				{
					where_clause += "( " + node +".id = " + response.get(k).getValue() + " ";
				}
				else
				{
					where_clause += "OR " + node +".id = " + response.get(k).getValue() + " ";
				}
				if(k == response.size()-1)
					where_clause += ")";
				cant_params++;
			}
			cant_mongo_queries ++;
		}
		List<String> returns_value = new ArrayList<String>();
		List<List<String>> values = new ArrayList<List<String>>();
		if(!exists_mongo)
		{
			new_query += query.substring(0,return_position);
			
			String return_clause = "RETURN ";
			
			String returns = query.substring(return_position).replace("RETURN", "");
			
			String[] each_return = returns.split(",");
			
			
			for(int i = 0; i<each_return.length; i++)
			{
				each_return[i] = each_return[i].replace(" ", "");
				
				if(each_return[i].contains("."))
				{
					String[] names = each_return[i].split("\\.");
					names[0] = names[0].replace(" ", "");
					names[1] = names[1].replace(" ", "");
					if(!returns_value.contains(names[0]))
					{
						returns_value.add(names[0]);
						values.add(new ArrayList<String>());
						int pos= returns_value.indexOf(names[0]);
						values.get(pos).add(names[1]);
					}
					else
					{
						int pos= returns_value.indexOf(names[0]);
						if(!values.get(pos).contains(names[1]))
						{
							values.get(pos).add(names[1]);
						}
						
					}
				}
				else
				{
					if(!returns_value.contains(each_return[i]))
					{
						returns_value.add(each_return[i]);
						int pos= returns_value.indexOf(each_return[i]);
						values.add(new ArrayList<String>());
						
					}
				}
			}
			
			for(int i = 0; i<returns_value.size(); i++)
			{
				return_clause += returns_value.get(i);
				if(returns_value.size()>1 && i!=returns_value.size()-1)
					return_clause += ", ";
			}
			
			new_query += return_clause;
		}
		else
		{
			new_query += (query.substring(right+1, return_position));
			
			new_query += where_clause;
			
			String return_clause = "RETURN ";
			
			String returns = query.substring(return_position).replace("RETURN", "");
			
			String[] each_return = returns.split(",");
			
			
			for(int i = 0; i<each_return.length; i++)
			{
				each_return[i] = each_return[i].replace(" ", "");
				
				if(each_return[i].contains("."))
				{
					String[] names = each_return[i].split("\\.");
					names[0] = names[0].replace(" ", "");
					names[1] = names[1].replace(" ", "");
					if(!returns_value.contains(names[0]))
					{
						returns_value.add(names[0]);
						values.add(new ArrayList<String>());
						int pos= returns_value.indexOf(names[0]);
						values.get(pos).add(names[1]);
					}
					else
					{
						int pos= returns_value.indexOf(names[0]);
						if(!values.get(pos).contains(names[1]))
						{
							values.get(pos).add(names[1]);
						}
						
					}
				}
				else
				{
					if(!returns_value.contains(each_return[i]))
					{
						returns_value.add(each_return[i]);
						int pos= returns_value.indexOf(each_return[i]);
						values.add(new ArrayList<String>());
						
					}
				}
			}
			
			for(int i = 0; i<returns_value.size(); i++)
			{
				return_clause += returns_value.get(i);
				if(returns_value.size()>1 && i!=returns_value.size()-1)
					return_clause += ", ";
			}
			
			new_query += return_clause;
		}

		System.out.println(new_query);
		
		List<Pair<String, List<Integer>>> neo_response =  nc.doQuery(new_query);
		
		for(int i = 0; i<neo_response.size(); i++) // itero entre la cantidad de returns de la consulta
		{
			String r = neo_response.get(i).getKey();
			List<Integer> l = neo_response.get(i).getValue();
			int pos = returns_value.indexOf(r);
			for(int j = 0; j<values.get(i).size(); j++)// itero en los atributos que se piden de los nodos
			{
				for(int k = 0; k<l.size() ; k++) // itero entre los nodos que me dio neo
				{
					List<Pair<String, Object>> atts = new ArrayList<Pair<String, Object>>();
					atts.add(new Pair<String,Object>("id", l.get(k)));										
					List<Pair<String, Object>> response2 = mc.findFieldValue(atts, values.get(i).get(j));
					
					System.out.println(r + "." + values.get(i).get(j) + ":" + response2.get(0).getValue());
			
				}
			}
		}
		long end = System.currentTimeMillis( );
        long diff = end - start;
		
        System.out.println("Time Elapsed: " + diff + "ms");
        
		nc.shutDown();
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
			type_position = query.substring(left).indexOf(":", type_position+1);
			
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
