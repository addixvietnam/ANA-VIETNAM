/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ana.redownload;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author Nhan.Lam
 */
public abstract class PageDownload extends ADDIXSelenium{
    
    private WebDriver seleniumDriver;
    private String browserDriver = "";    
    private static ADDIXJsoup addixJSoup;
    
    public PageDownload(String browserDriver, String driverName) {
        super(browserDriver, driverName);
        this.browserDriver = browserDriver;
        this.seleniumDriver = getSeleniumDriver(); 
    }
    
    private static void download(ADDIXJsoup addixJSoup, int elementIndex, String src, String downloadDir) throws IOException {
        String fullFileName = addixJSoup.download(src, downloadDir);
        if (null == fullFileName)
            return;

        String pattern = src;
        if (src.contains("?"))
            pattern = src.substring(0, src.lastIndexOf("?")) + "\\" + src.substring(src.lastIndexOf("?"));
        String fileName = fullFileName.substring(fullFileName.lastIndexOf("\\") + 1);
        {
            int lastIndex = downloadDir.lastIndexOf("\\");
            if(lastIndex > 0){
                lastIndex ++;
            }else if(lastIndex < 0){
                lastIndex = downloadDir.lastIndexOf("/");
            }
            String nameFolder = downloadDir.substring(lastIndex, downloadDir.length());
            String strFileDownload = "../" + nameFolder + "/" + fileName;

            addixJSoup.updateElement(elementIndex, strFileDownload);
        }
    }    
    
    public static void savePage(String url, String pathSource, String pathExtension, String htmlContent, String webName, String keyword) throws Exception{
        //Check folder exist, if not exist create it
        java.io.File fPathSource = new java.io.File(pathSource);
        if(!fPathSource.isDirectory()){
            fPathSource.mkdir();
        }
        java.io.File fPathExtension = new java.io.File(pathExtension);
        if(!fPathExtension.isDirectory()){
            fPathExtension.mkdir();
        }        
        //Using Jsoup to read Href 
        addixJSoup = new ADDIXJsoup() {};
        if (null != htmlContent) {
            addixJSoup.getDomain(url);
            addixJSoup.parse(htmlContent);
        } else {            
            addixJSoup.connect(url);  
            addixJSoup.getCurrentDomain(addixJSoup.getDocument());
        }
        String[] srcs = addixJSoup.getAllSrc();
        int numThread = 1;
        Semaphore sem = new Semaphore(numThread);
        ArrayList<Thread> subThreads = new ArrayList<>();
        for (int index = 0 ; index < srcs.length; index++) {
            final int fIndex = index;
            final String fSrc = srcs[index];
            Thread thread = new Thread(() -> {
                try {
                    sem.acquire();
                    download(addixJSoup, fIndex, fSrc, pathExtension);
                } catch (InterruptedException ex) {                    
                    System.out.println("Downloading error " + ex.toString());                    
                } catch (IOException ex) {
                    System.out.println("Writing file downloaded error " + ex.toString());                      
                } finally {
                    sem.release();
                }
            });
            thread.start();
            subThreads.add(thread);
        }
        for (Thread thread : subThreads) {
              try {
                  thread.join();
              } catch (InterruptedException ex) {
                  System.out.println("Joining thread download error " + ex.toString());                  
              }
        }
        String htmlName = addixJSoup.getTitle()                
                .replace("\\\\", "_")
                .replaceAll("\\/", "_")
                .replaceAll(":", "_")
                .replaceAll("\\?", "_")                
                .replaceAll("\\|","_")                
                .replaceAll("<", "_")
                .replaceAll(">", "_")
                .replaceAll("\\*", "_")
                .replace("\"", "_")
                .replaceAll("  ", "")
                .replaceAll(" ", "_");

//        System.out.println(pathSource + File.separator + htmlName + ".html");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pathSource + java.io.File.separator + htmlName + ".html"))) {
            writer.write(addixJSoup.getHtml());
            writer.close();
        } catch (IOException ex) {
            String strLine = webName + "," + keyword + "," + url;
            ANARedownload.arrErrLink.add(strLine);
            System.err.println("Writing file download error " + ex.toString());                                    
        }        
    }        
    
}
