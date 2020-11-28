package ru.evasmall.tm.observer;

import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.exeption.TaskNotFoundException;

import java.util.Scanner;

public interface Publisher {
    int addListener(Listener listener);
    int deleteListener(Listener listener);
    void notifyListener(Scanner scanner) throws ProjectNotFoundException, TaskNotFoundException;
    int updateListener(String command, Scanner scanner);

}
