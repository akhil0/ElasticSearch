import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.index.Terms;
import org.elasticsearch.common.collect.Iterators;
import org.elasticsearch.common.xcontent.XContentFactory.*;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.termvector.TermVectorResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.FilterBuilders.*;
import org.elasticsearch.index.query.QueryBuilders.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static org.elasticsearch.node.NodeBuilder.*;


public class TF {
	public static void main(String[] args) throws IOException, JSONException {

		Node node = nodeBuilder().client(true).clusterName("elasticsearch").node();
		Client client = node.client();
		//String url = "http://localhost:9200/ap_dataset/document/AP891231-0003/_termvector";
		HashMap<String, Integer> countmap = new HashMap<String,Integer>();
		// Reading List of Queries into List
		List<String> querylist = Files.readAllLines(Paths.get("C:\\Users\\AKI\\workspace\\Elasticsearch\\setquery.txt"));
		//List<String> querylist = Files.readAllLines(Paths.get("C:\\Users\\AKI\\workspace\\Elasticsearch\\new.txt"));
		Iterator<String> queritr = querylist.iterator();
		while(queritr.hasNext())
		{
			//Break the String into QueryNo and Query String with Five Spaces
			String[] strarray = queritr.next().split("     ");
			System.out.println(strarray.length);
			String queryno = strarray[0].toString();
			String mainstr = strarray[1].toString();
			String[] s = {};
			String url = "http://localhost:9200/ap_dataset/document/"+ d[i] +"/_termvector?fields=text,term_statistics=true"; 
			String response = httpGet(url);
			JSONObject repo = new JSONObject(response);
			JSONObject vector1 = repo.getJSONObject("term_vectors");

			if(vector1.isNull("text"))
				System.out.println("null obj");
			else

			{
				JSONObject vector = vector1.getJSONObject("text").getJSONObject("terms");
				int tf = 0;
				int sum = 0;
				System.out.println(vector.toString());

				Iterator itr = vector.keys();

				while(itr.hasNext())
				{
					String word = itr.next().toString();
					System.out.println(word);

					if(countmap.containsKey(word))
						countmap.put(word, countmap.get(word) + 1);
					else
						countmap.put(word, 1);
				}
			}
			System.out.println(countmap.size());
		}

			client.close(); 
		} 

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




