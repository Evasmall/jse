package ru.evasmall.tm.observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.evasmall.tm.Application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.evasmall.tm.constant.TerminalConst.*;

class UserListenerTest {

    private UserListener userListener;

    private final Application application = new Application();

    @BeforeEach
    void setUp() {
        userListener = new UserListener();
    }

    @Test
    void updateCorrect() {
        application.userIdCurrent = 2L;
        assertEquals(RETURN_OK, userListener.update("object-json"));
    }

    @Test
    void updateException() {
        assertEquals(RETURN_FOREIGN_COMMAND, userListener.update("about"));
        assertEquals(RETURN_ERROR, userListener.update(null));
        assertEquals(RETURN_FOREIGN_COMMAND, userListener.update("Неизвестная команда"));
    }

}