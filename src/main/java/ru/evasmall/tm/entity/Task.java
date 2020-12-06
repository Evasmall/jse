package ru.evasmall.tm.entity;

import java.time.LocalDateTime;

public class Task extends AbstractEntity {

    private Long projectId;

    private LocalDateTime deadline;

    private String notifyDeadline = "8 HOURS";

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getNotifyDeadline() {
        return notifyDeadline;
    }

    public void setNotifyDeadline(String notifyDeadline) {
        this.notifyDeadline = notifyDeadline;
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
