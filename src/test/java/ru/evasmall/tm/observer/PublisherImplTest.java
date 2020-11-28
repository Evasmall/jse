package ru.evasmall.tm.observer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.evasmall.tm.constant.TerminalConst.*;

class PublisherImplTest {

    private PublisherImpl publisher;

    private ProjectListener projectListener;
    private TaskListener taskListener;
    private UserListener userListener;
    private SystemListener systemListener;
    private InputStream inputStream = System.in;

    @BeforeEach
    void setUp() {
        publisher = new PublisherImpl();
        projectListener = Mockito.mock(ProjectListener.class);
        taskListener = Mockito.mock(TaskListener.class);
        userListener = Mockito.mock(UserListener.class);
        systemListener = Mockito.mock(SystemListener.class);
    }

    @Test
    void addDeleteListenerCorrect() {
        assertEquals(RETURN_OK, publisher.addListener(projectListener));
        assertEquals(RETURN_OK, publisher.addListener(taskListener));
        assertEquals(RETURN_OK, publisher.addListener(userListener));
        assertEquals(RETURN_OK, publisher.addListener(systemListener));
        assertEquals(RETURN_OK, publisher.deleteListener(projectListener));
        assertEquals(RETURN_OK, publisher.deleteListener(taskListener));
        assertEquals(RETURN_OK, publisher.deleteListener(userListener));
        assertEquals(RETURN_OK, publisher.deleteListener(systemListener));
    }

    @Test
    void notifyListenerCorrect() {
        String command =  CMD_PROJECT_LIST + "\n" + CMD_EXIT;
        System.setIn(new ByteArrayInputStream(command.getBytes()));
        Scanner scanner = new Scanner(System.in);
        publisher.notifyListener(scanner);
        projectListener.update(CMD_PROJECT_LIST);
        System.setIn(inputStream);
    }

    @Test
    void notifyListenerException() {
        String command =  CMD_PROJECT_LIST + "\n";
        System.setIn(new ByteArrayInputStream(command.getBytes()));
        Scanner scanner = new Scanner(System.in);
        assertThrows(NoSuchElementException.class, () -> publisher.notifyListener(scanner));
    }

}