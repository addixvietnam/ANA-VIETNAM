/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ana.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Nhan.Lam
 */
public class ANABackup {

    public static void main(String[] args) {
        String pathDirectory = Paths.get("").toAbsolutePath().toString() + "\\output";
        String backupDirectory = pathDirectory + "\\backup";
        String backupDataSourceDir = backupDirectory + "\\data_page_source";
        String backupDataStorageDir = backupDirectory + "\\data_storage";
        List<String> listFolderSource = getListFolder(pathDirectory + "\\data_page_source\\HTML");
        List<String> listFileStorage = getListFile(pathDirectory + "\\data_storage");
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");   
        Date presentDate = new Date();
        String myDate = dateFormat.format(presentDate);
        
        //Compress folder
        compressABackup(pathDirectory, backupDataSourceDir, myDate, listFolderSource);
        //Copy to new folder
        copyABackup(pathDirectory, backupDataStorageDir, myDate, listFileStorage);
    }
    
    public static void copyABackup(String pathDirectory, String backupDataStorageDir, String myDate, List<String> listFileStorage){
        String strDest = backupDataStorageDir + "\\" + myDate;
        File destFolder = new File(strDest);
        if(!destFolder.exists()){
            destFolder.mkdir();
        }

        for(String myFile : listFileStorage){
            File source = new File(pathDirectory + "\\data_storage\\" + myFile);
            File dest = new File(strDest + "\\" + myFile);            
        }
    }
    
    private static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
        
        
    
    public static void compressABackup(String pathDirectory, String backupDataSourceDir, String myDate, List<String> listFolderSource){
        
        String strDest = backupDataSourceDir + "\\HTML\\" + myDate;
        File destFolder = new File(strDest);
        if(!destFolder.exists()){
            destFolder.mkdir();
        }
        ZipFiles myZip = new ZipFiles();
        for(String folder : listFolderSource){
            File strSource = new File(pathDirectory + "\\data_page_source\\HTML\\" + folder);

            myZip.zipDirectory(strSource, strDest + "\\" + folder + ".zip");
        }
    }
    
    public static List<String> getListFolder(String parentFolder){
        List<String> myListFolder = new ArrayList<>();
        File[] listFile = new java.io.File(parentFolder).listFiles();
        for(java.io.File file: listFile){
            if(file.isDirectory()){ 
                myListFolder.add(file.getName());                
            }
        }
        return myListFolder;
    }
    
    public static List<String> getListFile(String parentFolder){
        List<String> myListFile = new ArrayList<>();
        File[] listFile = new java.io.File(parentFolder).listFiles();
        for(java.io.File file: listFile){
            if(file.isFile()){ 
                myListFile.add(file.getName());                
            }
        }
        return myListFile;
    }    
    
}
