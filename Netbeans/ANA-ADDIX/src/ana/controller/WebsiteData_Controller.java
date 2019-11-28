package ana.controller;

import ana.GlobalVars;
import ana.model.WebsiteData;
import ana.model.WebsiteElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import vn.addix.utils.Log;


public class WebsiteData_Controller {

    public WebsiteData_Controller() {        
    }    
    
    /**
     * Read all data on url item, then save to array list
     * @return true if successful and otherwise
     */
    public boolean readDataWebsite(){       
        for(int i = 0; i < GlobalVars.LIST_WEBSITE_DATA.size(); i++){
            String urlItem = GlobalVars.LIST_WEBSITE_DATA.get(i).getUrlArticle().toString().trim();
            String webName = GlobalVars.LIST_WEBSITE_DATA.get(i).getWebName().toString().trim();
            WebsiteElement elementWebsite = GlobalVars.MAP_WEBSITE_ELEMENTS.getWebsiteElement(webName);
            try{
                Document urlItemDoc = Jsoup.connect(urlItem).get();
                String charset = urlItemDoc.charset().toString();
                
                String post_date = urlItemDoc.select(elementWebsite.getPostDate().trim()).text();
                String source = urlItemDoc.select(elementWebsite.getSource().trim() ).text();
                Elements elementContents = urlItemDoc.select(elementWebsite.getContent().trim());
                
                String content = "";
                for(Element elementChild : elementContents){
                    if(content.length() == 0){
                        content = content + elementChild.text();
                    }else{
                        content = content + "\n\r" + elementChild.text();
                    }
                }
                //Write to global variables
                GlobalVars.LIST_WEBSITE_DATA.get(i).setAllText(content);
                GlobalVars.LIST_WEBSITE_DATA.get(i).setSource(source);
                GlobalVars.LIST_WEBSITE_DATA.get(i).setPostDate(post_date);
            }catch(Exception ex){
                GlobalVars.ERRORS = 1;
                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                    GlobalVars.LOG_ADDIX.formatStringError("Error JSoup read page " + urlItem, ex.toString()));     
            }
        }   
        return true;
    }

    /**
     * write data (only 1 keyword) to output excel file
     * @param fileName
     * @param sheetName
     * @param arrData 
     */
    public void writeToOutputExcelFile(String fileName, String sheetName, ArrayList<WebsiteData> arrData, String webName, String keyword){
        try{
            //Get day of week and format them
            SimpleDateFormat strFormatDate= new SimpleDateFormat("yyyyMMdd");
            Date presentDate = new Date(System.currentTimeMillis());

            String strPresentDate = strFormatDate.format(presentDate).toString();

            DateTimeFormatter strPatternFormat = DateTimeFormatter.ofPattern("yyyyMMdd");
            LocalDate myFormatDate = LocalDate.parse(strPresentDate, strPatternFormat);

            String strDayOfWeek = myFormatDate.with(TemporalAdjusters.next(DayOfWeek.FRIDAY)).toString();
            strDayOfWeek = strDayOfWeek.replace("-", "");
            System.out.println("\t-Writing data to file " + fileName);
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                    GlobalVars.LOG_ADDIX.formatStringContent("Writing data to output file " + fileName));
            
            //Open excel file
            File outputExcelFile = new File(fileName);            
            FileInputStream myFileInputStream = new FileInputStream(outputExcelFile);
            
            // we create an XSSF Workbook object for our XLSX Excel File
            XSSFWorkbook workbook = new XSSFWorkbook(myFileInputStream);
            // choose sheet name
            XSSFSheet sheet = workbook.getSheet(sheetName);
            //Get last row of file
            int lastRow = sheet.getLastRowNum();
            String linkUrl = "";     
            
            if(arrData.isEmpty()){
                WebsiteElement elementWeb = GlobalVars.MAP_WEBSITE_ELEMENTS.getWebsiteElement(webName);
                
                for(int i = 0; i < GlobalVars.NUMBER_LIMIT_TOP_URL; i++){
                    lastRow ++;                
                    Row row = sheet.createRow(lastRow);
                    row.createCell(0).setCellValue(i + 1);
                    row.createCell(1).setCellValue(webName);
                    row.createCell(2).setCellValue(elementWeb.getDomain());
                    row.createCell(3).setCellValue(keyword);
                }
            }else{
                for(int i = 0; i < arrData.size(); i++){                
                    lastRow ++;                
                    Row row = sheet.createRow(lastRow);
                    row.createCell(0).setCellValue(i + 1);
                    row.createCell(1).setCellValue(arrData.get(i).getWebName());
                    row.createCell(2).setCellValue(arrData.get(i).getUrlListTab());
                    row.createCell(3).setCellValue(arrData.get(i).getKeyword());

                    String strUrlArticle = arrData.get(i).getUrlArticle();
                    row.createCell(4).setCellValue(strUrlArticle);

                    row.createCell(5).setCellValue(arrData.get(i).getTitle());
                    row.createCell(6).setCellValue(arrData.get(i).getPostDate());
                    row.createCell(7).setCellValue(arrData.get(i).getSource());
                    row.createCell(8).setCellValue(arrData.get(i).getAllText());

                    if(strUrlArticle.contains("https://")){
                        strUrlArticle = strUrlArticle.replace("https://", "");
                    }else if(strUrlArticle.contains("http://")){
                        strUrlArticle.replace("http://", "");
                    }
                    linkUrl = "https://s3.console.aws.amazon.com/s3/buckets/avatar-rpa-products/"+ strDayOfWeek + "HTTrack/"+strUrlArticle+"index.html";
                    row.createCell(9).setCellValue(linkUrl);

    //                if(GlobalVars.DEBUG == 1){
    //                    System.out.println((i + 1) + "\n" + arrData.get(i).getWebName() 
    //                        + "\n" + arrData.get(i).getUrlListTab()
    //                        + "\n" + arrData.get(i).getKeyword()
    //                        + "\n" + arrData.get(i).getUrlArticle()
    //                        + "\n" + arrData.get(i).getTitle()
    //                        + "\n" + arrData.get(i).getPostDate()
    //                        + "\n" + arrData.get(i).getSource()
    //                        + "\n" + arrData.get(i).getAllText()
    //                        + "\n" + linkUrl
    //                    );  
    //                }
                }
                for(int i = arrData.size(); i < GlobalVars.NUMBER_LIMIT_TOP_URL; i++){
                    lastRow ++;
                    Row row = sheet.createRow(lastRow);
                    row.createCell(0).setCellValue(i + 1);
                    row.createCell(1).setCellValue(arrData.get(0).getWebName());
                    row.createCell(2).setCellValue(arrData.get(0).getUrlListTab());
                    row.createCell(3).setCellValue(arrData.get(0).getKeyword());
    //                if(GlobalVars.DEBUG == 1){
    //                    System.out.println((i + 1) + "\n" + arrData.get(i).getWebName() 
    //                        + "\n" + arrData.get(0).getUrlListTab()
    //                        + "\n" + arrData.get(0).getKeyword()                        
    //                    );
    //                }
                }
            }

            myFileInputStream.close();
            FileOutputStream output_file =new FileOutputStream(new File(fileName));
            //write changes
            workbook.write(output_file);
            output_file.close();
            
        }catch(Exception ex){
            GlobalVars.ERRORS = 1;
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                    GlobalVars.LOG_ADDIX.formatStringError("Error Write Output To Excel File " + fileName, ex.toString()));   
        }
    }
    
    public void writeToEndStorageExcelFile(String fileName, String sheetName, ArrayList<WebsiteData> arrData){
        try{
            //Open excel file
            File outputExcelFile = new File(fileName);
            FileInputStream myFileInputStream = new FileInputStream(outputExcelFile);
            
            // we create an XSSF Workbook object for our XLSX Excel File
            XSSFWorkbook workbook = new XSSFWorkbook(myFileInputStream);
            // choose sheet name
            XSSFSheet sheet = workbook.getSheet(sheetName);
            //Get last row of file
            int lastRow = sheet.getLastRowNum();            
            for(int i = 0; i < arrData.size(); i++){ 
                lastRow ++;                
                Row row = sheet.createRow(lastRow);                
                row.createCell(0).setCellValue(arrData.get(i).getUrlArticle());
            }
            
            myFileInputStream.close();
            FileOutputStream output_file =new FileOutputStream(new File(fileName));
            //write changes
            workbook.write(output_file);            
            output_file.close();            
        }catch(Exception ex){
            GlobalVars.ERRORS = 1;
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                    GlobalVars.LOG_ADDIX.formatStringError("Error Write Output To Excel File " + fileName, ex.toString()));   
        }
            
    }
}
