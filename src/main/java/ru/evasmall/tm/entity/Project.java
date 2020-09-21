package ru.evasmall.tm.entity;

import java.util.Comparator;
public class Project {

    private Long id = System.nanoTime();

    private String name = "";

    private String description = "";

    private Long userid = System.nanoTime();

    public Project() {
    }

    public Project(String name) {
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

    public static Comparator<Project> ProjectSortByName = new Comparator<Project>() {
        @Override
        public int compare(Project p1, Project p2) {
            return p1.getName().compareTo(p2.getName());
        }
    };

}
