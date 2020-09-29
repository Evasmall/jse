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
                    return 0;
                case CMD_VERSION:
                    SystemService.displayVersion();
                    return 0;
                case CMD_ABOUT:
                    SystemService.displayAbout();
                    return 0;
                case CMD_HISTORY:
                    SystemService.displayHistory();
                    return 0;
                case CMD_EXIT: {
                    logger.info("Exit.");
                    SystemService.displayExit();
                    return 0;
                }
                default:
                    return -2;
            }
        } catch (Exception e) {
            logger.error(e);
            return -1;
        }
    }
}
