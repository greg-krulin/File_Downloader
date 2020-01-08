package Downloader;

import java.net.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
/*testing
 * 
 */
public class JsoupDemo {
    public static void main(String[] args) throws Exception {
        URL url = new URL("http://www.cs.miami.edu/home/wuchtys/CSC322-18S/Content/CLanguage/Arrays.shtml");
        Document doc = Jsoup.parse(url, 3*1000);

        String text = doc.body().text();

        System.out.println(text); // outputs 1
    }
}