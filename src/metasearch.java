import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;


public class metasearch {
 
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		List<String> okapitfs = Files.readAllLines(Paths.get("C:\\Users\\AKI\\workspace\\Elasticsearch\\okapitf.txt"));
		String[] okapitf = new String[okapitfs.size()];
		okapitfs.toArray(okapitf);
		List<String> tfidfs = Files.readAllLines(Paths.get("C:\\Users\\AKI\\workspace\\Elasticsearch\\tfidf.txt"));
		String[] tfidf = new String[tfidfs.size()];
		tfidfs.toArray(tfidf);
		List<String> bm25s = Files.readAllLines(Paths.get("C:\\Users\\AKI\\workspace\\Elasticsearch\\bm25.txt"));
		String[] bm25 = new String[bm25s.size()];
		bm25s.toArray(bm25);
		List<String> laplaces = Files.readAllLines(Paths.get("C:\\Users\\AKI\\workspace\\Elasticsearch\\laplace.txt"));
		String[] laplace = new String[laplaces.size()];
		laplaces.toArray(laplace);
		List<String> jmsmoothings = Files.readAllLines(Paths.get("C:\\Users\\AKI\\workspace\\Elasticsearch\\jmsmoothing.txt"));
		String[] jmsmoothing = new String[jmsmoothings.size()];
		jmsmoothings.toArray(jmsmoothing);
		
		System.out.println(okapitf.length); 
		System.out.println(tfidf.length);
		System.out.println(bm25.length);
		System.out.println(laplace.length);
		System.out.println(jmsmoothing.length);
		
		int no = 1;
		int terms = 1000;
		HashMap<String, Integer> okmap = new HashMap<String, Integer>();
		HashMap<String, Integer> tfmap = new HashMap<String, Integer>();
		HashMap<String, Integer> bmmap = new HashMap<String, Integer>();
		HashMap<String, Integer> lpmap = new HashMap<String, Integer>();
		HashMap<String, Integer> jmmap = new HashMap<String, Integer>();
		
		for(int i= 0 ; i <= 25 ; i++)
		{
			okmap = createhashmap(okapitf, i);
			tfmap = createhashmap(tfidf, i);
			bmmap = createhashmap(bm25, i);
			lpmap = createhashmap(laplace, i);
			jmmap = createhashmap(jmsmoothing, i);
		}

	}

	private static HashMap<String, Integer> createhashmap(String[] okapitf, int no) {
		// TODO Auto-generated method stub
		no = no + 1;
		for (int j = no ; j <= no+1000; j++)
		{
			String[] s = okapitf[j].split(" ");
			String queryno = s[0];
			String id = s[2];
			Integer rank = Integer.parseInt(s[3]);
		}
	}

}
