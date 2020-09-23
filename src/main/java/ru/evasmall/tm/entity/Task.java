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
public class Task {

    private Long id = System.nanoTime();

    private String name = "";

    private String description = "";

    private Long projectId;

    private Long userid = System.nanoTime();

    public Task(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return id + ": " + name;
    }

    public final static Comparator<Task> TaskSortByName = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            return t1.getName().compareTo(t2.getName());
        }
    };

}
