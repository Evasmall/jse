package ru.evasmall.tm.entity;

public class Task extends AbstractEntity {

    private Long projectId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Task() {
    }

    public Task(String name) {
        super(name);
    }

    public Task(String name, String description) {
        super(name, description);
    }

}
