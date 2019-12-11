package ana.controller;

import org.openqa.selenium.WebDriver;

public class SeleniumAdapter {
    private WebDriver driver;
//    private static final String chromeDriver = "";

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
    
    /**
     * Connect to website by URL
     * @param URL : URL to target website
     * @return 
     */
    public boolean connect(String URL) {
        driver.get(URL);
        return true;
    }
    /**
     * Get all page source from url page
     * @return 
     */
    public String getHtml() {
        return driver.getPageSource();
    }    
    /**
     * Close the browser
     */
    public void close() {
        driver.close();
        driver.quit();
    }    
}
