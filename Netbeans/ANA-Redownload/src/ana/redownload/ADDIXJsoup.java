/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ana.redownload;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Nhan.Lam
 */
public abstract class ADDIXJsoup {
    private String domain = "";    
    private Document document;
    private Elements srcElements = new Elements();
    private static final String AVAILABLE_SRC = "Avaiablabe";
    
    /**
     * Get domain from string url
     * @param url
     * @return 
     */
    public String getDomain(String url){
        int indexOfDot = url.indexOf('.');
        int indexOfCross = url.indexOf('/', indexOfDot);
        domain = url.substring(0, indexOfCross);
        return domain;
    }
    /**
     * Get current domain in document
     * @param document
     * @return 
     */
    public String getCurrentDomain(Document document){
        String url = document.location();
        if (url.isEmpty())
            url = this.domain;
        else {
            int indexOfDot = url.indexOf('.');
            int indexOfCross = url.indexOf('/', indexOfDot);
            url = url.substring(0, indexOfCross);
        }
        if (!url.endsWith("/")) {
            url += '/';
        }            
        this.domain = url;
        return this.domain; 
    }
    
    /**
     * Parse html to document
     * @param htmlContent
     * @return 
     */
    public Document parse(String htmlContent){
        this.document = Jsoup.parse(htmlContent);
        return document;
    }
    
        /**
     * Connect to url then get domain to prepare for download page
     * @param url
     * @return 
     */
    public void connect(String url) throws Exception {
        try {
            document = Jsoup.connect(url)
                .header("Accept-Encoding", "gzip, deflate")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0")
                .maxBodySize(0)
                .timeout(600000)
                .get();            
        } catch (Exception ex) {            
            throw ex;
        }        
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
            correctUrl = this.domain + url;
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
            if (src.isEmpty()) 
                // || listSrc.contains(src) ||  !src.contains("/")) || src.contains("https://apis.google.com"))
            {
                continue;
            }
            if (listSrc.contains(src)) {
                listSrc.add(src+AVAILABLE_SRC);
            } else {
                listSrc.add(src);
            }
            
            srcElements.add(element);
//            System.out.println(src);            
        }

        for (Element element : document.getElementsByTag("IMG")) {
            String image = element.attr("data-src");
            image = getCorrectUrl(image);
            if (null == image)
                continue;
            if (image.isEmpty())
                continue;
            if (listSrc.contains(image)) {
                listSrc.add(image+AVAILABLE_SRC);
            } else {
                listSrc.add(image);
            }
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
            if (listSrc.contains(css)) {
                listSrc.add(css+AVAILABLE_SRC);
            } else {
                listSrc.add(css);
            }
            srcElements.add(element);
//            System.out.println(css);
        }      
        return listSrc.toArray(new String[listSrc.size()]);
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
    
    /**
     * Download the file from URL and store at outputFile directory
     * @param url
     * @param outputFile
     * @return 
     */
    public String download(String url, String outputFile) throws IOException {
        if (url.contains(AVAILABLE_SRC)) {
            String fileName = getFileName(url.replaceAll(AVAILABLE_SRC, ""));
            String fullFileName = outputFile + java.io.File.separator + fileName;
            if (fileName.contains("?")) {
                fileName = fileName.substring(0, fileName.lastIndexOf("?"));
                fullFileName = outputFile + java.io.File.separator +  fileName;
            }
            return fullFileName;
        }
        String correctURL = getCorrectUrl(url);
        String fileName = getFileName(url);
        String fullFileName = outputFile + java.io.File.separator + fileName;
        if (fileName.contains("?")) {
            fileName = fileName.substring(0, fileName.lastIndexOf("?"));
            fullFileName = outputFile + java.io.File.separator +  fileName;
        }

        /* Check file and skip download if file exist */
        java.io.File output = new java.io.File(fullFileName);
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
            throw ex;            
        }
        return fullFileName;
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
    
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }    
}
