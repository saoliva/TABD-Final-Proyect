import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.io.fs.FileUtils;
import org.neo4j.kernel.configuration.Config;

import javafx.util.Pair;

public class Neo4jController 
{
	MongoDBController mc;
	GraphDatabaseService graphDB;
	
	public Neo4jController()
	{
		mc = new MongoDBController();
	}
	
	public void loadDB(String DB_path)
	{

		graphDB = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder( DB_path ).setConfig( GraphDatabaseSettings.allow_store_upgrade, "true").newGraphDatabase();;
		registerShutdownHook( graphDB );
	}
	
	public List<Pair<String, List<Integer>>> doQuery(String query)
	{
		String rows = "";
		List<Pair<String, List<Integer>>> atts = new ArrayList<Pair<String, List<Integer>>>();
		try ( Transaction tx = graphDB.beginTx() )
		{
			Result result = graphDB.execute( query );
			List<String> columns = result.columns();
			//System.out.println(columns.size());			
			for(int i = 0; i<columns.size(); i++)
			{
				if(i!=0)
					result = graphDB.execute( query );
				List<Integer> values = new ArrayList<Integer>();
				Iterator<Node> n_column = result.columnAs( columns.get(i));
				for ( Node node : IteratorUtil.asIterable( n_column ) )
				{
				   String nodeResult = node + ": " + node.getProperty( "id" );
				   //System.out.println(nodeResult);					
				   if(!values.contains(node.getProperty( "id" )))
				   {
					   String[] aux = nodeResult.split(":");
					   values.add(Integer.parseInt(aux[1].replace(" ", "")));
				   }
					
				}
				atts.add(new Pair<String,List<Integer>>(columns.get(i), values));
		    }
			
			
			tx.success();
			
		}
		return atts;
	}
	
	void shutDown()
    {
        System.out.println();
        System.out.println( "Shutting down database ..." );
        // START SNIPPET: shutdownServer
        graphDB.shutdown();
        // END SNIPPET: shutdownServer
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


