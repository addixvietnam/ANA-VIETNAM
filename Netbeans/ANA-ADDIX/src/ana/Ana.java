package ana;

import ana.controller.CommonFunctions;
import ana.controller.WebsiteData_Controller;
import ana.model.Project;
import ana.model.Website;
import ana.model.WebsiteData;
import ana.model.WebsiteElement;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
//import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import vn.addix.utils.AddixConfig;
import vn.addix.utils.AddixExcel;
import vn.addix.utils.Log;
import vn.addix.utils.Utilities;

public class Ana {
    /**
     * Main function of program
     * @param args 
     */
    public static void main(String[] args) throws Exception{             
        try{            
            //Get current folder path
            GlobalVars.LIB_COMMON = new CommonFunctions();               
            Ana myProgram = new Ana();
            //Read some config variables from Config.md file
            myProgram.initProgram();
            //Init variables for Log file
            GlobalVars.LOG_ADDIX = new Log(GlobalVars.LOG_WIDTH_SIZE, GlobalVars.LOG_NUMBER_SPACE_ERROR, 
                    GlobalVars.LOG_CHARACTER_HEADER, GlobalVars.LOG_CHARACTER_ERROR);               
            //Check present log file to backup and create new log file
            GlobalVars.LOG_ADDIX.createLogFile(GlobalVars.LOG_FOLDER_NAME, GlobalVars.LOG_NUMBER_LIMIT_FILE);        
            //Write to header log file
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                    GlobalVars.LOG_ADDIX.formatStringHeaderAndFooter(true));
            //Write working directory to log file
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                    GlobalVars.LOG_ADDIX.formatStringContent("WORKING DIRECTORY:\n\t" + GlobalVars.WORK_DIRECTORY));
            //Write main Program
            myProgram.anaProject();        
            //Write to footer log file
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, GlobalVars.LOG_ADDIX.formatStringHeaderAndFooter(false));
        }catch(Exception ex){
            throw ex;
        }    
    }
    /**
     * Main of program
     */
    public void anaProject(){
        GlobalVars.PATH_INPUT_FILE = GlobalVars.WORK_DIRECTORY + "/input/input.xlsx";        
        //Read input variables from input.xlsx file
        initAnaProject(GlobalVars.PATH_INPUT_FILE);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");        
        for (Map.Entry<String, Website> presentWebsite : GlobalVars.MAP_WEBSITE.getWebsites().entrySet()) {
            //Read all old url item of 1 website from data_storage
            GlobalVars.MAP_OLD_DATA = new HashMap<>();//free & reallocate memory for old data          
            String startProjectTime = Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE);
            String webName = presentWebsite.getKey().trim();
            String strOutExcelFile = "";
            System.out.println("Processing data with website " + webName);

            //Copy from input excel template to output file
            try{
                // Get date time follow format yyyyMMdd                
                Date presentDate = new Date();
                //New output excel file
                strOutExcelFile = GlobalVars.LIB_COMMON.coppyExcelFile(GlobalVars.WORK_DIRECTORY + "/input/output.xlsx", 
                                            GlobalVars.WORK_DIRECTORY + "/output/data_page_source/HTML/" + webName.toLowerCase(), 
                                            dateFormat.format(presentDate) + "_" + webName + ".xlsx" );
            }catch(Exception ex){
                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringError("Copy file " + webName, ex.toString()));   
            }
            
            System.out.println("\tCopied from " + GlobalVars.WORK_DIRECTORY + "/input/output.xlsx" + 
                    "\n\t         to " + strOutExcelFile);
            
            if(GlobalVars.MAP_WEBSITE_ELEMENTS.getWebsiteElement(webName).getType() == 1){//ANA 1
                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringContent("Starting ANA-1 Project. Start time: " + startProjectTime));                
                GlobalVars.MAP_PROJECTS.getProject("ANA-1").setCreateDate(startProjectTime);
                //Read all old data from excel file in data_storage
//                readOldData(GlobalVars.LOG_ADDIX, webName);
                //Read data from website
                String webSearch = presentWebsite.getValue().getUrl().trim();
                
                for(int i = 0; i < GlobalVars.ARRAY_KEYWORD.length; i++){
                    System.out.println(i + ". Keyword " + GlobalVars.ARRAY_KEYWORD[i].trim());
                    System.out.println("\tReading old data of keyword " + GlobalVars.ARRAY_KEYWORD[i].trim());
                    readOldData1Keyword(GlobalVars.ARRAY_KEYWORD[i].trim(), webName);                    
                    GlobalVars.MAP_ITEM_URL = new HashMap<>();
                    GlobalVars.LIST_WEBSITE_DATA = new ArrayList<>();
                    System.out.println("\t-Reading new url which doesn't exist in data_storage.");
                    //read new url item from website with a keyword. Then save to arrItemUrl, arrItemTitle                    
                    readDataKeywordWebsite(presentWebsite.getKey().trim(), GlobalVars.ARRAY_KEYWORD[i].trim(), webSearch);
                    System.out.println("\t-Reading full data of every item url");
                    //Fill data to LIST_WEBSITE_DATA
                    WebsiteData_Controller myOutputData = new WebsiteData_Controller();
                    myOutputData.readDataWebsite();
                    System.out.println("\t-Writing data to excel file.");
                    //Write data (only 1 keyword) to output excel file
                    myOutputData.writeToOutputExcelFile(strOutExcelFile, "Sheet1", GlobalVars.LIST_WEBSITE_DATA, webName, GlobalVars.ARRAY_KEYWORD[i].trim());
                    System.out.println("\t-Writing data to end of data_storage");
                    //Write to end of file in data storage
                    myOutputData.writeToEndStorageExcelFile(GlobalVars.WORK_DIRECTORY + "/output/data_storge/" + webName.toLowerCase() + ".xlsx", 
                            GlobalVars.ARRAY_KEYWORD[i].trim().toUpperCase(), GlobalVars.LIST_WEBSITE_DATA);
                }
            }else{//ANA 2
//                myLog.writeLog(GlobalVars.LOG_FILE_NAME, myLog.formatStringContent("Starting ANA-2 Project. Start time: " + startProjectTime));
//                GlobalVars.projectMap.get("ANA-2").setCreateDate(startProjectTime);
//                readOldData(myLog, webName);
                
            }            
        }
        System.out.println("Finished program");
    }
    
    public void readOldData1Keyword(String keyword, String webName){        
        
        GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, GlobalVars.LOG_ADDIX.formatStringContent("Reading all old data with website is " + 
                        webName + ". Start time: " + Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE)));
        String oldFileName = GlobalVars.WORK_DIRECTORY + "\\output\\data_storge\\" + webName + ".xlsx";
        
        GlobalVars.MAP_OLD_DATA = new HashMap<>();
        String[][] oldUrlItem = null;

        if(GlobalVars.DEBUG == 1){
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, GlobalVars.LOG_ADDIX.formatStringContent("Reading old data with keyword is " + 
                keyword.trim() + ". Start time: " + Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE)));
        }

        try{
            AddixExcel libExcelFile = new AddixExcel(oldFileName) {
                @Override
                public boolean writeExcelFile(String string, String[][] strings) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public boolean writeExcelFile(String string, int i, int i1, String[][] strings) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
            oldUrlItem = libExcelFile.readExcelFile(keyword, false, new int[]{1});

        }catch(Exception ex){
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                GlobalVars.LOG_ADDIX.formatStringError("Read Input Keyword (" + keyword.trim() + ") Excel Error (" + oldFileName + ")", ex.toString()));
        }
        if(GlobalVars.DEBUG == 1){
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, GlobalVars.LOG_ADDIX.formatStringContent("End reading old data with keyword is " + 
                keyword.trim() + ". End time: " + Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE)));                    
        }

        for(int row = 0; row < oldUrlItem.length; row++){
            Map<String, Boolean> objValue = new HashMap<>();
            objValue.put(oldUrlItem[row][0].trim(), Boolean.TRUE);
            GlobalVars.MAP_OLD_DATA.put(keyword.trim(), objValue);
        }
        
            
        GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, GlobalVars.LOG_ADDIX.formatStringContent("End reading all old data with website is " + 
                        webName + ". End time: " + Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE)));          
    }
    /**
     * Read configure from Config.md file
     */
    public void initProgram(){
        AddixConfig myConfig = new AddixConfig();        
        System.out.println("My Config file: " + GlobalVars.LIB_COMMON.getCurrentPath() + "\\Config.md");
        String [] arrConfig = myConfig.readConfig(GlobalVars.LIB_COMMON.getCurrentPath() + "\\Config.md");
        GlobalVars.setGlobalVariables(arrConfig);
    }
    
    /**
     * Read data from input file then save them to model of program
     * @param inputExcelFile     *  
     */
    public void initAnaProject(String inputExcelFile){     
        try {
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringContent("Starting init ana Project"));
            
            if(GlobalVars.DEBUG == 1){
                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringContent("Reading from input excel file"));
            }
            AddixExcel libExcelFile = new AddixExcel(inputExcelFile) {
                @Override
                public boolean writeExcelFile(String string, String[][] strings) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
                
                @Override
                public boolean writeExcelFile(String string, int i, int i1, String[][] strings) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
            String[][] inputData = libExcelFile.readExcelFile("URL", true, null);

            if(GlobalVars.DEBUG == 1){
                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringContent("Finished reading input excel file.\nAdding data to project (use hashmap)"));
            }
            //Add Project data to map           
            GlobalVars.MAP_PROJECTS.addProject("ANA-1", new Project(1, "ANA-1", Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE), 1));
            GlobalVars.MAP_PROJECTS.addProject("ANA-2", new Project(2, "ANA-2", Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE), 1));

            if(GlobalVars.DEBUG == 1){
                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringContent("Adding data to website object (use hashmap)"));
            }            
            //Add Website data to map            
            for(int i = 0; i < inputData.length; i++){                 
                GlobalVars.MAP_WEBSITE.addWebsite(inputData[i][0], new Website(i+ 1, inputData[i][0], inputData[i][6], 1));
            }
            
            if(GlobalVars.DEBUG == 1){
                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringContent("Adding data to element of website (use hashmap)"));
            }                        
            //Add element website data to map            
            for(int i = 0; i < inputData.length; i++){  
                GlobalVars.MAP_WEBSITE_ELEMENTS.addWebElement(inputData[i][0].trim(), 
                        new WebsiteElement(
                                i + 1, i + 1, inputData[i][7], inputData[i][8], 
                                inputData[i][9], inputData[i][10], inputData[i][11], 
                                Integer.parseInt(inputData[i][12]), 
                                inputData[i][13], inputData[i][14], inputData[i][15],
                                Integer.parseInt(inputData[i][16]), 
                                Integer.parseInt(inputData[i][18]), 
                                inputData[i][19], 
                                Integer.parseInt(inputData[i][3]),
                                Integer.parseInt(inputData[i][4]),
                                Integer.parseInt(inputData[i][5]),
                                inputData[i][20]));
            }
            if(GlobalVars.DEBUG == 1){
                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringContent("Adding data to keyword of website (use array)"));
            }
            String[][] keywordData = libExcelFile.readExcelFile("keyword", true, null);
            GlobalVars.ARRAY_KEYWORD = new String[keywordData.length];
//            System.out.println("Keyword length: " + keywordData.length);
            for(int i = 0; i < keywordData.length; i++){
                GlobalVars.ARRAY_KEYWORD[i] = keywordData[i][0];
//                System.out.println(i + " => " + keywordData[i][0]);
            }            
            
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringContent("Finished init ana Project"));
        } catch (Exception ex) {
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                    GlobalVars.LOG_ADDIX.formatStringError("Read Input Excel Error (" + inputExcelFile + ")", ex.toString()));
        }        
    }
    /**
     * Update string url with page no and keyword with we want search. Then get top N child url item in webpage
     * @param webName 
     * @param keyword
     * @param urlSearch is a main url string. We will update with this string
     */
    public void readDataKeywordWebsite(String webName, String keyword, String urlSearch){
        GlobalVars.TOTAL_URL_ITEM = 1;
        GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                GlobalVars.LOG_ADDIX.formatStringContent("Reading top n keyword & checking exist item on old data. Website: " + 
                    webName + " .Keyword: " + keyword + ". Start time: " + Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE)));
        urlSearch = urlSearch.replace("(keyword)", keyword);                
        int pageNum = 1;

        //if website need to read 1 page
        int numPageToRead = GlobalVars.MAP_WEBSITE_ELEMENTS.getWebsiteElement(webName).getNumReadPage();

        GlobalVars.TOTAL_PAGE = 1;
        if(numPageToRead == 1){
            String myWeb = urlSearch.replace("(pagenum)", Integer.toString(pageNum));
            getTopNWebsiteItem(myWeb, webName, keyword);
        }else{//read many page
            GlobalVars.TOTAL_PAGE = 1;
            while(numPageToRead > 0){
                String myWeb = urlSearch.replace("(pagenum)", Integer.toString(pageNum));
                getTopNWebsiteItem(myWeb, webName, keyword);

                if(GlobalVars.TOTAL_PAGE == 1){
                    numPageToRead = 0;
                }else{
                    pageNum++;
                    numPageToRead--;
                }                
            }
        } 
    }
    /**
     * Read old data from excel and put data to temp hash map (oldDataMap)
     * @param myLog is a Log object
     * @param webName is name of excel file which storage old data
     */
    public void readOldData(Log myLog, String webName){
        myLog.writeLog(GlobalVars.LOG_FILE_NAME, myLog.formatStringContent("Reading all old data with website is " + 
                        webName + ". Start time: " + Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE)));
            for(int i = 0; i < GlobalVars.ARRAY_KEYWORD.length; i++){                
                String oldFileName = GlobalVars.WORK_DIRECTORY + "\\output\\data_storge\\" + webName + ".xlsx";
                String[][] oldUrlItem = null;
                
                if(GlobalVars.DEBUG == 1){
                    myLog.writeLog(GlobalVars.LOG_FILE_NAME, myLog.formatStringContent("Reading old data with keyword is " + 
                        GlobalVars.ARRAY_KEYWORD[i].trim() + ". Start time: " + Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE)));
                }

                try{
                    AddixExcel libExcelFile = new AddixExcel(oldFileName) {
                        @Override
                        public boolean writeExcelFile(String string, String[][] strings) {
                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }
                        
                        @Override
                        public boolean writeExcelFile(String string, int i, int i1, String[][] strings) {
                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }
                    };
                    oldUrlItem = libExcelFile.readExcelFile(GlobalVars.ARRAY_KEYWORD[i].trim(), false, new int[]{1});
                    
                }catch(Exception ex){
                    myLog.writeLog(GlobalVars.LOG_FILE_NAME, 
                        myLog.formatStringError("Read Input Keyword (" + GlobalVars.ARRAY_KEYWORD[i].trim() + ") Excel Error (" + oldFileName + ")", ex.toString()));
                }
                if(GlobalVars.DEBUG == 1){
                    myLog.writeLog(GlobalVars.LOG_FILE_NAME, myLog.formatStringContent("End reading old data with keyword is " + 
                        GlobalVars.ARRAY_KEYWORD[i].trim() + ". End time: " + Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE)));                    
                }

                for(int row = 0; row < oldUrlItem.length; row++){
                    Map<String, Boolean> objValue = new HashMap<>();
                    objValue.put(oldUrlItem[row][0].trim(), Boolean.TRUE);
                    GlobalVars.MAP_OLD_DATA.put(GlobalVars.ARRAY_KEYWORD[i].trim(), objValue);
                }
            }            
            myLog.writeLog(GlobalVars.LOG_FILE_NAME, myLog.formatStringContent("End reading all old data with website is " + 
                        webName + ". End time: " + Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE)));  
    }
    /**
     * Read link N child item url of webpage, then save them to LIST_WEBSITE_DATA
     * @param childUrl
     * @param webName
     * @param keyword 
     */
    public void getTopNWebsiteItem(String childUrl, String webName, String keyword){
        try{ 
//            GlobalVars.MAP_ITEM_URL = new HashMap<>();
//            GlobalVars.LIST_WEBSITE_DATA = new ArrayList<>();
            
            Document doc = Jsoup.connect(childUrl).get();
            String charset = doc.charset().toString();
            
            if(GlobalVars.DEBUG == 1){
                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringContent("Reading " + childUrl + "\nCharset: " + charset));
            }
            WebsiteElement webElement = GlobalVars.MAP_WEBSITE_ELEMENTS.getWebsiteElement(webName);
            String[] arrHref = webElement.getHref().trim().split(",");
            String[] arrTitle = webElement.getTitle().trim().split(",");
            String pageElement = webElement.getItemPageElement().trim();
            if(pageElement != null){
                Elements pageItem = doc.select(pageElement);                
                if(pageItem.size() > 0){
                    GlobalVars.TOTAL_PAGE = pageItem.size();
                }
            }            
            for(int iter = 0; iter < arrHref.length; iter++){                
                Elements hrefs = doc.select(arrHref[iter]);
                Elements titles = doc.select(arrTitle[iter]);
                if(titles.size() < hrefs.size()){
                    titles = doc.select(arrHref[iter]);
                }
                //Check list item link & item title
                for(int i = 0; i < hrefs.size(); i++){
                    String strUrl = "";
                    String strTitle = titles.get(i).text().trim();
                    if(!hrefs.get(i).attr("href").contains(webElement.getDomain().trim())){
                        strUrl = webElement.getDomain().trim() + hrefs.get(i).attr("href");
                    }else{
                        strUrl = hrefs.get(i).attr("href");
                    }
                    if(strUrl.contains("/msg/?ty")){
                        strUrl = strUrl.substring(0, strUrl.indexOf("/msg/?ty"));                        
                    }else if(strUrl.contains("/?ty")){
                        strUrl = strUrl.substring(0, strUrl.indexOf("/?ty"));                        
                    }else if(strUrl.contains("_message/")){
                        strUrl = strUrl.replace("_message/", "_detail/");                        
                    }else if(strUrl.contains("/-tab__pr/")){
                        strUrl = strUrl.replace("/-tab__pr/", "/-tab__jd/");                        
                    }else if(strUrl.contains("nx1_")){
                        strUrl = strUrl.replace("nx1_", "nx2_");
                        strUrl = strUrl.replace("&list_disp_no=1", "");
                        strUrl = strUrl.replace("n_ichiran_cst_n5_ttl", "ngen_tab-top_info");                        
                    }else{
                        strUrl = strUrl.replace(",", "");                        
                    } 
                    Map<String, Boolean> myOldData = GlobalVars.MAP_OLD_DATA.get(keyword);
//                    if((!GlobalVars.arrItemUrl.contains(strUrl)) && (countItemArray < GlobalVars.NUMBER_LIMIT_TOP_URL)){                        
                    if((!GlobalVars.MAP_ITEM_URL.containsKey(strUrl)) && (GlobalVars.TOTAL_URL_ITEM  <= GlobalVars.NUMBER_LIMIT_TOP_URL)){                        
                        if((!myOldData.containsKey(strUrl)) && (!strUrl.contains("/tag/"))){                        
                            GlobalVars.MAP_ITEM_URL.put(strUrl, Boolean.TRUE);
                            WebsiteData myModelData = new WebsiteData(webName, 
                                    GlobalVars.MAP_WEBSITE_ELEMENTS.getWebsiteElement(webName).getDomain().toString().trim(),
                                    keyword, strUrl, strTitle);
                            GlobalVars.LIST_WEBSITE_DATA.add(myModelData);
                        }
                        GlobalVars.TOTAL_URL_ITEM ++;
                    }
                }
            }            
        }catch(Exception ex){
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                GlobalVars.LOG_ADDIX.formatStringError("Error JSoup read page " + childUrl, ex.toString()));            
        }        
    }
    
    public void printDataInputTest(String[][] dataInput){
        for(int i = 0; i < 2; i ++){
            System.out.println("==========================");
            System.out.println("0. WebName: " + dataInput[i][0]);           
            System.out.println("1. CrawlSwitch: " + dataInput[i][1]);           
            System.out.println("2. SaveSwitch: " + dataInput[i][2]);           
            System.out.println("3. CrawlMethod: " + dataInput[i][3]);           
            System.out.println("4. SaveMethod: " + dataInput[i][4]);           
            System.out.println("5. PageNum: " + dataInput[i][5]);           
            System.out.println("6. URL: " + dataInput[i][6]);   
            System.out.println("7. Class_Href: " + dataInput[i][7]);           
            System.out.println("8. Class_Title: " + dataInput[i][8]);           
            System.out.println("9. Class_PostDate: " + dataInput[i][9]);           
            System.out.println("10. Class_Source: " + dataInput[i][10]);           
            System.out.println("11. Class_Content: " + dataInput[i][11]);           
            System.out.println("12. click_Search: " + dataInput[i][12]);           
            System.out.println("13. class_Next_page: " + dataInput[i][13]);           
            System.out.println("14. Domain: " + dataInput[i][14]);   
            System.out.println("15. replace_nxt_p: " + dataInput[i][15]);           
            System.out.println("16. case_display: " + dataInput[i][16]);           
            System.out.println("17. class_tr_dl: " + dataInput[i][17]);           
            System.out.println("18. click_sort: " + dataInput[i][18]);           
            System.out.println("19. class_gsc_cursor_page: " + dataInput[i][19]);           
            System.out.println("20. 備考(1ページ目の表示アイテム数): " + dataInput[i][20]);           
        
            
        }
    }
}
