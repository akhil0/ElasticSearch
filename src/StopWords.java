import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;





public class StopWords {
  public static void main(String[] args)throws IOException{
    	String filePath1 = "C:\\Users\\AKI\\workspace\\Elasticsearch\\test.txt";
    	String testHtml = "";
    	try {
			testHtml = String.join("\n", Files.readAllLines(Paths.get(filePath1) ,Charset.forName("ISO-8859-1")));

		} catch (Exception e) {
			e.printStackTrace();
		}
    	System.out.println(testHtml);
    	String[] a = testHtml.replace(". ", " ").toLowerCase().split(" ");
    	List<String> listOfWords = new ArrayList<String>();
    	listOfWords.addAll(Arrays.asList(a));
    	//System.out.println(testHtml);
    	
    	
    	//String[] a = {"a" ,"the","akhil", "loop"};
  
    	 List<String> stoplist = Files.readAllLines(Paths.get("C:\\Users\\AKI\\workspace\\Elasticsearch\\stoplist.txt")); 
    	 listOfWords.removeAll(stoplist);
    	 System.out.println(listOfWords);
    	/*for(String str : stoplist)
    	{
    		System.out.println(str);
    		testHtml=testHtml.replaceAll(" " + str + " ", " ");
    		testHtml=testHtml.replaceAll("." + str + " ", " ");
    		testHtml=testHtml.replaceAll(" +", " ").trim();
    		
    	}*/
    	
    	//System.out.println(testHtml);
    	 /*combinedtext = combinedtext//.replace(".", "")//.replaceAll("[^a-zA-Z0-9 ]", "")//replaceAll("[][(){},.;!?<>%]", "")
			.replaceAll("\n", " ").replaceAll("[/']", " ").replaceAll("[^a-z/A-Z/0-9/./ /-/']", " ").
			replaceAll("\\.+\\s", " ").replaceAll("[,;]$", "").replaceAll("\\.(?!\\w)", "")
			.replaceAll(" +", " ").
			trim().toLowerCase();//;
	combinedtext = combinedtext.replaceAll(". ", " ")//.replaceAll(" (", " ").replaceAll(") ", " ")
			.replaceAll(", ", " ").replaceAll("? ", " ").replaceAll("-", " ").replaceAll(" _", " ")
			.replaceAll("_ ", " ").replaceAll(":"," ").replaceAll("; ", " ").replaceAll(" +", " ").trim().toLowerCase();*/
			/*String textWithoutPunctuations = combinedtext.replaceAll("[^a-z/A-Z/0-9/./ /-/']", " ");
	textWithoutPunctuations = textWithoutPunctuations.replaceAll("[/']", " ");	//remove every '.
	textWithoutPunctuations = textWithoutPunctuations.replaceAll("\\.+\\s", " ");	//remove . at end of each line.
	textWithoutPunctuations = textWithoutPunctuations.replaceAll(" {2,}", " ");	//remove extra white space
	textWithoutPunctuations = textWithoutPunctuations.replaceAll("[,;]$", "").toLowerCase();*/
    	 
    	//combinedtext = StopWordRemoval(combinedtext, stoplist);
			//String[] sarray = combinedtext.split("[^\\w+(\\.?\\w+)*]");
			//String[] sarray = combinedtext.split(" ");
			//String[] sarray = textWithoutPunctuations.split(" ");
    	 
    	//listOfWord.addAll(Arrays.asList(sarray));
			//listOfWords.addAll(Arrays.asList(sarray));
    	 

			//listOfWords.removeAll(stoplist);
			/*Iterator<String> itr1 = listOfWord.iterator();
			while (itr1.hasNext())
			{
				String s = itr1.next();						
				s = s.replace("[", "").replace("]", "").replace("(", "")
						.replace(")", "").replace("{", "").replace("}", "")
						.replace(",", "").replace(".", "").replace(":", "")
						.replace(";", "").replace(":", "").replace("?", "")
						.replace("!", "").replace("<", "").replace(">", "")
						.replace("%", "").replace("+", "");
				listOfWords.add(s);

			}*/
			//listOfWords = StopWordRemoval(listOfWords,stoplist);
			//slist.removeAll(Arrays.asList(stoplist));
  }
}
