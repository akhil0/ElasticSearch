import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.xcontent.XContentFactory.*;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.termvector.TermVectorRequest;
import org.elasticsearch.action.termvector.TermVectorResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.FilterBuilders.*;
import org.elasticsearch.index.query.QueryBuilders.*;
public class Response {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		Node node = nodeBuilder().client(true).clusterName("elasticsearch").node();
		Client client = node.client();

		//QueryBuilder qb = idsQuery().ids("AP891231-0003");
		SearchResponse response = client.prepareSearch("ap_dataset") 
				        .setSearchType(SearchType.DFS_QUERY_THEN_FETCH) 
				        .setQuery(QueryBuilders.termQuery("text", "1980")) 
				        .setExplain(false) 
				        .execute() 
				        .actionGet(); 

		/*TermVectorResponse resp = client.prepareTermVector()
						.setIndex("ap_dataset").setType("document")
						.setId("AP891231-0003")
						.setFieldStatistics(true)
						.execute()
						.actionGet();*/



		// SearchHit[] docs = response.getHits().getHits(); 
		// System.out.println("size is " +  docs.length); 
		//String response1 = response.toString();
		//System.out.println(response1);
		//Float test = response.getHits().getMaxScore();
		//System.out.println(test);

		/*TermVectorRequest req = new TermVectorRequest("ap_dataset", "document","AP891231-0003");
		req = req.termStatistics(true)
				.offsets(true)
				.payloads(true)
				.fieldStatistics(true).selectedFields("text");
		TermVectorResponse response1 = client.termVector(req).get();
		String axe = response1.getFromContext("term_vectors");
		System.out.println(axe);
		client.close();*/

	}

}
