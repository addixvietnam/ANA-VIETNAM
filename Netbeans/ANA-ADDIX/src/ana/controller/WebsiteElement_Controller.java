package ana.controller;

import ana.model.WebsiteElement;
import java.util.HashMap;
import java.util.Map;

public class WebsiteElement_Controller {
    private Map<String, WebsiteElement> webElements;

    public WebsiteElement_Controller() {
        webElements = new HashMap<>();
    }

    public Map<String, WebsiteElement> getWebElements() {
        return webElements;
    }

    public void setWebElements(Map<String, WebsiteElement> webElements) {
        this.webElements = webElements;
    }
    
    public void addWebElement(String keyword, WebsiteElement webElement){
        this.webElements.put(keyword, webElement);
    }
    
    public WebsiteElement getWebsiteElement(String keyword){
        return this.webElements.get(keyword);
    }
    
    public boolean keyExist(String keyword){
        return this.webElements.containsKey(keyword);
    }
    
}
