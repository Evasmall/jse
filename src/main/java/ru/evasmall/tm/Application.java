package ru.evasmall.tm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.exeption.TaskNotFoundException;
import ru.evasmall.tm.observer.*;
import ru.evasmall.tm.service.ProjectService;
import ru.evasmall.tm.service.SystemService;
import ru.evasmall.tm.service.TaskService;
import ru.evasmall.tm.service.UserService;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * Приложение для обучения JAVA.
 */
public class Application {

    private static final Logger logger = LogManager.getLogger(Application.class);

    //Текущая сессия пользователя
    public static Long userIdCurrent = null;

    //История команд
    public final static LinkedList<String> history = new LinkedList();

    {
        logger.info("Begin program.");
        UserService.getInstance().createBeginUsers();
        ProjectService.getInstance().createBeginProjects();
        TaskService.getInstance().createBeginTasks();
    }

    public static void main(final String[] args) throws ProjectNotFoundException, TaskNotFoundException {
        final Application application = new Application();
        SystemService.displayWelcome();
        Publisher publisher = new PublisherImpl();
        publisher.addListener(new ProjectListener());
        publisher.addListener(new UserListener());
        publisher.addListener(new SystemListener());
        publisher.addListener(new TaskListener());
        publisher.notifyListener(new Scanner(System.in));
    }

}