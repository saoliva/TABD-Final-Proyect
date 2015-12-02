
public class main {

	public static void main(String[] args) {
		
		String neo4j_query = "MATCH (p )-[:VECINO]->(o )<-[:VECINO]-(s) RETURN COUNT(o), o.nombre"; // aqui ira una query neo4j para probar
		String neo4j_query2 = "MATCH (p { nombre: \"Seba\"} )-[:VECINO]->(o )-[:MASCOTA]->(s) RETURN COUNT(o), s.nombre"; // aqui ira una query neo4j para probar
		String neo4j_query3 = "MATCH (p )-[:MASCOTA]->(o )<-[:MASCOTA]-(s) RETURN o.nombre, s.nombre";
		String neo4j_query4 = "MATCH n RETURN n.nombre";
		
		
		QueryParser qp = new QueryParser();
		qp.recieveQuery(neo4j_query2);
		
		

	}

}
