package ru.evasmall.tm.observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.evasmall.tm.service.SystemService;

import static ru.evasmall.tm.constant.TerminalConst.*;

public class SystemListener implements Listener {
    private static final Logger logger = LogManager.getRootLogger();

    @Override
    public int update(String param) {
        try {
            switch (param) {
                case CMD_HELP:
                    SystemService.displayHelp();
                    return RETURN_OK;
                case CMD_VERSION:
                    SystemService.displayVersion();
                    return RETURN_OK;
                case CMD_ABOUT:
                    SystemService.displayAbout();
                    return RETURN_OK;
                case CMD_HISTORY:
                    SystemService.displayHistory();
                    return RETURN_OK;
                case CMD_EXIT: {
                    logger.info("Exit.");
                    SystemService.displayExit();
                    return RETURN_OK;
                }
                default:
                    return RETURN_FOREIGN_COMMAND;
            }
        } catch (Exception e) {
            logger.error(e);
            return RETURN_ERROR;
        }
    }
}
