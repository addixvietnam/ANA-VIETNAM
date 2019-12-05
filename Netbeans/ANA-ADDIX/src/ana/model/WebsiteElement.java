package ana.model;

public class WebsiteElement {
    private int id;
    private int idWebsite;
    private String href = "";
    private String title = "";
    private String postDate = "";    
    private String source = "";
    private String content = "";
    private int numClickSearch = 1;
    private String strNextPage = "";
    private String domain = "";
    private String strReplaceNextPage = "";
    private int caseDisplay = 1;
    private String trdl = "";
    private int numClickSort = 0;
    private String strPaging = "";
    
    private int saveMethod = 1;
    private int numReadPage = 1;    
    private String itemPageElement = "";
    
    private int crawlSwitch = 1;
    private int saveSwitch = 1;
    private int crawlMethod = 1;    
    private int loadWaitTime = 1;
    private int saveWaitTime = 1;
    private String xPathNextPage = "";
    private String webName = "";
    private int pageNum = 1;
    private String url = "";

    public int getSaveMethod() {
        return saveMethod;
    }

    public void setSaveMethod(int saveMethod) {
        this.saveMethod = saveMethod;
    }

    public int getNumReadPage() {
        return numReadPage;
    }

    public void setNumReadPage(int numReadPage) {
        this.numReadPage = numReadPage;
    }

    public WebsiteElement() {
    }

    public WebsiteElement(int idWebsite, int id, String webName, 
            int crawlSwitch, int saveSwitch, int crawlMethod, int saveMethod,
            int pageNum, int loadWaitTime, int saveWaitTime,
            String url, String classHref, String classTitle, String classPostDate,
            String classSource, String classContent, int clickSearch,
            String classNextPage, String domain, String replaceNextPage,
            int caseDisplay, String classTrDl, int clickSort, 
            String itemPageElement, String xPathNextPage) {
        this.idWebsite = idWebsite;
        this.id = id;
        this.webName = webName;
        this.crawlSwitch = crawlSwitch;
        this. saveSwitch = saveSwitch;
        this.crawlMethod = crawlMethod;
        this.saveMethod = saveMethod;
        this.numReadPage = pageNum;
        this.loadWaitTime = loadWaitTime;
        this.saveWaitTime = saveWaitTime;
        this.url = url;
        this.href = classHref;
        this.title = classTitle;
        this.postDate = classPostDate;
        this.source = classSource;
        this.content = classContent;
        this.numClickSearch = clickSearch;
        this.strNextPage = classNextPage;
        this.domain = domain;
        this.strReplaceNextPage = replaceNextPage;
        this.caseDisplay = caseDisplay;
        this.trdl = classTrDl;
        this.numClickSort = clickSort;
        this.itemPageElement = itemPageElement;
        this.xPathNextPage = xPathNextPage;
    }

    public String getItemPageElement() {
        return itemPageElement;
    }

    public void setItemPageElement(String itemPageElement) {
        this.itemPageElement = itemPageElement;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdWebsite() {
        return idWebsite;
    }

    public void setIdWebsite(int idWebsite) {
        this.idWebsite = idWebsite;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNumClickSearch() {
        return numClickSearch;
    }

    public void setNumClickSearch(int numClickSearch) {
        this.numClickSearch = numClickSearch;
    }

    public String getStrNextPage() {
        return strNextPage;
    }

    public void setStrNextPage(String strNextPage) {
        this.strNextPage = strNextPage;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getStrReplaceNextPage() {
        return strReplaceNextPage;
    }

    public void setStrReplaceNextPage(String strReplaceNextPage) {
        this.strReplaceNextPage = strReplaceNextPage;
    }

    public int getCaseDisplay() {
        return caseDisplay;
    }

    public void setCaseDisplay(int caseDisplay) {
        this.caseDisplay = caseDisplay;
    }

    public String getTrdl() {
        return trdl;
    }

    public void setTrdl(String trdl) {
        this.trdl = trdl;
    }

    public int getNumClickSort() {
        return numClickSort;
    }

    public void setNumClickSort(int numClickSort) {
        this.numClickSort = numClickSort;
    }

    public String getStrPaging() {
        return strPaging;
    }

    public void setStrPaging(String strPaging) {
        this.strPaging = strPaging;
    }

    public int getCrawlSwitch() {
        return crawlSwitch;
    }

    public void setCrawlSwitch(int crawlSwitch) {
        this.crawlSwitch = crawlSwitch;
    }

    public int getSaveSwitch() {
        return saveSwitch;
    }

    public void setSaveSwitch(int saveSwitch) {
        this.saveSwitch = saveSwitch;
    }

    public int getCrawlMethod() {
        return crawlMethod;
    }

    public void setCrawlMethod(int crawlMethod) {
        this.crawlMethod = crawlMethod;
    }

    public int getLoadWaitTime() {
        return loadWaitTime;
    }

    public void setLoadWaitTime(int loadWaitTime) {
        this.loadWaitTime = loadWaitTime;
    }

    public int getSaveWaitTime() {
        return saveWaitTime;
    }

    public void setSaveWaitTime(int saveWaitTime) {
        this.saveWaitTime = saveWaitTime;
    }

    public String getxPathNextPage() {
        return xPathNextPage;
    }

    public void setxPathNextPage(String xPathNextPage) {
        this.xPathNextPage = xPathNextPage;
    }

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
    }    

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }        

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
}
