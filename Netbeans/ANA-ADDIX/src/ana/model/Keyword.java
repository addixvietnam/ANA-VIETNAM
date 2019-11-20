package ana.model;

public class Keyword {
    private int id;
    private String keyword;
    private String name;
    private int status = 0; //1: active; 2: pending to repair; 0: delete
    private int numLimitItem = 20;//Default get top 20 url
    
    public void addKeyword(int id, String keyword, String name, int status){
        this.id = id;
        this.keyword = keyword;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
}
