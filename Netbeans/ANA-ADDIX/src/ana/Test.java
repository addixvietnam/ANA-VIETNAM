/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ana;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
/**
 *
 * @author Nhan.Lam
 */
public class Test {
    public static void main(String[] args) throws Exception{
//        Test myDownload = new Test();
//        myDownload.saveUrl("D:/web.html", "https://newswitch.jp");
        
        String fileName = "D:/web.html";
        
        Document doc = Jsoup.parse(new File(fileName), "utf-8"); 
        
        
        System.out.println(doc.title());
    }
    public void saveUrl(final String filename, final String urlString)
        throws MalformedURLException, IOException {
        BufferedInputStream in = null;
        FileOutputStream fout = null;
        try {
            in = new BufferedInputStream(new URL(urlString).openStream());
            fout = new FileOutputStream(filename);

            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }
}
