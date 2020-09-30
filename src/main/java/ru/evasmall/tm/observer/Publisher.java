package ru.evasmall.tm.observer;

import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.exeption.TaskNotFoundException;

public interface Publisher {
    void addListener(Listener listener);
    void deleteListener(Listener listener);
    void notifyListener() throws ProjectNotFoundException, TaskNotFoundException;

}
