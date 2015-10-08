import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;


public class hashmaptest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		HashMap<String,Integer> mapo = new HashMap<String, Integer>();
		mapo.put("aki", 100);
		mapo.put("abhi", 10);
		mapo.put("vig", 90);
		mapo.put("maha", 110);
		mapo.put("rev", 120);
		System.out.println(mapo.size());
		Iterator<String> a = mapo.keySet().iterator();
		int id =0 ;
		HashMap<String,Integer> newmapo = new HashMap<String, Integer>();
		
		if(mapo.size() <= 2)
			
		while(a.hasNext() && id <2)
		{
			id++;
			String next = a.next().toString();
			newmapo.put(next, mapo.get(next));
		}
		System.out.println(newmapo.size());
		
		mapo = (HashMap<String, Integer>) sortByValues(mapo);
		Iterator<String> a1 = mapo.keySet().iterator();
		System.out.println(mapo.size());
		while(a1.hasNext())
		{
			String next = a1.next().toString();
			System.out.println(next + " " + mapo.get(next));
		}
		
		String str = "10. 10this is wat i want";
		System.out.println(str.replaceFirst("10" , ""));
		

	}
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
	       return sortedHashMap;
	  }
	
	/*public static <K, V extends Comparable<? super V>> Map<K, V> 
	sortByValue( Map<K, V> map )
	{
		Map<K,V> result = new LinkedHashMap<>();
		Stream<Entry<K, V>> st = map.entrySet().stream();
		 Comparator<Map.Entry<Integer,Integer>> descendingComparator = (x,y)->y.getKey().compareTo(x.getKey());
		st.sorted(Comparator.comparing(e -> e.getValue()))
		.forEach(e ->result.put(e.getKey(),e.getValue()));
		
		
 
		return result;
	}
*/

}
