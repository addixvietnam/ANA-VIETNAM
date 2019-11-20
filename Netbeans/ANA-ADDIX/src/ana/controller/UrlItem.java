package ana.controller;

import ana.GlobalVars;
import vn.addix.utils.Log;


public class UrlItem {
    public void getTopNItem(int numTopItem){
        
    }
    public boolean checkExistItem(String [] itemArr){
        return true;
    }
    public void getListExistItem(){
        
    }
    
    public void readExcelData(String fileName, String sheetName, int numCol, Log logFile){        
        try{
            //??? Hashmap or arraylist
        }catch(Exception ex){
            logFile.writeLog(GlobalVars.LOG_FILE_NAME, 
                    logFile.formatStringError("Read Data From Excel File " + fileName, ex.toString()));
        }
    }
}
