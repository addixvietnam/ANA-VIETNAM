/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ana.redownload;

/**
 *
 * @author Nhan.Lam
 */
public class WebLink {
    private String webName = "";
    private String keyword = "";
    private String linkDownload = "";

    public WebLink(String webName, String keyword, String linkDownload){
        this.webName = webName;
        this.keyword = keyword;
        this.linkDownload = linkDownload;        
    }
    
    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getLinkDownload() {
        return linkDownload;
    }

    public void setLinkDownload(String linkDownload) {
        this.linkDownload = linkDownload;
    }
}
