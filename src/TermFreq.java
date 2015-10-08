import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.Query;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.index.search.MatchQuery;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.*;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilder.*;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.MetricsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.Cardinality;
import org.elasticsearch.search.facet.statistical.StatisticalFacet;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import static org.elasticsearch.index.query.FilterBuilders.*;
import static org.elasticsearch.index.query.QueryBuilders.*;

public class TermFreq {

	public static void main(String[] args) throws IOException, JSONException {
		// TODO Auto-generated method stub

		Node node = nodeBuilder().client(true).clusterName("elasticsearch").node();
		Client client = node.client();
		int id =0 ;
		String str = "85.   alleg measur corrupt public offici govern jurisdict worldwid";
		String[] strarray = str.split("   ");
		/*for(int i = 0; i < strarray.length ; i++)
			System.out.println(strarray[i]);*/

		String mainstr = strarray[1];
		String[] words = mainstr.split(" ");
		HashMap<String, Double> resultmap = new HashMap<String, Double>();
		for(int i = 0; i < words.length ; i++)

		{
			QueryBuilder qb = matchQuery( "text", words[i]);
			resultmap =  queryTF(client, qb, "ap_dataset" , "document", resultmap);
			Iterator resultitr = resultmap.keySet().iterator();
			while(resultitr.hasNext())
			{
				String next = resultitr.next().toString();
				System.out.println(next + " - " + resultmap.get(next));
			}
		}
		System.out.println("done");
	}

	public static HashMap<String, Double> queryTF(Client client, QueryBuilder qb, String index, String type, HashMap<String, Double> result) throws IOException, JSONException {
		SearchResponse scrollResp = client.prepareSearch(index).setTypes(type)
				.setScroll(new TimeValue(6000))
				.setQuery(qb)
				.setExplain(true)
				.setSize(100000).execute().actionGet();

		// no query matched
		if (scrollResp.getHits().getTotalHits() == 0) {
			return result;
		}
		//Map<String, Integer> results = new HashMap<>();
		while (true) {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				String docno = hit.getId();
				double tf =   hit.getExplanation().getDetails()[0].getDetails()[0].getDetails()[0].getValue();
				//double len = (double) lengthmap.get(docno);
				double len = doclength(docno);
				double okapitf = tf/(tf + 0.5 + 1.5*(len/247));
				if(result.containsKey(docno))
				{
					double docid = result.get(docno);
					result.put(docno, docid + okapitf);
				}
				else
					result.put(docno, okapitf);
			}
			scrollResp =
					client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(
							new TimeValue(6000)).execute().actionGet();
			if (scrollResp.getHits().getHits().length == 0) {
				break;
			}
		}
		return result;
	}

	private static double doclength(String docno) throws IOException, JSONException {
		String url = "http://localhost:9200/ap_dataset/document/" + docno + "/_termvector?fields=text,term_statistics=true"; 
		String response = httpGet(url);
		JSONObject repo = new JSONObject(response);
		JSONObject vector = repo.getJSONObject("term_vectors");
		double tf = 0;
		double length = 0;
		if(vector.isNull("text"))
			length = 0;
		else
		{
			JSONObject textep = vector.getJSONObject("text").getJSONObject("terms");
			Iterator itr = textep.keys();

			while(itr.hasNext())
			{
				tf = (int) textep.getJSONObject(itr.next().toString()).get("term_freq");
				length = length  + tf;
			}

		}
		
		return length;
	}

	//REFERENCE : http://rest.elkstein.org/
	public static String httpGet(String urlStr) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn =
				(HttpURLConnection) url.openConnection();

		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}

		// Buffer the result into a string
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line);
		}
		rd.close();

		conn.disconnect();
		return sb.toString();
	}

}
