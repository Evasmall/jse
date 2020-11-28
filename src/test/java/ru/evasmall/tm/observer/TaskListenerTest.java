package ru.evasmall.tm.observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.evasmall.tm.Application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.evasmall.tm.constant.TerminalConst.*;

class TaskListenerTest {

    private TaskListener taskListener;

    private final Application application = new Application();

    @BeforeEach
    void setUp() {
        taskListener = new TaskListener();
    }

    @Test
    void updateCorrect() {
        application.userIdCurrent = 2L;
        assertEquals(RETURN_OK, taskListener.update("object-json"));
    }

    @Test
    void updateException() {
        assertEquals(RETURN_FOREIGN_COMMAND, taskListener.update("about"));
        assertEquals(RETURN_ERROR, taskListener.update(null));
        assertEquals(RETURN_FOREIGN_COMMAND, taskListener.update("Неизвестная команда"));
    }

}