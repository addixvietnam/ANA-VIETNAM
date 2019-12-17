/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ana.redownload;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class File {
    public ArrayList<WebLink> readErrorLinkFile(String fileName){
        ArrayList<WebLink> arrLink = new ArrayList<>();
        String line = null;        
        String[] arrElementLine = new String[3];
        WebLink myWebLink;
        try {
            java.io.File file = new java.io.File(fileName);
            if(file.exists()){
                // FileReader reads text files in the default encoding.
                FileReader fileReader = new FileReader(fileName);
                // Always wrap FileReader in BufferedReader.
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                while((line = bufferedReader.readLine()) != null) {
                    if(line.trim() != ""){
                        arrElementLine = line.split(",");
                        myWebLink = new WebLink(arrElementLine[0].trim(), arrElementLine[1].trim(), arrElementLine[2].trim());
                        arrLink.add(myWebLink);
                    }
                }
                // Always close files.
                bufferedReader.close();
            }
        }catch(FileNotFoundException ex) {
            System.err.println("FindNotFoundReadFile: " + ex.toString());
            return null;
        }catch(Exception ex) {
            System.err.println("ReadFile: " + ex.toString());
            return null;
        }        
        return arrLink;
    }
    
    public void writeErrorLinkFile(String fileName, String content){
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName, true), StandardCharsets.UTF_8))){
            writer.write(content);
            writer.close();
        } catch (IOException ex) {
            System.err.println("Writing error link to file " + ex.toString());            
        }        
    }
    
    public void deleteErrorFile(String fileName){
        java.io.File myFile = new java.io.File(fileName);
        if(myFile.exists()){
            myFile.delete();
        }
    }
    
    public Map<String, String> readLinkDownload(String fileName){
        Map<String, String> mapLink = new HashMap<String, String>();
        //Read from excel file
        try{           
            FileInputStream fileInputStream = new FileInputStream(fileName);
            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();
            XSSFWorkbook myWorkBook = new XSSFWorkbook(fileInputStream);
            XSSFSheet mySheet = myWorkBook.getSheet("Sheet1");
            Iterator<Row> rowIterator = mySheet.iterator();
            int colDataSize = (rowIterator.next()).getLastCellNum();
            int rowDataSize = mySheet.getLastRowNum();
            if(colDataSize > 5){                
                //Assign data to array            
                int i = 0;
                while(rowIterator.hasNext()){                                                   
                    Row row = rowIterator.next();
                    if(row.getRowNum() > 0){  
                        Iterator<Cell> cellIterator = row.cellIterator();
                        i = 0;
                        String webName = "";
                        String keyword = "";
                        while ((cellIterator.hasNext()) && (i < 5)){
                            Cell cell = cellIterator.next();
//                            System.out.println(i + ".Cell Value " + dataFormatter.formatCellValue(cell));
                            if(i == 3){//get keyword
                                keyword = dataFormatter.formatCellValue(cell);
                            }else if(i == 4){//get link data
                                webName = dataFormatter.formatCellValue(cell);
                            }
                            i++;
                        }
                        
                        if(!webName.isEmpty()){
                            mapLink.put(webName, keyword);
                        }                        
                    }
                }
            }else{
                return null;
            }
        }catch(IOException ex){
            System.err.println("Error reading file " + fileName);
            return null;
        }
        return mapLink;
    }
}
