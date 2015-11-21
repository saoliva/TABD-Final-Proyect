import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.io.fs.FileUtils;

public class Neo4jController 
{
	MongoDBController mc;
	GraphDatabaseService graphDB;
	Node firstNode;
	Node secondNode;
	Relationship relationship;
	
	private static enum RelTypes implements RelationshipType
	{
	    KNOWS
	
	}
	public Neo4jController()
	{
		mc = new MongoDBController();
	}
	
	public void loadDB(String DB_path)
	{
		graphDB = new GraphDatabaseFactory().newEmbeddedDatabase( DB_path );
		registerShutdownHook( graphDB );
	}
	
	public void doQuery(String query)
	{
		try ( Transaction tx = graphDB.beginTx() )
		{
			firstNode = graphDB.createNode();
            firstNode.setProperty( "message", "Hello, " );
            secondNode = graphDB.createNode();
            secondNode.setProperty( "message", "World!" );

            relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
            relationship.setProperty( "message", "brave Neo4j " );
            // END SNIPPET: addData

            // START SNIPPET: readData
            System.out.print( firstNode.getProperty( "message" ) );
            System.out.print( relationship.getProperty( "message" ) );
            System.out.print( secondNode.getProperty( "message" ) );
		    tx.success();
		}
	}
	
	void shutDown()
    {
        System.out.println();
        System.out.println( "Shutting down database ..." );
        // START SNIPPET: shutdownServer
        graphDB.shutdown();
        // END SNIPPET: shutdownServer
    }
	
	void removeData()
    {
        try ( Transaction tx = graphDB.beginTx() )
        {
            // START SNIPPET: removingData
            // let's remove the data
            firstNode.getSingleRelationship( RelTypes.KNOWS, Direction.OUTGOING ).delete();
            firstNode.delete();
            secondNode.delete();
            // END SNIPPET: removingData

            tx.success();
        }
    }
	
	
	
	 // START SNIPPET: shutdownHook
    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
	
	

}


