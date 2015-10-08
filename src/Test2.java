import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;


public class Test2 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		List<String> querylist = Files.readAllLines(Paths.get("C:\\Users\\AKI\\workspace\\Elasticsearch\\length.txt"));
		double sum = 0 ;
		double avg;
		Iterator<String> queritr = querylist.iterator();
		System.out.println(querylist.size());
		while(queritr.hasNext())
		{
			String[] strarray = queritr.next().split("    ");
			
			sum = sum + Double.valueOf(strarray[1]); 
		}
		avg = (int)sum/84678;
		
		System.out.println(avg);
		
		
		
	}

}
