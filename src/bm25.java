import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.node.Node;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;


public class bm25 {

	public static void main(String[] args) throws IOException, JSONException {

		Node node = nodeBuilder().client(true).clusterName("elasticsearch").node();
		Client client = node.client();

		//Reading Doc Lengths to Hash Map
		List<String> a = Files.readAllLines(Paths.get("C:\\Users\\AKI\\workspace\\Elasticsearch\\length.txt"));
		Iterator<String> stritr = a.iterator();
		HashMap<String, Double> lengthmap = new HashMap<String, Double>();
		while(stritr.hasNext())
		{
			String line = stritr.next().toString();
			String[] quearra = line.split("    ");
			lengthmap.put(quearra[0], Double.valueOf(quearra[1]));
		}

		// Reading List of Queries into List
		List<String> querylist = Files.readAllLines(Paths.get("C:\\Users\\AKI\\workspace\\Elasticsearch\\setquery.txt"));
		//List<String> querylist = Files.readAllLines(Paths.get("C:\\Users\\AKI\\workspace\\Elasticsearch\\new.txt"));
		Iterator<String> queritr = querylist.iterator();
		while(queritr.hasNext())
		{
			String[] strarray = queritr.next().split("     ");
			System.out.println(strarray.length);
			String queryno = strarray[0].toString();
			String mainstr = strarray[1].toString();
			String[] words = mainstr.split(" ");
			System.out.println(queryno + " - " + words.length);
			HashMap<String, Double> resultmap = new HashMap<String, Double>();
			for(int i = 0; i < words.length ; i++)
			{
				String qb = words[i];
				resultmap =  queryTF(client, qb, "ap_dataset" , "document", resultmap, lengthmap);
			}
			System.out.println("done with" + queryno);
			printmap(resultmap,queryno);
		}
		client.close();
	}

	//Write HashMap to a File
	private static void printmap(HashMap<String, Double> resultmap, String string) {

		HashMap<String, Double> newmap = new HashMap<String, Double>();
		newmap = (HashMap<String, Double>) sortByValues(resultmap);
		Iterator newmapitr = newmap.keySet().iterator();
		int id0 = 0;
		File log = new File("C:\\Users\\AKI\\workspace\\Elasticsearch\\bm25.txt");
		//File log = new File("C:\\Users\\AKI\\workspace\\Elasticsearch\\okapitf.txt");

		try{
			FileWriter fileWriter = new FileWriter(log, true);

			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			while(newmapitr.hasNext())
			{
				id0++;
				String nextnew = newmapitr.next().toString();
				bufferedWriter.write(string + " Q0 " + nextnew + " " + id0 + " " + newmap.get(nextnew) + " Exp" + "\n");
			}
			bufferedWriter.close();

			System.out.println("Done");
			System.out.println(newmap.size());

		} catch(IOException e) {
			System.out.println("COULD NOT LOG!!");
		}


	}

	/*Reference : StackOverflow*/
	//Sort HashMap by value and return top 1000
	private static HashMap sortByValues(HashMap map) { 
		List list = new LinkedList(map.entrySet());
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue())
						.compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		// Here I am copying the sorted list in HashMap
		// using LinkedHashMap to preserve the insertion order
		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		} 

		HashMap newresult = new LinkedHashMap<>();
		Iterator atr = sortedHashMap.keySet().iterator();
		int id =0 ;

		if(sortedHashMap.size() < 1000)
			newresult.putAll(sortedHashMap);
		else
		{
			while(atr.hasNext() && id < 1000)
			{
				id++;
				String next = atr.next().toString();
				newresult.put( next, sortedHashMap.get(next));
			}
		}

		return newresult;
	}


	//Query based on term
	public static HashMap<String, Double> queryTF(Client client, String qb, String index, String type, HashMap<String, Double> result,HashMap<String, Double> lengthmap) throws IOException, JSONException {
		HashMap parammap = new HashMap();
		parammap.put("field" , "text");
		parammap.put("term" , qb);
		SearchResponse responsesearch = client.prepareSearch(index)
				.setQuery(new FunctionScoreQueryBuilder(QueryBuilders.matchQuery("text", qb)).boostMode("replace")
						.add(ScoreFunctionBuilders.scriptFunction("getTF").params(parammap))).setSize(100000).setNoFields().execute().actionGet();
		JSONObject obj = new JSONObject(responsesearch);
		JSONObject obj2 = obj.getJSONObject("hits");
		JSONArray hits = obj2.getJSONArray("hits");
		int df = 0;
		for (int j=0; j < hits.length(); j++)
		{
			JSONObject hitobj = hits.getJSONObject(j);
			double tf = hitobj.getDouble("score");
			if(tf > 0)
				df++;
		}

		if(df != 0)
		{
			for(int i=0 ; i < hits.length() ; i ++)
			{
				JSONObject newobj = hits.getJSONObject(i);	
				String id = newobj.getString("id");
				Double tf = newobj.getDouble("score");
				double len = lengthmap.get(id);
				//double idf = Math. log ((84678 - df + 0.5) / (df + 0.5));
				//double bm25 = idf * ((tf*2.6)/ (tf+ 1.6*(0.25 + 0.75*(len/247.8))));
				double bm25 = Math.log(84678/df)* ( 3*tf/ (tf+ 2*(0.25 + 0.75*(len/247.8))));
				if(result.containsKey(id))
				{
					double oldtf = result.get(id);
					result.put(id, oldtf + bm25);
				}
				else
					result.put(id, bm25);
			}
		}
		return result;
	}	

	//REFERENCE : http://rest.elkstein.org/
	//GET Request fr hTTP connection
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
