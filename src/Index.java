import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.elasticsearch.common.xcontent.XContentFactory.*;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import static org.elasticsearch.node.NodeBuilder.*;


public class Index {
	public static void main(String[] args) throws IOException {

		Node node = nodeBuilder().client(true).clusterName("elasticsearch").node();
		Client client = node.client();
		// Walking thru Files in Folder
		Files.walk(Paths.get("ap89_collection")).forEach(filePath -> {
			if (Files.isRegularFile(filePath)) {
				String filePath1 = filePath.toString();


				String testHtml=null;
				// Writing All Text to a String.
				try {
					testHtml = String.join("\n", Files.readAllLines(Paths.get(filePath1) ,Charset.forName("ISO-8859-1")));

				} catch (Exception e) {
					e.printStackTrace();
				}


				// Breaking all DOCS into String Arrays
				String[] tds = StringUtils.substringsBetween(testHtml, "<DOC>", "</DOC>");
				for (String td : tds) {
					String title = StringUtils.substringBetween(td, "<DOCNO>", "</DOCNO>");
					title = title.trim();
					String[] texts = StringUtils.substringsBetween(td, "<TEXT>", "</TEXT>");
					String combinedtext = StringUtils.join(texts);
					combinedtext = combinedtext.trim();
					combinedtext = combinedtext.trim().replaceAll("\n", " ");
					combinedtext = combinedtext.trim().replaceAll(" +", " ");
					System.out.println("ID: " + title);
					try {
						IndexResponse response = client.prepareIndex("ap_dataset", "document", title)
								.setSource(jsonBuilder()
										.startObject()
										.field("text", combinedtext)
										.endObject()
										)
										.execute()
										.actionGet();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}



			}
		});
	}
}

