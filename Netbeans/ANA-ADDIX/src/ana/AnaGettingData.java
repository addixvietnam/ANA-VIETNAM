package ana;

import ana.model.WebsiteData;
import ana.model.WebsiteElement;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import vn.addix.utils.AddixExcel;
import vn.addix.utils.Log;
import vn.addix.utils.Utilities;


public class AnaGettingData {
    
    /**
     * Read excel data file with 1 keyword
     * @param keyword
     * @param webName 
     */
    public void readOldData1Keyword(String keyword, String webName){        

        String oldFileName = GlobalVars.WORK_DIRECTORY + "\\output\\data_storage\\" + webName + ".xlsx";
        
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
//            Map<String, Boolean> objValue = new HashMap<>();
//            objValue.put(oldUrlItem[row][0].trim(), Boolean.TRUE);
//            GlobalVars.MAP_OLD_DATA.put(keyword.trim(), objValue);
            GlobalVars.MAP_OLD_DATA.put(oldUrlItem[row][0].trim(), Boolean.TRUE);
        }          
    }
 
    /**
     * Update string url with page no and keyword with we want search. Then get top N child url item in webpage
     * @param webName 
     * @param keyword
     * @param urlSearch is a main url string. We will update with this string
     */
    public void readDataKeywordWebsite(String webName, String keyword, String urlSearch, boolean isANA2){
        GlobalVars.TOTAL_URL_ITEM = 1;
        if(GlobalVars.DEBUG == 1){
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                GlobalVars.LOG_ADDIX.formatStringContent("Reading top n keyword & checking exist item on old data. Website: " + 
                    webName + " .Keyword: " + keyword + ". Start time: " + Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE)));
        }        
        urlSearch = urlSearch.replace("(keyword)", keyword);                
        int pageNum = 1;

        //if website need to read 1 page
        int numPageToRead = GlobalVars.MAP_WEBSITE_ELEMENTS.getWebsiteElement(webName).getNumReadPage();

        GlobalVars.TOTAL_PAGE = 1;
        if(numPageToRead == 1){
            String myWeb = urlSearch.replace("(pagenum)", Integer.toString(pageNum));
            if(isANA2){
                int countPage = 1;
                boolean isClickNextPage = false;
                getTopNWebsiteItemANA2(myWeb, webName, keyword, countPage, isClickNextPage);
            }else{//get online url
                getTopNWebsiteItem(myWeb, webName, keyword);
            }            
        }else{//read many page
            GlobalVars.TOTAL_PAGE = 1;
            if(isANA2){
                int countPage = 1;
                boolean isClickNextPage = false;
                if(!urlSearch.contains("(pagenum)")){
                    isClickNextPage = true;
                }
                while(numPageToRead > 0){
                    String myWeb = urlSearch.replace("(pagenum)", Integer.toString(pageNum));
                    System.out.println("Working with page " + pageNum);
                    getTopNWebsiteItemANA2(myWeb, webName, keyword, countPage, isClickNextPage);

                    if(GlobalVars.TOTAL_PAGE == 1){
                        numPageToRead = 0;
                    }else{
                        pageNum++;
                        numPageToRead--;
                    }             
                    countPage++;
                }
            }else{
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
    }
    /**
     * Read old data from excel and put data to temp hash map (oldDataMap)
     * @param myLog is a Log object
     * @param webName is name of excel file which storage old data
     */
    public void readOldData(Log myLog, String webName){
        if(GlobalVars.DEBUG == 1){
            myLog.writeLog(GlobalVars.LOG_FILE_NAME, myLog.formatStringContent("Reading all old data with website is " + 
                    webName + ". Start time: " + Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE)));            
        }

            for(int i = 0; i < GlobalVars.ARRAY_KEYWORD.length; i++){                
                String oldFileName = GlobalVars.WORK_DIRECTORY + "\\output\\data_storage\\" + webName + ".xlsx";
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
//                    Map<String, Boolean> objValue = new HashMap<>();
//                    objValue.put(oldUrlItem[row][0].trim(), Boolean.TRUE);
//                    GlobalVars.MAP_OLD_DATA.put(GlobalVars.ARRAY_KEYWORD[i].trim(), objValue);
                    GlobalVars.MAP_OLD_DATA.put(oldUrlItem[row][0].trim(), Boolean.TRUE);
                }
            }            
            myLog.writeLog(GlobalVars.LOG_FILE_NAME, myLog.formatStringContent("End reading all old data with website is " + 
                        webName + ". End time: " + Utilities.getDateTime(GlobalVars.LIB_DATETIME_FORMAT, GlobalVars.LIB_TIMEZONE)));  
    }
    
    public void getTopNWebsiteItemANA2(String childUrl, String webName, String keyword, int pageNo, boolean isClickNextPage){
        try{
            WebsiteElement webElement = GlobalVars.MAP_WEBSITE_ELEMENTS.getWebsiteElement(webName);
            TimeUnit timeSleep = TimeUnit.SECONDS;
            if((pageNo != 1) && (isClickNextPage == true)){
                //Click to next page                
                String strXPath = webElement.getxPathNextPage();
                int loadWaitTime = webElement.getLoadWaitTime();                
                String myXpath =  strXPath.substring(0, strXPath.lastIndexOf("[text()"));
                myXpath += "[text()='" + pageNo + "']";            
                GlobalVars.LIB_SELENIUM.goToPage(myXpath);
                timeSleep.sleep(2);
            }
            if(webName.trim().equals("gigazine")){
                childUrl = "https://gigazine.net/";
                GlobalVars.LIB_SELENIUM.connect(childUrl);    
                GlobalVars.LIB_SELENIUM.search("input", keyword);
            }else if(webName.trim().equals("newswitch")){
                GlobalVars.LIB_SELENIUM.connect(childUrl);
                //Check login by account of newswitch
                if(GlobalVars.LIB_SELENIUM.getSeleniumDriver().findElements(By.xpath("//*[@id=\"form-login\"]/div[1]/input")).size() != 0){
                    WebElement email = GlobalVars.LIB_SELENIUM.getSeleniumDriver().findElement(By.xpath("//*[@id=\"form-login\"]/div[1]/input"));
                    WebElement password = GlobalVars.LIB_SELENIUM.getSeleniumDriver().findElement(By.xpath("//*[@id=\"form-login\"]/div[2]/input"));
                    email.sendKeys("quoc.nguyen@addix.vn");
                    password.sendKeys("addix12");
                    GlobalVars.LIB_SELENIUM.getSeleniumDriver().findElement(By.xpath("//*[@id=\"form-login\"]/input[2]")).click();
                    timeSleep.sleep(3);
                }                
            }else if(webName.trim().equals("nikkan")){
                GlobalVars.LIB_SELENIUM.connect(childUrl);
                if(GlobalVars.LIB_SELENIUM.getSeleniumDriver().findElements(By.cssSelector("#header > ul > li.entry > a")).size() != 0){
                    GlobalVars.LIB_SELENIUM.getSeleniumDriver().findElement(By.cssSelector("#header > ul > li.entry > a")).click();
                    timeSleep.sleep(2);
                    //Check login by account of nikkan
                    if(GlobalVars.LIB_SELENIUM.getSeleniumDriver().findElements(By.xpath("//*[@id=\"UserLicenseLoginForm\"]")).size() != 0){
                        WebElement email = GlobalVars.LIB_SELENIUM.getSeleniumDriver().findElement(By.xpath("//*[@id=\"UserLicenseUsername\"]"));
                        WebElement password = GlobalVars.LIB_SELENIUM.getSeleniumDriver().findElement(By.xpath("//*[@id=\"UserLicensePassword\"]"));
                        email.sendKeys("quoc.nguyen@addix.vn");
                        password.sendKeys("addix12");
                        GlobalVars.LIB_SELENIUM.getSeleniumDriver().findElement(By.xpath("//*[@id=\"UserLicenseLoginForm\"]/div/input")).click();
                        timeSleep.sleep(3);
                    } 
                }
            }
            else{
                if(pageNo == 1){
                    GlobalVars.LIB_SELENIUM.connect(childUrl);   
                    if((webName.trim().equals("ascii")) || (webName.trim().equals("japancnet")) 
                        || (webName.trim().equals("monoist")) || (webName.trim().equals("wired"))){
                        timeSleep.sleep(3);
                        //Must click sort
                        GlobalVars.LIB_SELENIUM.click("gsc-option-selector");
                        GlobalVars.LIB_SELENIUM.click("gsc-option-menu");
                    }else if(webName.trim().equals("techcrunch")){
                        timeSleep.sleep(1); 
                        GlobalVars.LIB_SELENIUM.click("sort-selector");
                        timeSleep.sleep(2); 
                    }
                }    
                timeSleep.sleep(3);    
            }
//            timeSleep.sleep(5);     
                childUrl = GlobalVars.LIB_SELENIUM.getCurrentUrl();
                if(GlobalVars.DEBUG == 1){
                    GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                            GlobalVars.LOG_ADDIX.formatStringContent("Reading " + childUrl));
                }
            
                String[] arrHref = webElement.getHref().trim().split(",");
                String[] arrTitle = webElement.getTitle().trim().split(",");
                String pageElement = webElement.getItemPageElement().trim();
                if(pageElement != null){
                    List<WebElement> pageItem = GlobalVars.LIB_SELENIUM.getSeleniumDriver().findElements(By.cssSelector(pageElement));                              
                    if(pageItem.size() > 0){
                        GlobalVars.TOTAL_PAGE = pageItem.size();
                    }
                } 
                for(int iter = 0; iter < arrHref.length; iter++){                
                    List<WebElement> hrefs = GlobalVars.LIB_SELENIUM.getSeleniumDriver().findElements(By.cssSelector(arrHref[iter]));
                    List<WebElement> titles = GlobalVars.LIB_SELENIUM.getSeleniumDriver().findElements(By.cssSelector(arrTitle[iter]));
                    if(titles.size() < hrefs.size()){
                        titles = GlobalVars.LIB_SELENIUM.getSeleniumDriver().findElements(By.cssSelector(arrHref[iter]));
                    }                
//                    System.out.println("ELEMENT: " + arrHref[iter]);
                    //Check list item link & item title
                    for(int i = 0; i < hrefs.size(); i++){
                        String strUrl = "";
                        strUrl = hrefs.get(i).getAttribute("href");                       
                        String strTitle = "";
                        strTitle = titles.get(i).getText().trim();

                        if(strUrl.equals("") || (strUrl.equals(null))){                        
                            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                                GlobalVars.LOG_ADDIX.formatStringError("Not found href with element " + arrHref[iter], webName + keyword + " => Skip get href with these elements"));  
                        }else{
                            if(!hrefs.get(i).getAttribute("href").contains(webElement.getDomain().trim())
                                    && (!hrefs.get(i).getAttribute("href").contains("tech.ascii.jp"))
                                    && (!hrefs.get(i).getAttribute("href").contains("special.nikkeibp.co.jp"))
                                    && (!hrefs.get(i).getAttribute("href").contains("https://active.nikkeibp.co.jp"))){
                                strUrl = strUrl.replace("http://", "https://");
                            }

//                        System.out.println(i + " ANA2.========================== ");
//                        System.out.println("URL: " + strUrl);
//                        System.out.println("Check Map ITEM URL: " + GlobalVars.MAP_ITEM_URL.containsKey(strUrl));
//                        System.out.println("Total url item " + GlobalVars.TOTAL_URL_ITEM);
//                        System.out.println("Check map old data " + GlobalVars.MAP_OLD_DATA.containsKey(strUrl.trim()));
    //                        Map<String, Boolean> myOldData = GlobalVars.MAP_OLD_DATA.get(keyword);                                     
    //                        if((!GlobalVars.arrItemUrl.contains(strUrl)) && (countItemArray < GlobalVars.NUMBER_LIMIT_TOP_URL)){                        
                            if((!GlobalVars.MAP_ITEM_URL.containsKey(strUrl.trim())) && (GlobalVars.TOTAL_URL_ITEM  <= GlobalVars.NUMBER_LIMIT_TOP_URL)){                        
                                if((!GlobalVars.MAP_OLD_DATA.containsKey(strUrl.trim())) && (!strUrl.contains("/tag/"))
                                        && (!strUrl.contains("page="))){                        
                                    GlobalVars.MAP_ITEM_URL.put(strUrl, Boolean.TRUE);
                                    System.out.println(i + ".Write data to map " + strUrl);
                                    WebsiteData myModelData = new WebsiteData(webName, 
                                            GlobalVars.MAP_WEBSITE_ELEMENTS.getWebsiteElement(webName).getDomain().toString().trim(),
                                            keyword, strUrl, strTitle);
                                    GlobalVars.LIST_WEBSITE_DATA.add(myModelData);                                
                                }                            
                                GlobalVars.TOTAL_URL_ITEM ++;
                            }                        
                        }
                    }   
                
//            GlobalVars.LIB_SELENIUM.close();            
            }   
        }catch(Exception ex){
            GlobalVars.ERRORS = 1;
            System.out.println("Error read Href");
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                GlobalVars.LOG_ADDIX.formatStringError("Error JSoup read page (Offline) " + childUrl, ex.toString()));  
        }
    }   
    /**
     * Read link N child item url of webpage, then save them to LIST_WEBSITE_DATA
     * @param childUrl
     * @param webName
     * @param keyword 
     */
    public void getTopNWebsiteItem(String childUrl, String webName, String keyword){
        try{
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
//                    Map<String, Boolean> myOldData = GlobalVars.MAP_OLD_DATA.get(keyword);
                    System.out.println(i + " .========================== ");
                    System.out.println("URL: " + strUrl);
                    System.out.println("Check Map ITEM URL: " + GlobalVars.MAP_ITEM_URL.containsKey(strUrl));
                    System.out.println("Total url item " + GlobalVars.TOTAL_URL_ITEM);
                    System.out.println("Check map old data " + GlobalVars.MAP_OLD_DATA.containsKey(strUrl.trim()));
 
//                    if((!GlobalVars.arrItemUrl.contains(strUrl)) && (countItemArray < GlobalVars.NUMBER_LIMIT_TOP_URL)){                        
                    if((!GlobalVars.MAP_ITEM_URL.containsKey(strUrl)) && (GlobalVars.TOTAL_URL_ITEM  <= GlobalVars.NUMBER_LIMIT_TOP_URL)){                        
                        if((!GlobalVars.MAP_OLD_DATA.containsKey(strUrl.trim())) && (!strUrl.contains("/tag/"))){                        
//                        if((!myOldData.containsKey(strUrl)) && (!strUrl.contains("/tag/"))){                        
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
            GlobalVars.ERRORS = 1;
            GlobalVars.LOG_ADDIX.writeLog(GlobalVars.LOG_FILE_NAME, 
                GlobalVars.LOG_ADDIX.formatStringError("Error JSoup read page " + childUrl, ex.toString()));   
        }
    
    }        
}
