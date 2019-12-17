/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ana.redownload;

import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Nhan.Lam
 */
public class ANARedownload {
    public static ArrayList<String> arrErrLink = new ArrayList<>();
    
    public static void main(String[] args) {
        String pathDirectory = Paths.get("").toAbsolutePath().toString() + "\\output";
        System.out.println("Path of Directory: " + pathDirectory);
        String fileErrorLinkName = pathDirectory + "\\errorLinks.txt";
        ADDIXSelenium mySelenium = new ADDIXSelenium("D:\\Test\\chromedriver.exe", "chrome"){};

        ArrayList<WebLink> arrWebLink;
        
        //Read error link file to array list
        File myFile = new File();
        arrWebLink = myFile.readErrorLinkFile(fileErrorLinkName);
        Map<String, String> mapLink;
        //Check every error link in output excel file, if not exist is skip, else download it
        //If download error write to array list error
        if((!arrWebLink.isEmpty()) && (arrWebLink != null)){            
            String outputExcelNameFile = "";
            java.io.File[] listFile;
            String webNameRead = arrWebLink.get(0).getWebName();
            for(int i = 0; i < arrWebLink.size(); i++){
                mapLink = new HashMap<>();
                String myWebName = arrWebLink.get(i).getWebName();
                String myKeyword = arrWebLink.get(i).getKeyword();
                String myLinkDownload = arrWebLink.get(i).getLinkDownload();                
                String folderExcelFile = pathDirectory + "\\data_page_source\\HTML\\" + myWebName.toLowerCase();
                System.out.println(i + ". " + myWebName + " - " + myKeyword + " - " + myLinkDownload);
                if(i != 0){
                    if(!webNameRead.equals(myWebName)){
                        listFile = new java.io.File(folderExcelFile).listFiles();
                        for(java.io.File file: listFile){
                            if(file.isFile()){ 
                                outputExcelNameFile = file.getAbsolutePath();                        
                            }
                        }
                        System.out.println("\t" + i + ".Path of excel file " + outputExcelNameFile);
                        //Read link download from output\data_page_source\HTML folder
                        mapLink= myFile.readLinkDownload(outputExcelNameFile);
                    }
                }else if(i == 0){
                    listFile = new java.io.File(folderExcelFile).listFiles();
                    for(java.io.File file: listFile){
                        if(file.isFile()){ 
                            outputExcelNameFile = file.getAbsolutePath();                        
                        }
                    }
                    System.out.println("\t" + i + ".Path of excel file " + outputExcelNameFile);
                    //Read link download from output\data_page_source\HTML folder
                    mapLink = myFile.readLinkDownload(outputExcelNameFile);
                }
                webNameRead = myWebName;
                if((mapLink.isEmpty()) || (mapLink == null)){
                    System.out.println("Skipped website " + myWebName);
                    arrErrLink.add(myWebName + "," + myKeyword + "," + myLinkDownload);
                }else{
                    //Download website        
                    String folderExtension = folderExcelFile + "\\HTML\\downloads" ;        
                    String folderHtml = folderExcelFile + "\\HTML\\" + myKeyword;
                    String htmlContent = "";          
                                        
                    mySelenium.loadUrl(myLinkDownload);
                    htmlContent = mySelenium.getPageSource(myLinkDownload);
                    
                    try {
                        PageDownload.savePage(myLinkDownload, folderHtml, folderExtension, htmlContent, myWebName, myKeyword);
                    } catch (Exception ex) {
                        System.out.println("Error download" + ex.toString());
                    }
                    
                }
            }
        }        
        //Before finish program. Clear all date in in error link file. 
        // If array list error have exist data, write to this file
        myFile.deleteErrorFile(fileErrorLinkName);
        for(String strContent: arrErrLink){
            myFile.writeErrorLinkFile(fileErrorLinkName, strContent);
        }
        //Close the program
        mySelenium.closeDriver();
    }
    
}
