package ana;

import ana.controller.Automation;
import ana.controller.CommonFunctions;
import ana.controller.ProcessingUrl;
import vn.addix.utils.Log;

public class Ana {
    /**
     * Main function of program
     * @param args 
     */
    public static void main(String[] args) throws Exception{             
        try{            
            //Get current folder path
            GlobalVars.LIB_COMMON = new CommonFunctions();    
            GlobalVars.LIB_URL = new ProcessingUrl();            
            AnaProcessing prepareAna = new AnaProcessing();
            //Read some config variables from Config.md file
            prepareAna.initProgram();            
            GlobalVars.LIB_SELENIUM = new Automation(GlobalVars.WORK_DIRECTORY + "/chromedriver.exe");
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
            prepareAna.anaProject();        
            //Write to footer log file
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, GlobalVars.LOG_ADDIX.formatStringHeaderAndFooter(false));
        }catch(Exception ex){
            throw ex;
        }    
    }     
}
