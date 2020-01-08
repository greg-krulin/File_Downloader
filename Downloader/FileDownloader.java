package Downloader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
//import org.jsoup.safety.Whitelist;
import org.jsoup.nodes.Element;
import java.net.*;
import java.io.*;
import java.util.ArrayDeque;
//import java.util.Queue;
//import java.io.*;
import java.util.Set;
import java.util.HashSet;
import org.apache.commons.io.FileUtils;
import java.util.Scanner;

/**
 * 
 * @author Gregory Krulin
 * 
 * 
 * This program currently has three main methods: urlDownloader, urlScrapper, and makeDirectory.
 * Currently, how it works is to go down to main, switch the url string with the webpage you want to scrap.  The webpage url will be passed to makeDirectory which will create a directory at the end of the path.
 * Path has been decalred as a variable so it can be easily changed by a user.  makeDirectory will make the directory and return the entire path.
 * 
 *The program will then go through the webpage using the urlScrapper method, putting any links with .pdf, .text, .java or other filetypes user wants to scrap for into an ArrayDeque. Currently, have to manually enter
 *and remove element.attr("a[href]").endsWith(".filetype") in the for loop for scrapper method. Also, have a static set to check for repeats; so only single entries will be placed in ArrayDeque.  
 *The method will return an ArrayDeque to the main method.
 *
 *Finally, in the main method it will poll arraydeque until empty.  Each entry polled will be passed to urlDownloader along with the entire path variable.  It downloads each file into the directory, with data type at
 *end.
 *
 * libraries used:
 * JSoup - for data scrapping of webpages
 * Apache Commons IO - for downloading webpages as a specific file type
 *
 *To Do:
 * (more important)Make it so program can access webpages which require login information.
 * Add GUI  -Give options to user. Either use defualt path or allow user to give their own path. Input url with gui rather than directly into code?
 * Can be slow when dealing with many .pdf entries (that go to 80ish mb so far.)  Perhaps can make program quicker by using different data types?
 * ---Fix issue with Apache of making .pdf files unable to be read (no idea why this happens so far)
 */

public class FileDownloader{
	
	private static Set<String> set = new HashSet();

	/**
	 * 
	 * @param urlString - url html string to download
	 * this method uses Apache commons IO API, specifically FileUtils copyURLToFile
	 * takes in url string, creates a file with last index of url string with the .filetype and creates a file with it
	 * it then uses Apache method to copy url to the new file, with java and pdf info saved
	 * @throws Exception
	 */
	public static void urlDownload( String urlString, String filePath) throws Exception {
        URL url = new URL(urlString);
        String fileName = urlString.substring(urlString.lastIndexOf("/") + 1 );
       File file = new File( filePath +"/"+fileName );
        
        try{
        boolean file_create = file.createNewFile();
        if( file_create){
          System.out.println(fileName+" created");
        }
        else{
          System.out.println(fileName+" already exists");
        }
      }
      catch( IOException e ){
        System.out.println("Error");
        e.printStackTrace();
      }
        
       try {
    	   FileUtils.copyURLToFile(url, file);
       }
       catch( IOException e) {
    	   System.out.println("Error");
    	   e.printStackTrace();
       }
        
        
	}
	
	/**
	 * 
	 * @param urlString - website we are scrapping for links
	 * @return ArrayDeque<String> list - will be queue of links
	 * @throws Exception
	 * Goes through the webpage url given and scrapes all links to file types we would like to download
	 * Adds to
	 */
	public static ArrayDeque<String> urlScrapper( String urlString) throws Exception {
		ArrayDeque<String> list = new ArrayDeque();
        URL url = new URL(urlString);
        Document doc = null;
        int tryCount = 0;
        while( tryCount < 10 && doc == null){
        	 try {
        		tryCount++;
                 doc = Jsoup.parse( url, 3 *1000);
        	}
        	 catch(IOException e) {
        		 Thread.sleep(3000);
        		 e.printStackTrace();
        		 System.out.println("Trying to parse again");
        	 }
        }
        
        if( doc == null) {
        	throw new IOException("Unable to connect");
        }
        //
        for(Element element: doc.select("a[href]")) {
        	if( (element.attr("abs:href").endsWith(".pdf") ||
        			element.attr("abs:href").endsWith(".java") ||
        			element.attr("abs:href").endsWith(".txt"))  && !(set.contains(element.attr("abs:href") ) ) ) {
        		list.offer(element.attr("abs:href"));
        		set.add(element.attr("abs:href"));
        	}
        }
        
        return list;
        
	}
	/**
	 * 
	 * @param urlName from urlname will create a directory associated with it
	 * @return directoryName which is the path to the new directory
	 * the method below will take in a URL string, create a directory with it.  This is the spot for all the downloads of the urlDownloader method
	 */
	public static String makeDirectory( String urlName, String oldPath) {
		//String path = "/home/yoyogrego/Downloader/";
		//String path = "/media/removable/Lexar/";
        String directoryName = oldPath + urlName.substring(urlName.lastIndexOf("/") + 1 );//+file;
        
        if( directoryName.equals( oldPath)) //"/home/yoyogrego/Downloader/" ) )
        	directoryName =  oldPath+ "/temp";//"/home/yoyogrego/Downloader/temp";
        
        File dir = new File(directoryName);
        
        boolean create = dir.mkdir();
      
		if(create) {
			System.out.println(directoryName+" created");
		}
		else {
			System.out.println(directoryName + "already exists");
		}

		
		return directoryName;
	}
	
	public static void main( String[] args) throws Exception {
		//http://www.cs.miami.edu/home/vjm/csc220/prog12/
		//String url = "https://en.wikipedia.org/wiki/New_I/O_(Java)";
		
		//url for webpage to be scrapped
		Scanner keyboard = new Scanner( System.in );
		
		System.out.print("Please input url: ");
		String url = keyboard.nextLine();
		
		System.out.print("Please input folder path: ");
		String firstPath = keyboard.nextLine();
		
		String path = makeDirectory(url, firstPath);
		

		
		//returns an arraydeque of strings for the files on the webpage
		ArrayDeque<String> list = urlScrapper(url);
		/**
		for( String string: list) {
			System.out.println(string);
			System.out.println(string.substring(string.lastIndexOf("/") + 1));
		}
		//System.out.println("Done");
		 * 
		 */
		
		//downloads each webpage
		while( !(list.isEmpty())){
			url = list.poll();
			urlDownload(url,path);
		}
	}
}