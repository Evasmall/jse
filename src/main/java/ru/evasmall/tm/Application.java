package ru.evasmall.tm;

import ru.evasmall.tm.enumerated.RoleEnum;
import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.exeption.TaskNotFoundException;
import ru.evasmall.tm.observer.*;
import ru.evasmall.tm.service.*;
import ru.evasmall.tm.util.HashCode;

import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        UserService.getInstance().create(System.nanoTime(),"ADMIN", HashCode.getHash("POBEDA"), "Василий", "Чапаев",
                           "Иванович", "chapaev_vi@gmail.com", RoleEnum.ADMIN, true);
        UserService.getInstance().create(System.nanoTime(),"TEST", HashCode.getHash("123"), "Пётр", "Исаев",
                                     "Семёнович", "isaev_ps@gmail.com", RoleEnum.USER, false);
        UserService.getInstance().create(System.nanoTime(),"FF", HashCode.getHash("12345"), "Дмитрий", "Фурманов",
                "Андреевич", "furmanov_da@gmail.com", RoleEnum.USER, false);

        ProjectService.getInstance().create("DEMO_PROJECT_3", "DESC PROJECT 3", UserService.getInstance().findByLogin("ADMIN").getUserid());
        ProjectService.getInstance().create("DEMO_PROJECT_1", "DESC PROJECT 4", UserService.getInstance().findByLogin("TEST").getUserid());
        ProjectService.getInstance().create("DEMO_PROJECT_1", "DESC PROJECT 1", UserService.getInstance().findByLogin("TEST").getUserid());
        ProjectService.getInstance().create("DEMO_PROJECT_2", "DESC PROJECT 2", UserService.getInstance().findByLogin("TEST").getUserid());

        TaskService.getInstance().create("TEST_TASK_3", "DESC TASK 3", UserService.getInstance().findByLogin("ADMIN").getUserid() );
        TaskService.getInstance().create("TEST_TASK_2", "DESC TASK 2", UserService.getInstance().findByLogin("TEST").getUserid());
        TaskService.getInstance().create("TEST_TASK_1", "DESC TASK 1", UserService.getInstance().findByLogin("TEST").getUserid());
    }

    public static void main(final String[] args) throws ProjectNotFoundException, TaskNotFoundException {
        final Application application = new Application();
        SystemService.displayWelcome();
        Publisher publisher = new PublisherImpl();
        publisher.addListener(new ProjectListener());
        publisher.addListener(new UserListener());
        publisher.addListener(new SystemListener());
        publisher.addListener(new TaskListener());
        publisher.notifyListener();
    }

}