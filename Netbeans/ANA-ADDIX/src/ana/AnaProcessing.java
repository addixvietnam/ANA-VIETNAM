package ana;

import ana.controller.WebsiteData_Controller;
import ana.model.Project;
import ana.model.Website;
import ana.model.WebsiteElement;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import vn.addix.utils.AddixConfig;
import vn.addix.utils.AddixExcel;
import vn.addix.utils.Utilities;

public class AnaProcessing {
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
            String startInitAnaTime = Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE);
            
            if(GlobalVars.DEBUG == 1){
                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringContent("Starting init ana Project. \nReading from input excel file"));
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
            String[][] inputData = libExcelFile.readExcelFile("website", true, null);
            
//            printDataInputTest(inputData);

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
                GlobalVars.MAP_WEBSITE.addWebsite(inputData[i][0], new Website(i+ 1, inputData[i][0], inputData[i][16], 1));
            }
            
            if(GlobalVars.DEBUG == 1){
                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringContent("Adding data to element of website (use hashmap)"));
            }                        
            //Add element website data to map            
            for(int i = 0; i < inputData.length; i++){  
                int loadWaitTime = Integer.parseInt(inputData[i][6].split("s")[0]);
                int saveWaitTime = Integer.parseInt(inputData[i][7].split("s")[0]);
//                System.out.println(inputData[i][0] + " => " + " .LoadWaitTime: " + loadWaitTime + " .SaveWaitTime: " + saveWaitTime);
                GlobalVars.MAP_WEBSITE_ELEMENTS.addWebElement(inputData[i][0].trim(),                         
                        new WebsiteElement(
                                i + 1, i + 1, inputData[i][0], 
                                Integer.parseInt(inputData[i][1]), Integer.parseInt(inputData[i][2]), Integer.parseInt(inputData[i][3]), Integer.parseInt(inputData[i][4]),
                                Integer.parseInt(inputData[i][5]), loadWaitTime, saveWaitTime,
                                inputData[i][8], inputData[i][9], inputData[i][10], inputData[i][11],
                                inputData[i][12], inputData[i][13], Integer.parseInt(inputData[i][14]),
                                inputData[i][15], inputData[i][16], inputData[i][17],
                                Integer.parseInt(inputData[i][18]), inputData[i][19], Integer.parseInt(inputData[i][20]),
                                inputData[i][22], inputData[i][23]));
            }
            if(GlobalVars.DEBUG == 1){
                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringContent("Adding data to keyword of website (use array)"));
            }
            String[][] keywordData = libExcelFile.readExcelFile("keyword", true, null);
            GlobalVars.ARRAY_KEYWORD = new String[keywordData.length];
            for(int i = 0; i < keywordData.length; i++){
                GlobalVars.ARRAY_KEYWORD[i] = keywordData[i][0];
            }            
            String endInitAnaTime = Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE);
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringContent("Finished init ana Project (read input.xlsx). \nTotal time " + 
                                Utilities.getTotalWaitTime(startInitAnaTime, endInitAnaTime, "yyyy/MM/dd HH:mm:ss z"))); 

            
        } catch (Exception ex) {
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                    GlobalVars.LOG_ADDIX.formatStringError("Read Input Excel Error (" + inputExcelFile + ")", ex.toString()));
        }        
    }
    
    /**
     * Main of program
     */
    public void anaProject() throws IOException{
        GlobalVars.PATH_INPUT_FILE = GlobalVars.WORK_DIRECTORY + "/input/input.xlsx";        
        //Read input variables from input.xlsx file
        initAnaProject(GlobalVars.PATH_INPUT_FILE);
        AnaGettingData anaGettingData = new AnaGettingData();
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");       
        
        String startProjectTime = Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE);
        
        for (Map.Entry<String, Website> presentWebsite : GlobalVars.MAP_WEBSITE.getWebsites().entrySet()) {            
            //Read all old url item of 1 website from data_storage
            GlobalVars.MAP_OLD_DATA = new HashMap<>();//free & reallocate memory for old data          
            String beginWebsiteTime = Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE);
            String webName = presentWebsite.getKey().trim();
            String strOutExcelFile = "";      
            
            if(GlobalVars.MAP_WEBSITE_ELEMENTS.getWebsiteElement(webName).getCrawlSwitch() == 1){
                System.out.println("Processing data with website " + webName);
                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringContent("Processing data with website " + webName));

                //Copy from input excel template to output file
                try{
                    // Get date time follow format yyyyMMdd                
                    Date presentDate = new Date();
                    String outputExcelNameFile = GlobalVars.WORK_DIRECTORY + "/output/data_page_source/HTML/" + webName.toLowerCase() +
                            "/" + dateFormat.format(presentDate) + "_" + webName + ".xlsx";
                    GlobalVars.LIB_COMMON.deleteFile(outputExcelNameFile);
                    //New output excel file
                    strOutExcelFile = GlobalVars.LIB_COMMON.coppyExcelFile(GlobalVars.WORK_DIRECTORY + "/input/output.xlsx", 
                                                GlobalVars.WORK_DIRECTORY + "/output/data_page_source/HTML/" + webName.toLowerCase(), 
                                                dateFormat.format(presentDate) + "_" + webName + ".xlsx" );
                }catch(Exception ex){
                    GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                            GlobalVars.LOG_ADDIX.formatStringError("Copy file " + webName, ex.toString()));   
                }
                if(GlobalVars.DEBUG == 1){
                    System.out.println("\tCopied from " + GlobalVars.WORK_DIRECTORY + "/input/output.xlsx" + 
                        "\n\t         to " + strOutExcelFile);
                }            

                //Read data from website
                String webSearch = GlobalVars.MAP_WEBSITE_ELEMENTS.getWebsiteElement(webName).getUrl();
//                if(GlobalVars.MAP_WEBSITE_ELEMENTS.getWebsiteElement(webName).getCrawlMethod() == 2){
//                    GlobalVars.LIB_SELENIUM.setChromeDriver();
//                }
                
//                String webSearch = presentWebsite.getValue().getUrl().trim();     
                for(int i = 0; i < GlobalVars.ARRAY_KEYWORD.length; i++){
                    if(GlobalVars.ARRAY_KEYWORD[i].trim().equals("AI") && webName.trim().equals("gigazine")){
                        //Skip this action
                    }else{
                        String strOutLog = "";
                        String beginKeywordTime = Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE);
                        strOutLog = (i+1) + ". Keyword " + GlobalVars.ARRAY_KEYWORD[i].trim();     

                        System.out.println(strOutLog);
                        System.out.println("\tReading old data of keyword " + GlobalVars.ARRAY_KEYWORD[i].trim());
                        GlobalVars.ERRORS = 0;
                        anaGettingData.readOldData1Keyword(GlobalVars.ARRAY_KEYWORD[i].trim(), webName);  

                        GlobalVars.MAP_ITEM_URL = new HashMap<>();
                        GlobalVars.LIST_WEBSITE_DATA = new ArrayList<>();
                        if(GlobalVars.ERRORS == 0){
                            if(GlobalVars.DEBUG == 1){
                                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                                        GlobalVars.LOG_ADDIX.formatStringContent("\t-Reading new url which doesn't exist in data_storage."));  
                            }
                            System.out.println("\t-Reading new url which doesn't exist in data_storage.");
                            if(GlobalVars.MAP_WEBSITE_ELEMENTS.getWebsiteElement(webName).getCrawlMethod() == 1){//ANA 1   
                                //read new url item from website with a keyword. Then save to arrItemUrl, arrItemTitle                    
                                anaGettingData.readDataKeywordWebsite(presentWebsite.getKey().trim(), GlobalVars.ARRAY_KEYWORD[i].trim(), webSearch, false);
                            }else{//ANA-2      
                                GlobalVars.LIB_SELENIUM.setChromeDriver();
                                anaGettingData.readDataKeywordWebsite(presentWebsite.getKey().trim(), GlobalVars.ARRAY_KEYWORD[i].trim(), webSearch, true);                            
                                GlobalVars.LIB_SELENIUM.close();
                                
                            }
                        }
//                        if(GlobalVars.MAP_WEBSITE_ELEMENTS.getWebsiteElement(webName).getCrawlMethod() == 2){
//                            GlobalVars.LIB_SELENIUM.close();
//                        }                        

                        WebsiteData_Controller myOutputData = new WebsiteData_Controller();
                        if(GlobalVars.ERRORS == 0){
                            if(GlobalVars.DEBUG == 1){
                                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                                        GlobalVars.LOG_ADDIX.formatStringContent("\t-Reading and fill data of every item url"));  
                            }
                            System.out.println("\t-Reading and fill data of every item url");
                            //Fill data to LIST_WEBSITE_DATA                        
                            myOutputData.readDataWebsite();
                        }
                        if(GlobalVars.ERRORS == 0){
                            if(GlobalVars.DEBUG == 1){
                                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                                        GlobalVars.LOG_ADDIX.formatStringContent("\t-Writing data to output excel file."));  
                            }
                            System.out.println("\t-Writing data to output excel file.");
                            //Write data (only 1 keyword) to output excel file
                            myOutputData.writeToOutputExcelFile(strOutExcelFile, "Sheet1", GlobalVars.LIST_WEBSITE_DATA, webName, GlobalVars.ARRAY_KEYWORD[i].trim());

                        }
                        if(GlobalVars.ERRORS == 0){
                            if(GlobalVars.DEBUG == 1){
                                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                                        GlobalVars.LOG_ADDIX.formatStringContent("\t-Writing data to end of data_storage."));  
                            }
                            System.out.println("\t-Writing data to end of data_storage");
                            myOutputData.writeToEndStorageExcelFile(GlobalVars.WORK_DIRECTORY + "/output/data_storge/" + webName.toLowerCase().trim() + ".xlsx", 
                                    GlobalVars.ARRAY_KEYWORD[i].trim(), GlobalVars.LIST_WEBSITE_DATA);
                        }

                        //Write to end of file in data storage
                        myOutputData.writeToEndStorageExcelFile(GlobalVars.WORK_DIRECTORY + "/output/data_storge/" + webName.toLowerCase() + ".xlsx", 
                                GlobalVars.ARRAY_KEYWORD[i].trim().toUpperCase(), GlobalVars.LIST_WEBSITE_DATA);

                        String endKeywordTime = Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE);
                        GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                            GlobalVars.LOG_ADDIX.formatStringContent((i+1) + ". Keyword " + GlobalVars.ARRAY_KEYWORD[i].trim() + ".\nTotal time " + 
                                    Utilities.getTotalWaitTime(beginKeywordTime, endKeywordTime, "yyyy/MM/dd HH:mm:ss z")));
                    }                    
                }            

                String endWebsiteTime = Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE);
                GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                            GlobalVars.LOG_ADDIX.formatStringContent("Finished getting data from " + webName + ". \nTotal time " + 
                                    Utilities.getTotalWaitTime(beginWebsiteTime, endWebsiteTime, "yyyy/MM/dd HH:mm:ss z")));                 
                
            }                  
        }
        String endProjectTime = Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE);
        GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                        GlobalVars.LOG_ADDIX.formatStringContent("Finished ANA Project. \nTotal time " + 
                                Utilities.getTotalWaitTime(startProjectTime, endProjectTime, "yyyy/MM/dd HH:mm:ss z")));  
        System.out.println("Finished program");
        
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
            System.out.println("6. LoadWaitTime: " + dataInput[i][6]);  
            System.out.println("7. SaveWaitTime: " + dataInput[i][7]);  
            System.out.println("8. URL: " + dataInput[i][8]);   
            System.out.println("9. Class_Href: " + dataInput[i][9]);           
            System.out.println("10. Class_Title: " + dataInput[i][10]);           
            System.out.println("11. Class_PostDate: " + dataInput[i][11]);           
            System.out.println("12. Class_Source: " + dataInput[i][12]);           
            System.out.println("13. Class_Content: " + dataInput[i][13]);           
            System.out.println("14. click_Search: " + dataInput[i][14]);           
            System.out.println("15. class_Next_page: " + dataInput[i][15]);           
            System.out.println("16. Domain: " + dataInput[i][16]);   
            System.out.println("17. replace_nxt_p: " + dataInput[i][17]);           
            System.out.println("18. case_display: " + dataInput[i][18]);           
            System.out.println("19. class_tr_dl: " + dataInput[i][19]);           
            System.out.println("20. click_sort: " + dataInput[i][20]);           
            System.out.println("21. class_gsc_cursor_page: " + dataInput[i][21]);     
            System.out.println("22. item next page: " + dataInput[i][22]);
            System.out.println("23. xpath next page: " + dataInput[i][23]);
            System.out.println("24. 備考(1ページ目の表示アイテム数): " + dataInput[i][24]);           
        
            
        }
    }     
}
