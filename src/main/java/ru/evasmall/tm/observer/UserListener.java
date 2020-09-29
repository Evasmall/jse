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
            switch (param) {
                case CMD_USER_REGISTRATION:
                    UserService.getInstance().createUser();
                    return 0;
                case CMD_USER_SIGN:
                    UserService.getInstance().signUser();
                    return 0;
                case CMD_USER_LIST:
                    UserService.getInstance().listUser(1);
                    return 0;
                case CMD_USER_LIST_BY_FIO:
                    UserService.getInstance().listUser(2);
                    return 0;
                case CMD_USER_REMOVE_BY_LOGIN:
                    UserService.getInstance().removeUserByLogin(Application.userIdCurrent);
                    return 0;
                case CMD_USER_UPDATE_ROLE:
                    UserService.getInstance().updateUserRole(Application.userIdCurrent);
                    return 0;
                case CMD_USER_PROFILE_VIEW:
                    UserService.getInstance().userProfile(Application.userIdCurrent);
                    return 0;
                case CMD_USER_PROFILE_UPDATE:
                    UserService.getInstance().updateProfile(Application.userIdCurrent);
                    return 0;
                case CMD_PASSWORD_CHANGE:
                    UserService.getInstance().changePassword(Application.userIdCurrent);
                    return 0;
                case CMD_USER_EXIT:
                    UserService.getInstance().exitUser();
                    return 0;
                default:
                    return -2;
            }
        }
        catch (IllegalArgumentException | NullPointerException e) {
                logger.error(e);
                return -1;
            }
        }

}
