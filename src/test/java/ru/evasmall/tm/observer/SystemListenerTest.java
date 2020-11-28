package ru.evasmall.tm.observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.evasmall.tm.Application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.evasmall.tm.constant.TerminalConst.*;

class SystemListenerTest {

    private SystemListener systemListener;

    private final Application application = new Application();

    @BeforeEach
    void setUp() {
        systemListener = new SystemListener();
    }

    @Test
    void updateCorrect() {
        application.userIdCurrent = 2L;
        assertEquals(RETURN_OK, systemListener.update("about"));
    }

    @Test
    void updateException() {
        assertEquals(RETURN_ERROR, systemListener.update(null));
        assertEquals(RETURN_FOREIGN_COMMAND, systemListener.update("Неизвестная команда"));
    }

}