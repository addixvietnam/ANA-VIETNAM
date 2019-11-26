/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ana.controller;


import java.nio.file.Paths;
import vn.addix.utils.AddixFile;

/**
 *
 * Some popular functions
 */
public class CommonFunctions extends AddixFile{
    private String currentPath = "";    

    /**
     * Constructor auto get current path
     * @throws Exception 
     */
    public CommonFunctions() throws Exception {
        try{
            this.currentPath = Paths.get("").toAbsolutePath().toString();                   
        }catch(Exception ex){
            throw ex;
        }        
    }
    
    /**
     * Get total time waiting to execute the program
     * @param beginTime
     * @param endTime
     * @return 
     */
    public static String getTotalWaitTime(String beginTime, String endTime){
        String strTotalWaitTime = "";
        
        return strTotalWaitTime;
    }
    
    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }
    
    
}
