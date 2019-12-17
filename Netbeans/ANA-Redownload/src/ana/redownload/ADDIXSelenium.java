/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ana.redownload;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

/**
 *
 * @author Nhan.Lam
 */
public abstract class ADDIXSelenium {
    private WebDriver seleniumDriver;
    private String browserDriver = "";

    /**
     * Constructor with some parameters (default timeout to load browser driver is 3s)
     * @param browserDriver is a path to the browser driver
     * @param driverName is name of driver. Current support: chrome, ie, firefox
     */
    public ADDIXSelenium(String browserDriver, String driverName) {
        this.browserDriver = browserDriver;
        if(driverName.equals("chrome")){
            System.setProperty("webdriver.chrome.driver", this.browserDriver);
            seleniumDriver = new ChromeDriver();
        }else if(driverName.equals("ie")){
            System.setProperty("webdriver.ie.driver", this.browserDriver);
            seleniumDriver = new InternetExplorerDriver();
        }else if(driverName.equals("firefox")){
            System.setProperty("webdriver.gecko.driver", this.browserDriver);
            seleniumDriver = new FirefoxDriver();
        }
        //Set timeout when open browser driver
        seleniumDriver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);        
    }
    /**
     * Set timeout when open browser driver
     * @param time is number will browser timeout
     * @param timeUnit use TimeUnit maybe is seconds or hours ...
     */
    public void lanchTimeOutBrowser(int time, TimeUnit timeUnit){
        if(seleniumDriver != null){
            seleniumDriver.manage().timeouts().implicitlyWait(time, timeUnit);        
        }
    }
    /**
     * Set MaximizeBrowser
     */
    public void setMaximizeBrowser(){
        if(seleniumDriver != null){
            seleniumDriver.manage().window().maximize();
        }
    }
    /**
     * Using quit() function of selenium which used to shut down the web driver 
     * instance or destroy the web driver instance(Close all the windows)
     */
    public void closeDriver(){
        if(seleniumDriver != null){
            seleniumDriver.quit();
        }
    }
    /**
     * Load a new web page in the current browser
     * @param url 
     */
    public void loadUrl(String url){
        if(seleniumDriver != null){
            seleniumDriver.get(url);
        }
    }
    /**
     * find an element in web page
     * @param by
     * @return 
     */
    public WebElement findElement(By by){
        if(seleniumDriver != null){
            return seleniumDriver.findElement(by);    
        }        
        return null;
    }
    /**
     * find all element in web page
     * @param by
     * @return 
     */
    public List<WebElement> findElements(By by){
        if(seleniumDriver != null){
            return seleniumDriver.findElements(by);    
        }
        return null;        
    }
    /**
     * Get all page source from url
     * @param url
     * @return 
     */
    public String getPageSource(String url){
        if(seleniumDriver != null){
            return seleniumDriver.getPageSource();
        }
        return null;
    }
    /**
     * Get current url
     * @return 
     */
    public String getCurrentUrl(){
        if(seleniumDriver != null){
            return seleniumDriver.getCurrentUrl();
        }
        return null;
    }
    /**
     * Get the title of a page
     * @return 
     */
    public String getTitle(){
        if(seleniumDriver != null){
            return seleniumDriver.getTitle();
        }
        return null;
    }

    public WebDriver getSeleniumDriver() {
        return seleniumDriver;
    }

    public void setSeleniumDriver(WebDriver seleniumDriver) {
        this.seleniumDriver = seleniumDriver;
    }

    public String getBrowserDriver() {
        return browserDriver;
    }

    public void setBrowserDriver(String browserDriver) {
        this.browserDriver = browserDriver;
    }    
}
