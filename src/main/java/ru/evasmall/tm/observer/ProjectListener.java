package ru.evasmall.tm.observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.exeption.TaskNotFoundException;
import ru.evasmall.tm.service.ProjectService;
import ru.evasmall.tm.service.ProjectTaskService;

import static ru.evasmall.tm.constant.TerminalConst.*;

public class ProjectListener implements Listener {
    private static final Logger logger = LogManager.getRootLogger();

    @Override
    public int update(String param) throws ProjectNotFoundException, TaskNotFoundException {
        try {
            switch (param) {
                case CMD_PROJECT_CREATE:
                    ProjectService.getInstance().createProject();
                    return 0;
                case CMD_PROJECT_CLEAR:
                    ProjectService.getInstance().clearProject();
                    return 0;
                case CMD_PROJECT_LIST:
                    ProjectService.getInstance().listProject();
                    return 0;

                case CMD_PROJECT_VIEW_BY_NAME:
                    ProjectService.getInstance().viewProjectByName();
                    return 0;
                case CMD_PROJECT_VIEW_BY_INDEX:
                    ProjectService.getInstance().viewProjectByIndex();
                    return 0;
                case CMD_PROJECT_VIEW_BY_ID:
                    ProjectService.getInstance().viewProjectById();
                    return 0;

                case CMD_PROJECT_REMOVE_BY_ID:
                    ProjectService.getInstance().removeProjectById();
                    return 0;
                case CMD_PROJECT_REMOVE_BY_INDEX:
                    ProjectService.getInstance().removeProjectByIndex();
                    return 0;

                case CMD_PROJECT_UPDATE_BY_INDEX:
                    ProjectService.getInstance().updateProjectByIndex();
                    return 0;
                case CMD_PROJECT_UPDATE_BY_ID:
                    ProjectService.getInstance().updateProjectById();
                    return 0;

                case CMD_PROJECT_ADD_USER:
                    ProjectService.getInstance().addProjectToUser();
                    return 0;
                case CMD_PROJECT_REMOVE_USER:
                    ProjectService.getInstance().removeProjectFromUser();
                    return 0;

                case CMD_PROJECT_REMOVE_BY_ID_WITH_TASKS:
                    ProjectTaskService.getInstance().removeProjectByIdWithTasks();
                    return 0;
                case CMD_PROJECT_REMOVE_BY_INDEX_WITH_TASKS:
                    ProjectTaskService.getInstance().removeProjectByIndexWithTasks();
                    return 0;
                default:
                    return -2;
            }
        } catch (ProjectNotFoundException | IllegalArgumentException e) {
            logger.error(e);
            return -1;
        }
    }

}
