package ru.evasmall.tm.observer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.evasmall.tm.exeption.ProjectNotFoundException;
import ru.evasmall.tm.exeption.TaskNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class PublisherImpl implements Publisher{
    List<Listener> listeners = new ArrayList<>();

    private static final Logger logger = LogManager.getRootLogger();

    @Override
    public void addListener(Listener listener) {
        if (!listeners.contains(listener)){
            listeners.add(listener);
        }
    }

    @Override
    public void deleteListener(Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListener(String command) throws ProjectNotFoundException, TaskNotFoundException {
            int i = 0;
            for (Listener listener : listeners){
                i = i +listener.update(command);
            }
            if (i == -8) logger.error("ERROR! Unknown program argument.");
    }

}
