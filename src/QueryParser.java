
public class QueryParser
{

	Neo4jController nc;
	MongoDBController mc;
	
	
	public QueryParser()
	{
		 nc = new Neo4jController();
		 mc = new MongoDBController();
	}
	
	
}
