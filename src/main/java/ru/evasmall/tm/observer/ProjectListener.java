package ru.evasmall.tm.observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.evasmall.tm.service.ProjectService;
import ru.evasmall.tm.service.ProjectTaskService;

import static ru.evasmall.tm.constant.TerminalConst.*;

public class ProjectListener implements Listener {
    private static final Logger logger = LogManager.getRootLogger();

    @Override
    public int update(String param) {
        ProjectService p = ProjectService.getInstance();
        ProjectTaskService pt = ProjectTaskService.getInstance();
        try {
            switch (param) {
                case CMD_PROJECT_CREATE:
                    p.createProject();
                    return RETURN_OK;
                case CMD_PROJECT_CLEAR:
                    p.clearProject();
                    return RETURN_OK;
                case CMD_PROJECT_LIST:
                    p.listProject();
                    return RETURN_OK;

                case CMD_PROJECT_VIEW_BY_NAME:
                    p.viewProjectByName();
                    return RETURN_OK;
                case CMD_PROJECT_VIEW_BY_INDEX:
                    p.viewProjectByIndex();
                    return RETURN_OK;
                case CMD_PROJECT_VIEW_BY_ID:
                    p.viewProjectById();
                    return RETURN_OK;

                case CMD_PROJECT_REMOVE_BY_ID:
                    p.removeProjectById();
                    return RETURN_OK;
                case CMD_PROJECT_REMOVE_BY_INDEX:
                    p.removeProjectByIndex();
                    return RETURN_OK;

                case CMD_PROJECT_UPDATE_BY_INDEX:
                    p.updateProjectByIndex();
                    return RETURN_OK;
                case CMD_PROJECT_UPDATE_BY_ID:
                    p.updateProjectById();
                    return RETURN_OK;

                case CMD_PROJECT_ADD_USER:
                    p.addProjectToUser();
                    return RETURN_OK;
                case CMD_PROJECT_REMOVE_USER:
                    p.removeProjectFromUser();
                    return RETURN_OK;

                case CMD_PROJECT_REMOVE_BY_ID_WITH_TASKS:
                    pt.removeProjectByIdWithTasks();
                    return RETURN_OK;
                case CMD_PROJECT_REMOVE_BY_INDEX_WITH_TASKS:
                    pt.removeProjectByIndexWithTasks();
                    return RETURN_OK;

                case CMD_OBJECT_JSON:
                    p.writeProjectJson();
                    return RETURN_OK;
                case CMD_OBJECT_XML:
                    p.writeProjectXML();
                    return RETURN_OK;

                default:
                    return RETURN_FOREIGN_COMMAND;
            }
        } catch (Exception e) {
            logger.error(e);
            return RETURN_ERROR;
        }
    }

}
