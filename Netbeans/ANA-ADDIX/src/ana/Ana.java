package ana;

import ana.model.Project;
import ana.model.Website;
import ana.model.WebsiteElement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import vn.addix.utils.AddixConfig;
import vn.addix.utils.Log;
import vn.addix.utils.Utilities;

public class Ana {
    public void initProgram(){
        GlobalVars.WORK_DIRECTORY = "D:\\MyProjects\\Sources\\Netbeans\\ANA-ADDIX";
        AddixConfig myConfig = new AddixConfig();        
        String [] arrConfig = myConfig.readConfig(GlobalVars.WORK_DIRECTORY + "\\Config.md");
        GlobalVars.setGlobalVariables(arrConfig);
    }
 
    public static void main(String[] args) {     
        Ana myProgram = new Ana();
        myProgram.initProgram();
                
        Log myLog = new Log(GlobalVars.LOG_WIDTH_SIZE, GlobalVars.LOG_NUMBER_SPACE_ERROR, 
                GlobalVars.LOG_CHARACTER_HEADER, GlobalVars.LOG_CHARACTER_ERROR);        
        //Check present log file to backup and create new log file
        myLog.createLogFile(GlobalVars.LOG_FOLDER_NAME, GlobalVars.LOG_NUMBER_LIMIT_FILE);        
        //Write to header log file
        myLog.writeLog(GlobalVars.LOG_FILE_NAME, myLog.formatStringHeaderAndFooter(true));
        //Write main Program
        myProgram.anaProject(myLog);
        
        //Write to footer log file
        myLog.writeLog(GlobalVars.LOG_FILE_NAME, myLog.formatStringHeaderAndFooter(false));

        
    }
    
    public void initAnaProject(String inputExcelFile, Log myLog){     
        try {
            if(GlobalVars.DEBUG == 1){
                myLog.writeLog(GlobalVars.LOG_FILE_NAME, 
                        myLog.formatStringContent("Reading from input excel file"));
            }
            String[][] inputData = AddixExcel.readExcelFile(inputExcelFile, "URL", true, null);
            if(GlobalVars.DEBUG == 1){
                myLog.writeLog(GlobalVars.LOG_FILE_NAME, 
                        myLog.formatStringContent("Finished reading input excel file.\nAdding data to project (use hashmap)"));
            }
            //Add Project data to map           
            GlobalVars.projectMap.put("ANA-1", new Project(1, "ANA-1", Utilities.getDateTime(), 1));

            if(GlobalVars.DEBUG == 1){
                myLog.writeLog(GlobalVars.LOG_FILE_NAME, 
                        myLog.formatStringContent("Adding data to website object (use hashmap)"));
            }            
            //Add Website data to map            
            for(int i = 0; i < 24; i++){                
                GlobalVars.webMap.put(inputData[i][0], new Website(i+ 1, inputData[i][0], inputData[i][6], 1));
            }
            
            if(GlobalVars.DEBUG == 1){
                myLog.writeLog(GlobalVars.LOG_FILE_NAME, 
                        myLog.formatStringContent("Adding data to element of website (use hashmap)"));
            }
                        
            //Add element website data to map
            for(int i = 0; i < 24; i++){  
                GlobalVars.webElementMap.put(inputData[i][0], 
                        new WebsiteElement(
                                i + 1, i + 1, inputData[i][7], inputData[i][8], 
                                inputData[i][9], inputData[i][10], inputData[i][11], 
                                Integer.parseInt(inputData[i][12]), 
                                inputData[i][13], inputData[i][14], inputData[i][15],
                                Integer.parseInt(inputData[i][16]), 
                                Integer.parseInt(inputData[i][18]), 
                                inputData[i][19], 
                                Integer.parseInt(inputData[i][3])));
            }

            myLog.writeLog(GlobalVars.LOG_FILE_NAME, 
                        myLog.formatStringContent("Finished init ana Project"));
        } catch (Exception ex) {
            myLog.writeLog(GlobalVars.LOG_FILE_NAME, 
                    myLog.formatStringError("Read Input Excel Error (" + inputExcelFile + ")", ex.toString()));
        }        
    }
    public void anaProject(Log myLog){
        GlobalVars.PATH_INPUT_FILE = GlobalVars.WORK_DIRECTORY + "\\input\\input.xlsx";
        initAnaProject(GlobalVars.PATH_INPUT_FILE, myLog);
                
        for (Map.Entry<String, Website> entry : GlobalVars.webMap.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue().getName());
            
            
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
