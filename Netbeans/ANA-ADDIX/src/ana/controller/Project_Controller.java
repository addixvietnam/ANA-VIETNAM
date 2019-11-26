package ana.controller;

import ana.model.Project;
import java.util.HashMap;
import java.util.Map;

public class Project_Controller {
    private Map<String, Project> projects;

    public Project_Controller() {
        projects = new HashMap<>();
    }
    
    public Map<String, Project> getProjects() {
        return projects;
    }

    public void setProjects(Map<String, Project> projects) {
        this.projects = projects;
    }
    
    public void addProject(String keyword, Project project){
        projects.put(keyword, project);
    }
    
    public Project getProject(String keyword){
        return projects.get(keyword);
    }
    
    public boolean keyExist(String keyword){
        return projects.containsKey(keyword);
    }
    
}
