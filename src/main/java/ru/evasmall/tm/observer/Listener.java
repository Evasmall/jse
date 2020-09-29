package ru.evasmall.tm.observer;

import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.exeption.TaskNotFoundException;

public interface Listener {
    int update(String command) throws ProjectNotFoundException, TaskNotFoundException;

}
