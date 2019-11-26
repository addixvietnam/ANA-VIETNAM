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
    private int type = 1;
    private int saveMethod = 1;
    private int numReadPage = 1;    
    private String itemPageElement = "";

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

    public WebsiteElement(int idWebsite, int id, String href, String title, String postDate,
            String source, String content, int numClickSearch, String strNextPage,
            String domain, String strReplaceNextPage, int caseDisplay, 
            int numClickSort, String strPaging, int type, int saveMethod, int numReadPage, String itemPageElement) {
        this.idWebsite = idWebsite;
        this.id = id;
        this.href = href;
        this.title = title;    
        this.postDate = postDate;    
        this.source = source;    
        this.content = content;            
        this.numClickSearch = numClickSearch;    
        this.strNextPage = strNextPage;    
        this.domain = domain;    
        this.strReplaceNextPage = strReplaceNextPage;          
        this.caseDisplay = caseDisplay;        
        this.numClickSort = numClickSort;
        this.strPaging = strPaging;           
        this.type = type;  
        this.saveMethod = saveMethod;
        this.numReadPage = numReadPage;        
        this.itemPageElement = itemPageElement;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    
}
