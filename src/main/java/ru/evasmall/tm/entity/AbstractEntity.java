package ru.evasmall.tm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public abstract class AbstractEntity {
    private Long id = System.nanoTime();

    private String name = "";

    private String description = "";

    private Long userid = System.nanoTime();

    public AbstractEntity() {
    }

    public AbstractEntity(String name) {
        this.name = name;
    }

    public AbstractEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
