package Downloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.nodes.Element;
import java.net.*;
import java.io.*;

public class URLReaderMod {

    public static void main(String[] args) throws Exception {


      /**
       * @author Gregory Krulin
     		This program uses JSoup to parse HTML source code into a text file which is downloaded onto the computer.
     		Formating is still wonky, but using a for loop with element selector for "p" (which is paragraph for HTML) have cleaned up the text files significantly.
     		
     		To Do:
     		Work on formating for text files so easier to read
     		
     		Primary Goal:
     		go through an url and download files on the webpage
      **/
//https://www.theatlantic.com/magazine/archive/2008/11/why-i-blog/307060/ 
    	
    	//http://www.cs.miami.edu/home/wuchtys/CSC322-18S/Content/CLanguage/Arrays.shtml
    	//https://www.newyorker.com/magazine/2011/04/18/farther-away-jonathan-franzen
    	//https://www.theatlantic.com/magazine/archive/2008/11/why-i-blog/307060/
        //BufferedReader in = null;
        //int trycount = 0;
        URL oracle = new URL("http://www.cs.miami.edu/home/odelia/teaching/csc317_spring18/problems/index.html");
        Document doc = Jsoup.parse( oracle, 3 *1000);
        //Whitelist w1 = new Whitelist().none();

        //creates url object and from url object it parses it into a Document using Jsoup
        File file = new File("newFile_3.txt");
//trys and create a file onto the harddisk
        try{

        boolean file_create = file.createNewFile();
        if( file_create){
          System.out.println("File created");
        }
        else{
          System.out.println("File already exists");
        }
      }
      catch( IOException e ){
        System.out.println("Error");
        e.printStackTrace();
      }
        //creates PrintWriter object that uses file created above
      PrintWriter writing = new PrintWriter(file);
      //String cleanText = Jsoup.clean( doc.text(), w1);


        System.out.println("Connected");
       // writing.print(cleanText);
       // String inputLine;
        //goes through the elements of the Document from above, selecting only those with "p" -- p in html refers to paragraphs.
        //Prints the paragraph into the file using the PrintWriter, and a space for more readibility
        for( Element element : doc.select("p"))
        {
            writing.println(element.text());
            writing.println();

        }
            //System.out.println(inputLine);
             

        writing.close();
        //file.close();
        //in.close();
    }
}