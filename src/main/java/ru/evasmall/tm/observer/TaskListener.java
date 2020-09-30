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
            TaskService t = TaskService.getInstance();
            ProjectTaskService pt = ProjectTaskService.getInstance();
            switch (param) {
                case CMD_TASK_CREATE:
                    t.createTask();
                    return RETURN_OK;
                case CMD_TASK_CLEAR:
                    t.clearTask();
                    return RETURN_OK;
                case CMD_TASK_LIST:
                    t.listTask();
                    return RETURN_OK;

                case CMD_TASK_VIEW_BY_NAME:
                    t.viewTaskByName();
                    return RETURN_OK;
                case CMD_TASK_VIEW_BY_INDEX:
                    t.viewTaskByIndex();
                    return RETURN_OK;
                case CMD_TASK_VIEW_BY_ID:
                    t.viewTaskById();
                    return RETURN_OK;

                case CMD_TASK_REMOVE_BY_ID:
                    t.removeTaskById();
                    return RETURN_OK;
                case CMD_TASK_REMOVE_BY_INDEX:
                    t.removeTaskByIndex();
                    return RETURN_OK;

                case CMD_TASK_UPDATE_BY_INDEX:
                    t.updateTaskByIndex();
                    return RETURN_OK;
                case CMD_TASK_UPDATE_BY_ID:
                    t.updateTaskById();
                    return RETURN_OK;

                case CMD_TASK_ADD_USER:
                    t.addTaskToUser();
                    return RETURN_OK;
                case CMD_TASK_REMOVE_USER:
                    t.removeTaskFromUser();
                    return RETURN_OK;

                case CMD_TASK_ADD_TO_PROJECT_BY_IDS:
                    pt.addTaskToProjectByIds();
                    return RETURN_OK;
                case CMD_TASK_REMOVE_FROM_PROJECT_BY_IDS:
                    pt.removeTaskFromProjectByIds();
                    return RETURN_OK;
                case CMD_TASK_LIST_BY_PROJECT_ID:
                    pt.listTaskByProjectId();
                    return RETURN_OK;
                default:
                    return RETURN_FOREIGN_COMMAND;
            }
        }
        catch (ProjectNotFoundException | IllegalArgumentException e) {
            logger.error(e);
            return RETURN_ERROR;
        }
    }
}
