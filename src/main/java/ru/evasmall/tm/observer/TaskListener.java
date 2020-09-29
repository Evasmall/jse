package ru.evasmall.tm.observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.exeption.TaskNotFoundException;
import ru.evasmall.tm.service.ProjectTaskService;
import ru.evasmall.tm.service.TaskService;

import static ru.evasmall.tm.constant.TerminalConst.*;

public class TaskListener implements Listener {
    private static final Logger logger = LogManager.getRootLogger();

    @Override
    public int update(String param) throws ProjectNotFoundException, TaskNotFoundException {
        try {
            switch (param) {
                case CMD_TASK_CREATE:
                    TaskService.getInstance().createTask();
                    return 0;
                case CMD_TASK_CLEAR:
                    TaskService.getInstance().clearTask();
                    return 0;
                case CMD_TASK_LIST:
                    TaskService.getInstance().listTask();
                    return 0;

                case CMD_TASK_VIEW_BY_NAME:
                    TaskService.getInstance().viewTaskByName();
                    return 0;
                case CMD_TASK_VIEW_BY_INDEX:
                    TaskService.getInstance().viewTaskByIndex();
                    return 0;
                case CMD_TASK_VIEW_BY_ID:
                    TaskService.getInstance().viewTaskById();
                    return 0;

                case CMD_TASK_REMOVE_BY_ID:
                    TaskService.getInstance().removeTaskById();
                    return 0;
                case CMD_TASK_REMOVE_BY_INDEX:
                    TaskService.getInstance().removeTaskByIndex();
                    return 0;

                case CMD_TASK_UPDATE_BY_INDEX:
                    TaskService.getInstance().updateTaskByIndex();
                    return 0;
                case CMD_TASK_UPDATE_BY_ID:
                    TaskService.getInstance().updateTaskById();
                    return 0;

                case CMD_TASK_ADD_USER:
                    TaskService.getInstance().addTaskToUser();
                    return 0;
                case CMD_TASK_REMOVE_USER:
                    TaskService.getInstance().removeTaskFromUser();
                    return 0;

                case CMD_TASK_ADD_TO_PROJECT_BY_IDS:
                    ProjectTaskService.getInstance().addTaskToProjectByIds();
                    return 0;
                case CMD_TASK_REMOVE_FROM_PROJECT_BY_IDS:
                    ProjectTaskService.getInstance().removeTaskFromProjectByIds();
                    return 0;
                case CMD_TASK_LIST_BY_PROJECT_ID:
                    ProjectTaskService.getInstance().listTaskByProjectId();
                    return 0;
                default:
                    return -2;
            }
        }
        catch (ProjectNotFoundException | IllegalArgumentException e) {
            logger.error(e);
            return -1;
        }
    }
}
