package ana.model;


public class Project {
    private int id;
    private String name;
    private String createdDate;
    private int status;//1: active; 2: pending to repair; 0: delete

    public Project(int id, String name, String createdDate, int status) {
        this.id = id;
        this.name = name;
        this.createdDate = createdDate;
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

    public String getCreateDate() {
        return createdDate;
    }

    public void setCreateDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }    
}
