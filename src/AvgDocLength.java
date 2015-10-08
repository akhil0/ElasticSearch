import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.json.JSONException;
import org.json.JSONObject;

import static org.elasticsearch.node.NodeBuilder.*;


public class AvgDocLength {
	public static void main(String[] args) throws IOException, JSONException {
		Node node = nodeBuilder().client(true).clusterName("elasticsearch").node();
		Client client = node.client();
		List<String> a = Files.readAllLines(Paths.get("C:\\Users\\AKI\\Downloads\\AP89_DATA\\AP_DATA\\doclist_new_0609.txt"));

		Iterator stritr = a.iterator();
		HashMap<String, Integer> lengthmap = new HashMap<String, Integer>();
		
		File log = new File("C:\\Users\\AKI\\workspace\\Elasticsearch\\length.txt");
		FileWriter fileWriter = new FileWriter(log, true);

		BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
		
		while(stritr.hasNext())
		{
			String[] a1 = ( (String) stritr.next()).split("  ");
			 String id1 = a1[1];

			String url = "http://localhost:9200/ap_dataset/document/" + id1 + "/_termvector?fields=text,term_statistics=true"; 
			String response = httpGet(url);
			JSONObject repo = new JSONObject(response);
			JSONObject vector = repo.getJSONObject("term_vectors");
			int tf = 0;
			int sum = 0;

			if(vector.isNull("text"))
				bufferedWriter.write(id1+ "    " + 0 + "\n");
			else
			{
				JSONObject textep = vector.getJSONObject("text").getJSONObject("terms");
				Iterator itr = textep.keys();

				while(itr.hasNext())
				{
					tf = (int) textep.getJSONObject((String) itr.next()).get("term_freq");
					sum = sum  + tf;
				}
				System.out.println(id1 + " - " + sum);
				bufferedWriter.write(id1+ "    " + sum + "\n");

			}
			

		}
		bufferedWriter.close();
		System.out.println("Done");

		client.close(); 
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




