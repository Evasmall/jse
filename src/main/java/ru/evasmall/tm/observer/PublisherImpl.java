package ru.evasmall.tm.observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.evasmall.tm.constant.TerminalConst;

import java.util.HashSet;
import java.util.Scanner;

import static ru.evasmall.tm.Application.history;
import static ru.evasmall.tm.constant.TerminalConst.RETURN_OK;

public class PublisherImpl implements Publisher {
    private final HashSet<Listener> listeners = new HashSet<Listener>();

    private static final Logger logger = LogManager.getRootLogger();

    //Добавление слушателей
    @Override
    public int addListener(Listener listener) {
        listeners.add(listener);
        return RETURN_OK;
    }

    //Удаление слушателей
    @Override
    public int deleteListener(Listener listener) {
        listeners.remove(listener);
        return RETURN_OK;
    }

    //Уведомление слушателей
    @Override
    public void notifyListener(Scanner scanner) {
        String command = "";
        while (!TerminalConst.CMD_EXIT.equals(command)) {
            command = scanner.nextLine();
            history.add(command);
            if (history.size() > 10) history.pollFirst();
            try {
                int i = updateListener(command, scanner);
                //Логирование ошибок, если команда не опознана ни одним из слушателей.
                if (i == 4) {
                    logger.error("ERROR! Unknown program argument.");
                }
            }
            catch (Exception e) {
                logger.error(e);
            }
        }
    }

    @Override
    public int updateListener(String command, Scanner scanner) {
        int i = 0;
        for (Listener listener : listeners){
            i = i + listener.update(command);
        }
        return i;
    }

}
