package ru.evasmall.tm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Comparator;

@Getter
@Setter
@AllArgsConstructor
@ToString
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

    @Override
    public String toString() {
        return id + ": " + name;
    }

    public final static Comparator<Project> ProjectSortByName = new Comparator<Project>() {
        @Override
        public int compare(Project p1, Project p2) {
            return p1.getName().compareTo(p2.getName());
        }
    };

}
