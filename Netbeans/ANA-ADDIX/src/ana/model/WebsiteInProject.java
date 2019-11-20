package ana.model;

public class WebsiteInProject {
    private int idWebsite;
    private int idProject;
    private String createdDate = "";
    private String createdBy = "";
    private String deletedDate = "";
    private String deletedBy = "";
    private int status = 0; //1: active; 2: pending to repair; 0: delete
    
    public void addWebsiteInProject(int idWebsite, int idProject, String createdDate,
            String createdBy, String deletedDate, String deletedBy, int status){
        this.idWebsite = idWebsite;
        this.idProject = idProject;
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.deletedDate = deletedDate;
        this.deletedBy = deletedBy;
        this.status = status;        
    }

    public int getIdWebsite() {
        return idWebsite;
    }

    public void setIdWebsite(int idWebsite) {
        this.idWebsite = idWebsite;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(String deletedDate) {
        this.deletedDate = deletedDate;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    
    
}
