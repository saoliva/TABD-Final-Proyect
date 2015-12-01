
public class main {

	public static void main(String[] args) {
		
		String neo4j_query = ""; // aqui ira una query neo4j para probar
		String neo4j_query2 = "MATCH (p { nombre: \"Seba\"} )-[:VECINO]->(o )-[:MASCOTA]->(s) RETURN o.nombre, s.nombre"; // aqui ira una query neo4j para probar
		String neo4j_query3 = "";
		String neo4j_query4 = "MATCH n RETURN n";
		QueryParser qp = new QueryParser();
		qp.recieveQuery(neo4j_query2);
		
		

	}

}
