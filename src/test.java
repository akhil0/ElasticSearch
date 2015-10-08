import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import java.util.HashMap;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.node.Node;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
class test {

	public static void main(String[] args) throws JSONException {
		Node node = nodeBuilder().client(true).clusterName("elasticsearch").node();
		Client client = node.client();
		HashMap<String, Double> tfmap = new HashMap<String, Double>();
		HashMap parammap = new HashMap();
		parammap.put("field" , "text");
		parammap.put("term" , "alleg");
		SearchResponse responsesearch = client.prepareSearch("ap_dataset").setTypes("document")
				.setQuery(new FunctionScoreQueryBuilder(QueryBuilders.matchQuery("text", "alleg")).boostMode("replace")
						.add(ScoreFunctionBuilders.scriptFunction("getTF").params(parammap))).setSize(100000).setNoFields().execute().actionGet();
		JSONObject obj = new JSONObject(responsesearch);
		JSONObject obj2 = obj.getJSONObject("hits");
		JSONArray hits = obj2.getJSONArray("hits");

		for(int i=0 ; i < hits.length() ; i ++)
		{
			JSONObject newobj = hits.getJSONObject(i);	
			String id = newobj.getString("id");
			Double tf = newobj.getDouble("score");
			tfmap.put(id,tf);
		}
		
		System.out.println(tfmap.size());		
		client.close();
	}
}