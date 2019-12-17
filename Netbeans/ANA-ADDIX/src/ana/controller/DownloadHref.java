/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ana.controller;

import ana.GlobalVars;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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
            String folderSourceDownload, String htmlContent, String webName, String keyword) throws InterruptedException, UnsupportedEncodingException{
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
        final String folderExtension = URLDecoder.decode(folderSupportLinkDownload, "UTF-8") ;
        for (int index = 0 ; index < srcs.length; index++) {
            try {
                final int fIndex = index;
                final String fSrc = URLDecoder.decode(srcs[index], "UTF-8") ;
                Thread thread = new Thread(() -> {
                    try {
                        sem.acquire();
                        download(jsoupAdapter, fIndex, fSrc, folderExtension);
                    } catch (InterruptedException ex) {
                        System.out.println("Downloading InterruptedException error " + ex.toString());
                        GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                            GlobalVars.LOG_ADDIX.formatStringError("Downloading InterruptedException error" , ex.toString())); 
                    } finally {
                        sem.release();
                    }
                });
                thread.start();
                subThreads.add(thread);
            } catch (UnsupportedEncodingException ex) {
                throw ex;
            }
        }
        for (Thread thread : subThreads) {
              try {
                    thread.join();
              } catch (InterruptedException ex) {
                    System.out.println("Joining thread download error " + ex.toString());     
                    GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringError("Joining thread download error" , ex.toString()));                     
              }
        }
        // \/?:*"<>|
        String htmlName = jsoupAdapter.getTitle()
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

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(folderSourceDownload + File.separator + htmlName + ".html", true), StandardCharsets.UTF_8))){
//                BufferedWriter writer = new BufferedWriter(new FileWriter(folderSourceDownload + File.separator + htmlName + ".html"))) {
            writer.write(jsoupAdapter.getHtml());
            writer.close();
        } catch (IOException ex) {
            System.out.println("Writing file html download error " + ex.toString());            
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                GlobalVars.LOG_ADDIX.formatStringError("Writing file html download error", linkDownload  + "\n" + ex.toString())); 
            String bugLinks = webName + "," + keyword + "," + linkDownload;
            LinkErrors.writeLinkErrors(GlobalVars.WORK_DIRECTORY + "/output/errorLinks.txt", bugLinks);
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
            int lastIndex = downloadDir.lastIndexOf("\\");
            if(lastIndex > 0){
                lastIndex ++;
            }else if(lastIndex < 0){
                lastIndex = downloadDir.lastIndexOf("/");
            }
            String nameFolder = downloadDir.substring(lastIndex, downloadDir.length());
            String strFileDownload = "../" + nameFolder + "/" + fileName;
            jsoupAdapter.updateElement(elementIndex, strFileDownload);            
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
