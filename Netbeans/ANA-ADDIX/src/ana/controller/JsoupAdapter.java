package ana.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupAdapter {
    private Document document;
    private String url; //domain url
    private Elements srcElements = new Elements();    
    /**
     * Connect to url then get domain to prepare for download page
     * @param url
     * @return 
     */
    public boolean connect(String url) {
        try {
            document = Jsoup.connect(url)
                .header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(600000)
                .get();
            this.url = getCurrentURL();
        } catch (Exception e) {
            Logger.getLogger(JsoupAdapter.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
        return true;
    }
    /**
     * Parse string html to document 
     * @param htmlContent 
     */
    public void parseHtml2Doc(String htmlContent) {
        document = Jsoup.parse(htmlContent);
    }    
    
    /**
     * Get the current connected URL
     * @return
     */
    public String getCurrentURL() {
        String strUrl = document.location();
        if (strUrl.isEmpty())
            strUrl = this.url;
        else {
            int indexOfDot = strUrl.indexOf('.');
            int indexOfCross = strUrl.indexOf('/', indexOfDot);
            strUrl = strUrl.substring(0, indexOfCross);
        }
        if (!strUrl.endsWith("/")) 
            strUrl += '/';
        return strUrl; 
    }    
    
    /**
     * Check & fix url get from src in html
     * @param url
     * @return 
     */
    public String getCorrectUrl(String url) {
        String correctUrl = null;
        if (url.startsWith("http"))
            correctUrl = url;
        if (url.startsWith("//"))
            correctUrl = "https:" + url;
        else if (url.startsWith("/"))
            correctUrl = this.url + url;
        return correctUrl;
    }
    
    /**
     * Get list of reference source on website
     * @return String[]
     */
    public String[] getAllSrc() { 
        ArrayList<String> listSrc = new ArrayList<>();
        for (Element element : document.getElementsByAttribute("src")) {
            String src = element.attr("src");
            src = getCorrectUrl(src);
            if (null == src){
                continue;
            }                
            if (listSrc.contains(src) || src.isEmpty()) // || !src.contains("/")) || src.contains("https://apis.google.com"))
            {
                continue;
            }
            listSrc.add(src);
            srcElements.add(element);
//            System.out.println(src);            
        }

        for (Element element : document.getElementsByTag("IMG")) {
            String image = element.attr("data-src");
            image = getCorrectUrl(image);
            if (null == image)
                continue;
            if (listSrc.contains(image) || image.isEmpty())
                continue;
            listSrc.add(image);
            srcElements.add(element);
//            System.out.println(image);
        }


        for (Element element : document.getElementsByAttribute("href")) {
            String css = element.attr("href");
            css = getCorrectUrl(css);
            if (null == css)
                continue;
            if (listSrc.contains(css) || !css.contains(".css"))
                continue;
            listSrc.add(css);
            srcElements.add(element);
//            System.out.println(css);
        }      
        return listSrc.toArray(new String[listSrc.size()]);
    }    
    /**
     * get name of file from url 
     * @param url
     * @return 
     */
    public String getFileName(String url) {
        String fileName = null;
        try {
            URL urlObject = new URL(url);
            fileName = urlObject.getFile();
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
            if (fileName.isEmpty())  {
                fileName = null;
            }
        } catch (MalformedURLException ex) {
              fileName = null;
        }
        return fileName;
    }    
    
    /**
     * Download the file from URL and store at outputFile directory
     * @param url
     * @param outputFile
     * @return 
     */
    public String download(String url, String outputFile) {
        String correctURL = getCorrectUrl(url);
        String fileName = getFileName(url);
        String fullFileName = outputFile + File.separator + fileName;
        if (fileName.contains("?")) {
            fileName = fileName.substring(0, fileName.lastIndexOf("?"));
            fullFileName = outputFile + File.separator +  fileName;
        }

         /* Check file and skip download if file exist */
        File output = new File(fullFileName);
        if (output.exists())
            return fullFileName;
//        System.out.println("Download from " + correctURL);
        try (FileOutputStream outputStreamFile = new FileOutputStream(output)) {
            byte[] data = Jsoup.connect(correctURL).header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .ignoreContentType(true)
                .maxBodySize(0)
                .timeout(0)
                .execute()
                .bodyAsBytes();
                outputStreamFile.write(data);
        } catch (IOException ex) {
            Logger.getLogger(JsoupAdapter.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return fullFileName;
    }    
    /**
     * Update element
     * @param index
     * @param value 
     */
    public synchronized void updateElement(int index, String value) {
        Element element = srcElements.get(index);
        if (!element.attr("src").isEmpty()) {
            element.attr("src", value);
        } else if (!element.attr("href").isEmpty()) {
            element.attr("href", value);
        } else if (!element.attr("style").isEmpty()) {
            String newValue = "background-image: url(\"" + value + "\")";
            element.attr("style", newValue);
        }
    }
        
    /**
     * Get the HRML source of website
     * @return  String
     */
    public String getHtml() {
        return document.outerHtml();
    }

    /**
     * Get the title of website
     * @return  String
     */
    public String getTitle() {
        return document.title();
    }    

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        int indexOfDot = url.indexOf('.');
        int indexOfCross = url.indexOf('/', indexOfDot);
        this.url = url.substring(0, indexOfCross);        
    }

    public Elements getSrcElements() {
        return srcElements;
    }

    public void setSrcElements(Elements srcElements) {
        this.srcElements = srcElements;
    }
    
    
}
