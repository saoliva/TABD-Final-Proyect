
public class main {

	public static void main(String[] args) {
		
		//String neo4j_query = "MATCH (m:Movie) RETURN m"; // aqui ira una query neo4j para probar
		String neo4j_query2 = "MATCH (p { nombre:\"Seba\"})-[:VECINO]-(o { nombre:\"Adrian\"}) RETURN p.edad, o.nombre"; // aqui ira una query neo4j para probar
		//String neo4j_query3 = "MATCH (tobias { name: 'Tobias', hola: 'l' }),(others) WHERE others.name IN ['Andres', 'Peter'] AND (tobias)<--(others) RETURN others";
		String neo4j_query4 = "MATCH n RETURN n";
		QueryParser qp = new QueryParser();
		qp.recieveQuery(neo4j_query2);
		
		

	}

}
