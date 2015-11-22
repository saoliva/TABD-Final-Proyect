
public class main {

	public static void main(String[] args) {
		
		String neo4j_query = "MATCH (m:Movie) RETURN m"; // aqui ira una query neo4j para probar
		String neo4j_query2 = "MATCH (p:Person { name:\"Keanu Reeves\"}) RETURN p"; // aqui ira una query neo4j para probar
				
		
		QueryParser qp = new QueryParser();
		qp.recieveQuery(neo4j_query2);
		
		

	}

}
