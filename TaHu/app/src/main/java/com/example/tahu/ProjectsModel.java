package com.example.tahu;

import java.util.Map;

public class ProjectsModel {
    private String projectId, projectName, projectOwner, projectDes;
    private Map<String, Boolean> members;

    public ProjectsModel() {
    }

    public ProjectsModel(String projectId, String projectName, String projectOwner, String projectDes, Map<String, Boolean> members) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectOwner = projectOwner;
        this.projectDes = projectDes;
        this.members = members;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectOwner() {
        return projectOwner;
    }

    public void setProjectOwner(String projectOwner) {
        this.projectOwner = projectOwner;
    }

    public String getProjectDes() {
        return projectDes;
    }

    public void setProjectDes(String projectDes) {
        this.projectDes = projectDes;
    }

    public Map<String, Boolean> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Boolean> members) {
        this.members = members;
    }
}
