package ru.evasmall.tm.observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.evasmall.tm.constant.TerminalConst;
import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.exeption.TaskNotFoundException;

import java.util.*;

import static ru.evasmall.tm.Application.history;

public class PublisherImpl implements Publisher {
    private final HashSet<Listener> listeners = new HashSet<Listener>();

    private static final Logger logger = LogManager.getRootLogger();

    //Добавление слушателей
    @Override
    public void addListener(Listener listener) {
            listeners.add(listener);
    }

    //Удаление слушателей
    @Override
    public void deleteListener(Listener listener) {
        listeners.remove(listener);
    }

    //Уведомление слушателей
    @Override
    public void notifyListener() {
        final Scanner scanner = new Scanner(System.in);
        String command = "";
        while (!TerminalConst.CMD_EXIT.equals(command)) {
            command = scanner.nextLine();
            history.add(command);
            if (history.size() > 10) history.pollFirst();
            try {
                int i = 0;
                for (Listener listener : listeners){
                    i = i +listener.update(command);
                }
                //Логирование ошибок, если команда не опознана ни одним из слушателей.
                if (i == 4) logger.error("ERROR! Unknown program argument.");
            }
            catch (ProjectNotFoundException | TaskNotFoundException | IllegalArgumentException e) {
                logger.error(e);
            }
        }
    }

}
