package ana.model;

public class Website {
    private int id;    
    private String name;
    private String url;
    private int status;

    public Website(int id, String name, String url, int status) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.status = status;
    }
     
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }    
}
