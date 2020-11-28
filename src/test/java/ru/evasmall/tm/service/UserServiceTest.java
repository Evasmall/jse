package ru.evasmall.tm.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.evasmall.tm.Application;
import ru.evasmall.tm.entity.User;
import ru.evasmall.tm.enumerated.RoleEnum;
import ru.evasmall.tm.util.HashCode;

import static org.junit.jupiter.api.Assertions.*;
import static ru.evasmall.tm.constant.TerminalConst.RETURN_ERROR;
import static ru.evasmall.tm.constant.TerminalConst.RETURN_OK;

class UserServiceTest {

    private TaskService taskService;

    private ProjectService projectService;

    private UserService userService;

    private ProjectTaskService projectTaskService;

    private Long user_id = 10000000L;

    private Long id = 2L;

    private String login = "NAME";

    private Long userIdAdmin = 1L;

    private final Application application = new Application();

    @BeforeEach
    void setUp() {
        taskService = TaskService.getInstance();
        projectService = ProjectService.getInstance();
        userService = UserService.getInstance();
        projectTaskService = ProjectTaskService.getInstance();
    }

    @Test
    void writeUserJsonCorrect() {
        application.userIdCurrent = id;
        assertEquals(RETURN_OK,userService.writeUserJson());
    }

    @Test
    void writeUserJsonException() {
        assertEquals(RETURN_ERROR, userService.writeUserJson());
    }

    @Test
    void writeUserXMLCorrect() {
        application.userIdCurrent = id;
        assertEquals(RETURN_OK, userService.writeUserXML());
    }

    @Test
    void writeUserXMLException() {
        assertEquals(RETURN_ERROR,userService.writeUserXML());
    }

    @Test
    void readUserJsonCorrect() {
        application.userIdCurrent = userIdAdmin;;
        assertEquals(RETURN_OK, userService.readUserJson());
    }

    @Test //не работает
    void readUserJsonException() {
        assertEquals(RETURN_ERROR, userService.readUserJson());
        application.userIdCurrent = id;
        assertEquals(RETURN_ERROR, userService.readUserJson());
    }

    @Test
    void readUserXMLCorrect() {
        application.userIdCurrent = userIdAdmin;;
        assertEquals(RETURN_OK, userService.readUserXML());
    }

    @Test
    void readUserXMLException() {
        assertEquals(RETURN_ERROR, userService.readUserXML());
        application.userIdCurrent = id;
        assertEquals(RETURN_ERROR, userService.readUserXML());
    }

    @Test
    void findAllCorrect() {
        application.userIdCurrent = userIdAdmin;
        userService.createBeginUsers();
        assertNotNull(userService.findAll());
    }

    @Test
    void findByLoginCorrect() {
        assertEquals("TEST", userService.findByLogin("TEST").getLogin());
    }

    @Test
    void findByLoginException() {
        assertNull(userService.findByLogin(null));
        assertNull(userService.findByLogin("Неизвестный пользователь"));
    }

    @Test
    void findByUserIdCorrect() {
        assertEquals(2L, userService.findByUserId(2L).getUserid());
    }

    @Test
    void findByUserIdException() {
        assertNull(userService.findByUserId(100L));
        assertNull(userService.findByUserId(null));
    }

    @Test
    void createCorrect() {
        User user = userService.create(10L,login, HashCode.getHash("PASSWORD"), "Имя", "Фамилия",
                "Отчество", "mail@gmail.com", RoleEnum.USER, false);
        assertEquals(login, user.getLogin());
        User user2 = userService.create("LOGIN2");
        assertEquals("LOGIN2", user2.getLogin());
    }

    @Test
    void createException() {
        assertNull(userService.create(null));
        assertThrows(NullPointerException.class, () -> userService.create(10L,login, HashCode.getHash(null), "Имя", "Фамилия",
                "Отчество", "mail@gmail.com", RoleEnum.ADMIN, true));
        assertNull(userService.create(1L,login, HashCode.getHash("PASSWORD"), "Имя", "Фамилия",
                          "Отчество", "mail@gmail.com", RoleEnum.ADMIN, true));
        assertNull(userService.create(200L,"ADMIN", HashCode.getHash("PASSWORD"), "Имя", "Фамилия",
                "Отчество", "mail@gmail.com", RoleEnum.ADMIN, true));
    }

    @Test
    void removeByCorrect() {
        userService.removeByLogin("TEST");
        assertNull(userService.findByLogin("TEST"));
    }

    @Test
    void updateRoleCorrect() {
        User user = userService.create(10L,login, HashCode.getHash("PASSWORD"), "Имя", "Фамилия",
                "Отчество", "mail@gmail.com", RoleEnum.ADMIN, true);
        userService.updateRole(login, "USER");
        assertEquals(RoleEnum.USER, user.getRole());
    }

    @Test
    void updateRoleException() {
        User user = userService.create(10L,login, HashCode.getHash("PASSWORD"), "Имя", "Фамилия",
                "Отчество", "mail@gmail.com", RoleEnum.ADMIN, true);
        assertNull(userService.updateRole(null, "USER"));
        assertNull(userService.updateRole(login, null));
    }

    @Test
    void updateProfileCorrect() {
        User user = userService.create(10L,"LOGIN1", HashCode.getHash("PASSWORD"), "Имя1", "Фамилия1",
                "Отчество1", "mail1@gmail.com", RoleEnum.ADMIN, true);
        userService.updateProfile(10L, "LOGIN2", "Имя2", "Фамилия2",
                "Отчество2", "mail2@gmail.com");
        assertEquals("LOGIN2", user.getLogin());
    }

    @Test
    void updateProfileException() {
        User user = userService.create(10L,"LOGIN1", HashCode.getHash("PASSWORD"), "Имя1", "Фамилия1",
                "Отчество1", "mail1@gmail.com", RoleEnum.ADMIN, true);
        assertNull(userService.updateProfile(10L, null, "Имя2", "Фамилия2",
                "Отчество2", "mail2@gmail.com"));
    }

    @Test
    void changePasswordCorrect() {
        User user = userService.create(10L,"LOGIN1", HashCode.getHash("PASSWORD1"), "Имя1", "Фамилия1",
                "Отчество1", "mail1@gmail.com", RoleEnum.ADMIN, true);
        userService.changePassword(user.getUserid(), "PASSWORD2");
        assertEquals(HashCode.getHash("PASSWORD2"), user.getPassword());
    }

    @Test
    void changePasswordException() {
        User user = userService.create(10L,"LOGIN1", HashCode.getHash("PASSWORD1"), "Имя1", "Фамилия1",
                "Отчество1", "mail1@gmail.com", RoleEnum.ADMIN, true);
        assertNull(userService.changePassword(user.getUserid(), null));
        assertNull(userService.changePassword(null, "PASSWORD2"));
    }

    @Test
    void listUserException() {
        assertEquals(RETURN_ERROR, userService.listUser(1));
        application.userIdCurrent = id;
        assertEquals(0, userService.listUser(5));
    }

    @Test
    void removeUserByLoginException() {
        assertEquals(RETURN_ERROR, userService.removeUserByLogin(null));
        application.userIdCurrent = 2L;
        assertEquals(RETURN_ERROR, userService.removeUserByLogin(3L));
    }

    @Test
    void updateUserRoleException() {
        assertEquals(RETURN_ERROR, userService.updateUserRole(null));
        application.userIdCurrent = 2L;
        assertEquals(RETURN_ERROR, userService.updateUserRole(3L));
    }

    @Test
    void userProfileCorrect() {
        assertEquals(RETURN_OK, userService.userProfile(userIdAdmin));
    }

    @Test
    void userProfileException() {
        assertEquals(RETURN_ERROR, userService.userProfile(null));
        assertThrows(NullPointerException.class,() -> userService.userProfile(100L));
    }

    @Test
    void exitUserCorrect() {
        application.userIdCurrent = 2L;
        assertEquals(RETURN_OK, userService.exitUser());
        assertNull(application.userIdCurrent);
    }

    @Test
    void createBeginUsers() {
        userService.createBeginUsers();
        assertTrue(!userService.findAll().isEmpty());
    }

}