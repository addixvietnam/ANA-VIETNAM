/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ana.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Nhan.Lam
 */
public class DownloadHref {
    private String linkDownload;
    private String folderSourceDownload;
    private String folderSupportLinkDownload;
    
    public static void savePage(String linkDownload, String folderSupportLinkDownload, 
            String folderSourceDownload, String htmlContent) throws InterruptedException{
        //Check folder exist, if not exist create it
        File isFolderSrcDownload = new File(folderSourceDownload);
        File isFolderSupportLinkDownload = new File(folderSupportLinkDownload);
        if(!isFolderSrcDownload.isDirectory()){
            isFolderSrcDownload.mkdir();
        }
        if(!isFolderSupportLinkDownload.isDirectory()){
            isFolderSupportLinkDownload.mkdirs(); 
        }
        //Using Jsoup to read Href 
        JsoupAdapter jsoupAdapter = new JsoupAdapter();

        if (null != htmlContent) {
            jsoupAdapter.setUrl(linkDownload);
            jsoupAdapter.parseHtml2Doc(htmlContent);
        } else {
            jsoupAdapter.connect(linkDownload);
        }
        String[] srcs = jsoupAdapter.getAllSrc();

        int numOfThreads = 4;
        Semaphore sem = new Semaphore(numOfThreads);
        ArrayList<Thread> subThreads = new ArrayList<>();
        for (int index = 0 ; index < srcs.length; index++) {
            final int fIndex = index;
            final String fSrc = srcs[index];
            Thread thread = new Thread(() -> {
                try {
                    sem.acquire();
                    download(jsoupAdapter, fIndex, fSrc, folderSupportLinkDownload);
                } catch (InterruptedException ex) {                    
                    System.out.println("Downloading error " + ex.toString());                    
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
        String htmlName = jsoupAdapter.getTitle()
                .replaceAll("\\|","")
                .replaceAll("\\?", "")
                .replaceAll("<", "")
                .replaceAll(">", "")
                .replaceAll(":", "")
                .replaceAll("  ", "")
                .replaceAll(" ", "_");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(folderSourceDownload + File.separator + htmlName + ".html"))) {
            writer.write(jsoupAdapter.getHtml());
        writer.close();
        } catch (IOException ex) {
            System.out.println("Writing file download error " + ex.toString());            
        }
   }
    
   public static void download(JsoupAdapter jsoupAdapter, int elementIndex, String src, String downloadDir) {
        String fullFileName = jsoupAdapter.download(src, downloadDir);
        if (null == fullFileName)
            return;

        String pattern = src;
        if (src.contains("?"))
            pattern = src.substring(0, src.lastIndexOf("?")) + "\\" + src.substring(src.lastIndexOf("?"));
        String fileName = fullFileName.substring(fullFileName.lastIndexOf("\\") + 1);
        {
            jsoupAdapter.updateElement(elementIndex, "../Downloads/" + fileName);
        }
   }    

    public String getLinkDownload() {
        return linkDownload;
    }

    public void setLinkDownload(String linkDownload) {
        this.linkDownload = linkDownload;
    }

    public String getFolderSourceDownload() {
        return folderSourceDownload;
    }

    public void setFolderSourceDownload(String folderSourceDownload) {
        this.folderSourceDownload = folderSourceDownload;
    }

    public String getFolderSupportLinkDownload() {
        return folderSupportLinkDownload;
    }

    public void setFolderSupportLinkDownload(String folderSupportLinkDownload) {
        this.folderSupportLinkDownload = folderSupportLinkDownload;
    }
    
    
}
