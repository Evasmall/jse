package ru.evasmall.tm.observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.evasmall.tm.Application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.evasmall.tm.constant.TerminalConst.*;

class ProjectListenerTest {

    private ProjectListener projectListener;

    private final Application application = new Application();

    @BeforeEach
    void setUp() {
        projectListener = new ProjectListener();
    }

    @Test
    void updateCorrect() {
        application.userIdCurrent = 2L;
        assertEquals(RETURN_OK, projectListener.update("object-json"));
    }

    @Test
    void updateException() {
        assertEquals(RETURN_FOREIGN_COMMAND, projectListener.update("about"));
        assertEquals(RETURN_ERROR, projectListener.update(null));
        assertEquals(RETURN_FOREIGN_COMMAND, projectListener.update("Неизвестная команда"));
    }

}