package ru.evasmall.tm;

import ru.evasmall.tm.controller.ProjectController;
import ru.evasmall.tm.controller.SystemController;
import ru.evasmall.tm.controller.TaskController;
import ru.evasmall.tm.controller.UserController;
import ru.evasmall.tm.enumerated.RoleEnum;
import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.exeption.TaskNotFoundException;
import ru.evasmall.tm.service.*;
import ru.evasmall.tm.util.HashCode;

import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static ru.evasmall.tm.constant.TerminalConst.*;

/**
 * Приложение для обучения JAVA.
 */
public class Application {

    private static final Logger logger = LogManager.getLogger(Application.class);

    private final ProjectController projectController = new ProjectController();
    private final TaskController taskController = new TaskController();
    private final UserController userController = new UserController();
    private final SystemController systemController = new SystemController();

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
        final Scanner scanner = new Scanner(System.in);
        final Application application = new Application();
        application.systemController.displayWelcome();
        application.run(args);
        String command = "";
        while (!CMD_EXIT.equals(command)) {
            command = scanner.nextLine();
            history.add(command);
            if (history.size() > 10) history.pollFirst();
            try {
                application.run(command);
            }
            catch (ProjectNotFoundException | TaskNotFoundException | IllegalArgumentException e) {
                logger.error(e);
            }
        }
    }

    public void run(final String[] args) throws ProjectNotFoundException, TaskNotFoundException {
        if (args == null) return;
        if (args.length < 1) return;
        final String param = args[0];
        final int result = run(param);
        System.exit(result);
    }

    public int run(final String param) throws ProjectNotFoundException, TaskNotFoundException {
        if (param == null || param.isEmpty()) return -1;
        switch (param) {
            case CMD_HELP: return systemController.displayHelp();
            case CMD_ABOUT: return systemController.displayAbout();
            case CMD_HISTORY: return systemController.displayHistory();
            case CMD_VERSION: return systemController.displayVersion();
            case CMD_EXIT: {
                logger.info("Exit.");
                return systemController.displayExit();
            }


            case CMD_PROJECT_CREATE: return projectController.createProject();
            case CMD_PROJECT_CLEAR: return projectController.clearProject();
            case CMD_PROJECT_LIST: return projectController.listProject();

            case CMD_PROJECT_VIEW_BY_NAME: return projectController.viewProjectByName();
            case CMD_PROJECT_VIEW_BY_INDEX: return projectController.viewProjectByIndex();
            case CMD_PROJECT_VIEW_BY_ID: return projectController.viewProjectById();

            case CMD_PROJECT_REMOVE_BY_ID: return projectController.removeProjectById();
            case CMD_PROJECT_REMOVE_BY_INDEX: return projectController.removeProjectByIndex();
            case CMD_PROJECT_REMOVE_BY_ID_WITH_TASKS: return taskController.removeProjectByIdWithTasks();
            case CMD_PROJECT_REMOVE_BY_INDEX_WITH_TASKS: return taskController.removeProjectByIndexWithTasks();

            case CMD_PROJECT_UPDATE_BY_INDEX: return projectController.updateProjectByIndex();
            case CMD_PROJECT_UPDATE_BY_ID: return projectController.updateProjectById();
            case CMD_PROJECT_ADD_USER: return projectController.addProjectToUser();
            case CMD_PROJECT_REMOVE_USER: return projectController.removeProjectFromUser();

            case CMD_TASK_CREATE: return taskController.createTask();
            case CMD_TASK_CLEAR: return taskController.clearTask();
            case CMD_TASK_LIST: return taskController.listTask();

            case CMD_TASK_VIEW_BY_NAME: return taskController.viewTaskByName();
            case CMD_TASK_VIEW_BY_INDEX: return taskController.viewTaskByIndex();
            case CMD_TASK_VIEW_BY_ID: return taskController.viewTaskById();

            case CMD_TASK_REMOVE_BY_ID: return taskController.removeTaskById();
            case CMD_TASK_REMOVE_BY_INDEX: return taskController.removeTaskByIndex();

            case CMD_TASK_UPDATE_BY_INDEX: return taskController.updateTaskByIndex();
            case CMD_TASK_UPDATE_BY_ID: return taskController.updateTaskById();
            case CMD_TASK_ADD_USER: return taskController.addTaskToUser();
            case CMD_TASK_REMOVE_USER: return taskController.removeTaskFromUser();

            case CMD_TASK_ADD_TO_PROJECT_BY_IDS: return taskController.addTaskToProjectByIds();
            case CMD_TASK_REMOVE_FROM_PROJECT_BY_IDS: return taskController.removeTaskFromProjectByIds();
            case CMD_TASK_LIST_BY_PROJECT_ID: return taskController.listTaskByProjectId();

            case CMD_USER_REGISTRATION: return userController.createUser();
            case CMD_USER_SIGN: return userController.signUser();
            case CMD_USER_EXIT: return userController.exitUser();
            case CMD_USER_LIST: return userController.listUser(1);
            case CMD_USER_LIST_BY_FIO: return userController.listUser(2);
            case CMD_USER_REMOVE_BY_LOGIN: return userController.removeUserByLogin(userIdCurrent);
            case CMD_USER_UPDATE_ROLE: return userController.updateUserRole(userIdCurrent);
            case CMD_USER_PROFILE_VIEW: return userController.userProfile(userIdCurrent);
            case CMD_USER_PROFILE_UPDATE: return userController.updateProfile(userIdCurrent);
            case CMD_PASSWORD_CHANGE: return userController.changePassword(userIdCurrent);

            default: {
                logger.error("ERROR! Unknown program argument.");
                return -1;
            }
        }
    }

}