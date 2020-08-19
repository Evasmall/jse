package ru.evasmall.tm.entity;

import java.util.Comparator;

public class Task {

    private Long id = System.nanoTime();

    private String name = "";

    private String description = "";

    private Long projectId;

    private Long userid = System.nanoTime();

    public Task(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return id + ": " + name;
    }

    public static Comparator<Task> TaskSortByName = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t1.getName().compareTo(t2.getName());
        }
    };

}
