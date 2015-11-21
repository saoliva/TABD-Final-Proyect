
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
		nc.loadDB("./Escritorio");
		nc.doQuery(query);
		nc.removeData();
		nc.shutDown();
	}
	
	
}
