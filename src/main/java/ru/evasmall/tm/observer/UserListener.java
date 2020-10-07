package ru.evasmall.tm.observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.evasmall.tm.Application;
import ru.evasmall.tm.service.UserService;

import static ru.evasmall.tm.constant.TerminalConst.*;

public class UserListener implements Listener {
    private static final Logger logger = LogManager.getRootLogger();

    @Override
    public int update(String param) {
        try {
            UserService u =UserService.getInstance();
            switch (param) {
                case CMD_USER_REGISTRATION:
                    u.createUser();
                    return RETURN_OK;
                case CMD_USER_SIGN:
                    u.signUser();
                    return RETURN_OK;
                case CMD_USER_LIST:
                    u.listUser(1);
                    return RETURN_OK;
                case CMD_USER_LIST_BY_FIO:
                    u.listUser(2);
                    return RETURN_OK;
                case CMD_USER_REMOVE_BY_LOGIN:
                    u.removeUserByLogin(Application.userIdCurrent);
                    return RETURN_OK;
                case CMD_USER_UPDATE_ROLE:
                    u.updateUserRole(Application.userIdCurrent);
                    return RETURN_OK;
                case CMD_USER_PROFILE_VIEW:
                    u.userProfile(Application.userIdCurrent);
                    return RETURN_OK;
                case CMD_USER_PROFILE_UPDATE:
                    u.updateProfile(Application.userIdCurrent);
                    return RETURN_OK;
                case CMD_PASSWORD_CHANGE:
                    u.changePassword(Application.userIdCurrent);
                    return RETURN_OK;
                case CMD_USER_EXIT:
                    u.exitUser();
                    return RETURN_OK;

                case CMD_OBJECT_JSON:
                    u.writeUserJson();
                    return RETURN_OK;
                case CMD_OBJECT_XML:
                    u.writeUserXML();
                    return RETURN_OK;

                default:
                    return RETURN_FOREIGN_COMMAND;
            }
        }
        catch (Exception e) {
                logger.error(e);
                return RETURN_ERROR;
            }
        }

}
