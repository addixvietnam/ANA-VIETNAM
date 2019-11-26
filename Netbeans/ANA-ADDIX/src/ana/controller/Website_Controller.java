package ana.controller;

import ana.model.Website;
import java.util.HashMap;
import java.util.Map;

public class Website_Controller {
    private Map<String, Website> websites;

    public Website_Controller() {
        websites = new HashMap<>();
    }
    
    public Map<String, Website> getWebsites() {
        return websites;
    }

    public void setWebsites(Map<String, Website> websites) {
        this.websites = websites;
    }
    
    public void addWebsite(String keyword, Website website){
        websites.put(keyword, website);
    }
    
    public Website getWebsite(String keyword){
        return websites.get(keyword);
    }
    
    public boolean keyExist(String keyword){
        return websites.containsKey(keyword);
    }
    
}
