package ana.model;

public class WebsiteData {
    private int idWebsite;
    private int idKeyword;    
    //--------------- data for report
    private int rank;
    private String webName = "";    
    private String urlListTab = "";
    private String keyword = "";
    private String urlArticle = "";
    private String title = "";
    private String postDate = "";
    private String source = "";
    private String allText = "";
    private String urlUploadServer = "";

    public WebsiteData(String webName, String urlListTab, String keyword, String urlArticle, String title) {
        this.webName = webName;
        this.urlListTab = urlListTab;
        this.keyword = keyword;
        this.urlArticle = urlArticle;
        this.title = title;
    }

    
    public WebsiteData(int rank, String webName,
            String urlListTab, String keyword, String urlArticle, String title,
            String postDate, String source, String allText, String urlUploadServer) {
        this.rank = rank;
        this.webName = webName;
        this.urlListTab = urlListTab;
        this.keyword = keyword;
        this.urlArticle = urlArticle;
        this.title = title;
        this.postDate = postDate;
        this.source = source;
        this.allText = allText;
        this.urlUploadServer = urlUploadServer;
    }
    
    
    
    public void addWebsiteData(int idWebsite, int idKeyword, int rank, String webName,
            String urlListTab, String keyword, String urlArticle, String title,
            String postDate, String source, String allText, String urlUploadServer){
//        this.idWebsite = idWebsite;
//        this.idKeyword = idKeyword;
        this.rank = rank;
        this.webName = webName;
        this.urlListTab = urlListTab;
        this.keyword = keyword;
        this.urlArticle = urlArticle;
        this.title = title;
        this.postDate = postDate;
        this.source = source;
        this.allText = allText;
        this.urlUploadServer = urlUploadServer;
    }

    public int getIdWebsite() {
        return idWebsite;
    }

    public void setIdWebsite(int idWebsite) {
        this.idWebsite = idWebsite;
    }

    public int getIdKeyword() {
        return idKeyword;
    }

    public void setIdKeyword(int idKeyword) {
        this.idKeyword = idKeyword;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }

    public String getUrlListTab() {
        return urlListTab;
    }

    public void setUrlListTab(String urlListTab) {
        this.urlListTab = urlListTab;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUrlArticle() {
        return urlArticle;
    }

    public void setUrlArticle(String urlArticle) {
        this.urlArticle = urlArticle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAllText() {
        return allText;
    }

    public void setAllText(String allText) {
        this.allText = allText;
    }

    public String getUrlUploadServer() {
        return urlUploadServer;
    }

    public void setUrlUploadServer(String urlUploadServer) {
        this.urlUploadServer = urlUploadServer;
    }
    
    
}
