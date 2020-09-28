package ru.evasmall.tm;

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

    private final ProjectService projectService = new ProjectService();
    private final TaskService taskService = new TaskService();
    private final UserService userService = new UserService();
    private final ProjectTaskService projectTaskService = new ProjectTaskService();
    private final SystemService systemService = new SystemService();

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
        application.systemService.displayWelcome();
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
            case CMD_HELP: return systemService.displayHelp();
            case CMD_ABOUT: return systemService.displayAbout();
            case CMD_HISTORY: return systemService.displayHistory();
            case CMD_VERSION: return systemService.displayVersion();
            case CMD_EXIT: {
                logger.info("Exit.");
                return systemService.displayExit();
            }


            case CMD_PROJECT_CREATE: return projectService.createProject();
            case CMD_PROJECT_CLEAR: return projectService.clearProject();
            case CMD_PROJECT_LIST: return projectService.listProject();

            case CMD_PROJECT_VIEW_BY_NAME: return projectService.viewProjectByName();
            case CMD_PROJECT_VIEW_BY_INDEX: return projectService.viewProjectByIndex();
            case CMD_PROJECT_VIEW_BY_ID: return projectService.viewProjectById();

            case CMD_PROJECT_REMOVE_BY_ID: return projectService.removeProjectById();
            case CMD_PROJECT_REMOVE_BY_INDEX: return projectService.removeProjectByIndex();
            case CMD_PROJECT_REMOVE_BY_ID_WITH_TASKS: return projectTaskService.removeProjectByIdWithTasks();
            case CMD_PROJECT_REMOVE_BY_INDEX_WITH_TASKS: return projectTaskService.removeProjectByIndexWithTasks();

            case CMD_PROJECT_UPDATE_BY_INDEX: return projectService.updateProjectByIndex();
            case CMD_PROJECT_UPDATE_BY_ID: return projectService.updateProjectById();
            case CMD_PROJECT_ADD_USER: return projectService.addProjectToUser();
            case CMD_PROJECT_REMOVE_USER: return projectService.removeProjectFromUser();

            case CMD_TASK_CREATE: return taskService.createTask();
            case CMD_TASK_CLEAR: return taskService.clearTask();
            case CMD_TASK_LIST: return taskService.listTask();

            case CMD_TASK_VIEW_BY_NAME: return taskService.viewTaskByName();
            case CMD_TASK_VIEW_BY_INDEX: return taskService.viewTaskByIndex();
            case CMD_TASK_VIEW_BY_ID: return taskService.viewTaskById();

            case CMD_TASK_REMOVE_BY_ID: return taskService.removeTaskById();
            case CMD_TASK_REMOVE_BY_INDEX: return taskService.removeTaskByIndex();

            case CMD_TASK_UPDATE_BY_INDEX: return taskService.updateTaskByIndex();
            case CMD_TASK_UPDATE_BY_ID: return taskService.updateTaskById();
            case CMD_TASK_ADD_USER: return taskService.addTaskToUser();
            case CMD_TASK_REMOVE_USER: return taskService.removeTaskFromUser();

            case CMD_TASK_ADD_TO_PROJECT_BY_IDS: return projectTaskService.addTaskToProjectByIds();
            case CMD_TASK_REMOVE_FROM_PROJECT_BY_IDS: return projectTaskService.removeTaskFromProjectByIds();
            case CMD_TASK_LIST_BY_PROJECT_ID: return projectTaskService.listTaskByProjectId();

            case CMD_USER_REGISTRATION: return userService.createUser();
            case CMD_USER_SIGN: return userService.exitUser();
            case CMD_USER_LIST: return userService.listUser(1);
            case CMD_USER_LIST_BY_FIO: return userService.listUser(2);
            case CMD_USER_REMOVE_BY_LOGIN: return userService.removeUserByLogin(userIdCurrent);
            case CMD_USER_UPDATE_ROLE: return userService.updateUserRole(userIdCurrent);
            case CMD_USER_PROFILE_VIEW: return userService.userProfile(userIdCurrent);
            case CMD_USER_PROFILE_UPDATE: return userService.updateProfile(userIdCurrent);
            case CMD_PASSWORD_CHANGE: return userService.changePassword(userIdCurrent);

            default: {
                logger.error("ERROR! Unknown program argument.");
                return -1;
            }
        }
    }

}