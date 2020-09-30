package ru.evasmall.tm.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.evasmall.tm.Application;
import ru.evasmall.tm.entity.User;
import ru.evasmall.tm.enumerated.RoleEnum;
import ru.evasmall.tm.repository.UserRepository;
import ru.evasmall.tm.util.HashCode;

import java.util.Collections;
import java.util.List;

public class UserService extends AbstractService {

    private final UserRepository userRepository;

    private static final Logger logger = LogManager.getLogger(UserService.class);

    private final SystemService systemService = new SystemService();

    private static UserService instance = null;

    public UserService() {
        this.userRepository = UserRepository.getInstance();
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    //Поиск всех пользователей.
    public List<User> findAll() {
        return userRepository.findAll();
    }

    //Поиск пользователя по логину.
    public User findByLogin(String login) {
        if (login == null || login.isEmpty()) return null;
        return userRepository.findByLogin(login);
    }

    //Поиск пользователя по идентификатору.
    public User findByUserId(Long userId) {
        if (userId == null) return null;
        return userRepository.findById(userId);
    }

    //Создание пользователя.
    public User create(String login) {
        if (login == null || login.isEmpty()) return null;
        return userRepository.create(login);
    }

    //Создание пользователя.
    public User create(final Long userid, String login, String password, String firstname, String lastname,
                       String middlname, String email, RoleEnum role, boolean adminTrue) {
        if (login == null || login.isEmpty()) return null;
        if (password == null || password.isEmpty()) return null;
        return userRepository.create(userid, login, password, firstname, lastname, middlname, email, role, adminTrue);
    }

    //Создание пользователя по логину.
    public User removeByLogin(String login) {
        if (login == null || login.isEmpty()) return null;
        return userRepository.removeByLogin(login);
    }

    //Изменение роли пользователя.
    public User updateRole(String login, String role) {
        if (login == null || login.isEmpty()) return null;
        if (role == null || role.isEmpty()) return null;
        return userRepository.updateRole(login, role);
    }

    //Изменение профиля пользователя.
    public User updateProfile(Long userId, String login, String firstname, String middlname, String lastname, String email) {
        if (login == null || login.isEmpty()) return null;
        return userRepository.updateProfile(userId, login, firstname, middlname, lastname, email);
    }

    //Изменение пароля пользователя.
    public User changePassword(Long userId, String password) {
        if (userId == null) return null;
        if (password == null || password.isEmpty()) return null;
        return userRepository.changePassword(userId, password);
    }

    //Сортировка пользователей по логину.
    public List<User> userSortByLogin(List<User> users) {
        Collections.sort(users, User.UserSortByLogin);
        return users;
    }

    //Сортировка пользователей по фамилии, имени, отчеству.
    public List<User> userSortByFIO(List<User> users) {
        Collections.sort(users, User.UserSortByFIO);
        return users;
    }

    //Регистрация пользователя.
    public int createUser() {
        System.out.println("USER REGISTRATION");
        final String login = isLoginExists();
        if (login == null) { return -1; }
        else {
            System.out.println("PLEASE ENTER YOUR FIRSTNAME:");
            final String firstname = scanner.nextLine();
            System.out.println("PLEASE ENTER YOUR LASTNAME:");
            final String lastname = scanner.nextLine();
            System.out.println("PLEASE ENTER YOUR MIDDLNAME:");
            final String middlname = scanner.nextLine();
            System.out.println("PLEASE ENTER EMAIL:");
            final String email = scanner.nextLine();
            System.out.println("PLEASE ENTER PASSWORD:");
            final String password = HashCode.getHash(scanner.nextLine());
            //Проверка на пустой пароль
            if (password == null || password.isEmpty()) {
                logger.info("PASSWORD MAST NOT BE EMPTY!");
                logger.info("FAIL");
                return -1;
            }
            //По умолчанию при регистрации присваивается роль USER. Изменить роль может только администратор.
            final RoleEnum role = RoleEnum.USER;
            Long userid = System.nanoTime();
            boolean adminTrue = false;
            create(userid, login, password, firstname, lastname, middlname, email, role, adminTrue);
            logger.trace("USER REGISTRATION: USERID = {} LOGIN = {} FIRSTNAME = {} LASTNAME = {} MIDDLNAME = {} EMAIL = {}", userid, login, firstname, lastname, middlname, email );
            System.out.println("REGISTRATION COMPLETED SUCCESSFULLY.");
            return 0;
        }
    }

    //Просмотр пользователей.
    public int listUser (int sort) {
        System.out.println("LIST USER");
        viewUsers(findAll(), sort);
        System.out.println("OK");
        return 0;
    }

    //Просмотр списка пользователей.
    public void viewUsers (final List<User> users, int sort) {
        if (users == null || users.isEmpty()) return;
        int index = 1;
        //Параметр 1 - сортировка по логинам, 2 - сортировка по Фамилии, имени, отчеству.
        if (sort == 1) userSortByLogin(users);
        if (sort == 2) userSortByFIO(users);
        for (final User user: users) {
            System.out.println(index + ". ID: " + user.getUserid() +" LOGIN: " + user.getLogin() + "; LASTNAME: " + user.getLastname() +
                    "; FIRSTNAME: " + user.getFirstname() + "; MIDDLNAME: " + user.getMiddlname() +
                    "; EMAIL: " + user.getEmail() + "; ROLE: " + user.getRole().name() +
                    "; PASSWORD: " + user.getPassword() + "; ADMIN: " + user.isAdminTrue());
            index++;
        }
    }

    //Удаление пользователя по логину (доступно только администраторам).
    public int removeUserByLogin(Long userId) {
        System.out.println("REMOVE USER BY LOGIN");
        if (findByUserId(userId).isAdminTrue()) {
            System.out.println(findByUserId(userId).isAdminTrue());
            System.out.println("PLEASE ENTER LOGIN OF REMOVE USER:");
            final String login = scanner.nextLine();
            removeByLogin(login);
            if (login == null) System.out.println("FAIL");
            logger.trace("USER DELETED: LOGIN = {}", login);
            System.out.println("USER DELETED.");
            return 0;
        }
        else {
            systemService.displayForAdminOnly();
            return -1;
        }
    }

    //Изменение ролей пользователя по логину (доступно только администраторам).
    public int updateUserRole(Long userId) {
        System.out.println("[UPDATE USER DATA]");
        if (findByUserId(userId).isAdminTrue()) {
            System.out.println("ENTER UPDATE USER LOGIN:");
            final String login = scanner.nextLine();
            final User user = findByLogin(login);
            if (user == null) {
                System.out.println("FAIL");
                return 0;
            }
            System.out.println("PLEASE ENTER ROLE: ADMIN OR USER");
            final String role = scanner.nextLine();
            if (role.equals("ADMIN") || role.equals("USER")) {
                updateRole(login, role);
                logger.trace("USER ROLE: LOGIN = {} ROLE = {}", login, role);
                System.out.println("OK");
                return 0;
            }
            else {
                System.out.println("UNKNOWN ROLE!");
                return 0;
            }
        }
        else {
            systemService.displayForAdminOnly();
            return -1;
        }
    }

    //Аутентификация пользователя.
    public Long authentication() {
        //Проверка на существование логина
        System.out.println("PLEASE ENTER YOUR LOGIN:");
        final String login = scanner.nextLine();
        if (findByLogin(login) == null) {
            System.out.println("THIS LOGIN NOT EXISTS!");
            System.out.println("FAIL");
            throw new IllegalArgumentException("THIS LOGIN NOT EXISTS!");
        }
        //Проверка пароля
        System.out.println("PLEASE ENTER YOUR PASSWORD:");
        final String password_admin = scanner.nextLine();
        if (HashCode.getHash(password_admin).equals(findByLogin(login).getPassword())) {
            return findByLogin(login).getUserid();
        }
        else {
            System.out.println("INCORRECT PASSWORD!");
            return null;
        }
    }

    //Просмотр профиля текущего пользователя.
    public int userProfile(final Long userId) {
        if (userId == null) return -1;
        System.out.println("CURRENT SESSION:");
        System.out.println("ID:" + userId.toString());
        System.out.println("LOGIN: " + findByUserId(userId).getLogin());
        System.out.println("FIRSTNAME:" + findByUserId(userId).getFirstname());
        System.out.println("MIDDLNAME:" + findByUserId(userId).getMiddlname());
        System.out.println("LASTNAME:" + findByUserId(userId).getLastname());
        System.out.println("EMAIL:" + findByUserId(userId).getEmail());
        System.out.println("ROLE:" + findByUserId(userId).getRole().name());
        return 0;
    }

    //Изменение профиля текущего пользователя.
    public int updateProfile(final Long userId) {
        final String login = isLoginExists();
        if (login == null) {
            return -1;
        }
        else {
            System.out.println("PLEASE ENTER YOUR NEW FIRSTNAME:");
            final String firstname = scanner.nextLine();
            System.out.println("PLEASE ENTER YOUR NEW MIDDLNAME:");
            final String middlname = scanner.nextLine();
            System.out.println("PLEASE ENTER YOUR NEW LASTNAME:");
            final String lastname = scanner.nextLine();
            System.out.println("PLEASE ENTER YOUR NEW EMAIL:");
            final String email = scanner.nextLine();
            updateProfile(userId, login, firstname, middlname, lastname, email);
            logger.trace("PROFILE UPDATED. LOGIN = {}", login);
            System.out.println("PROFILE UPDATED.");
            userProfile(userId);
            return 0;
        }
    }

    //Изменение пароля пользователя.
    public int changePassword(Long userIdCurrent) {
        System.out.println("PLEASE ENTER LOGIN:");
        final String login1 = scanner.nextLine();
        final User user1 = findByLogin(login1);
        if (user1 == null) {
            System.out.println("LOGIN NOT EXIST!");
            return -1;
        }
        //Проверка логина текущего пользователя или на права администратора
        if (user1.getUserid().equals(userIdCurrent) || findByUserId(userIdCurrent).isAdminTrue()) {
            System.out.println("PLEASE ENTER NEW PASSWORD:");
            final String password1 = scanner.nextLine();
            //Проверка на пустой пароль
            if (password1 == null || password1.isEmpty()) {
                System.out.println("PASSWORD NOT BE EMPTY!");
                System.out.println("FAIL");
                return -1;
            }
            //Проверка на подтверждение пароля
            System.out.println("CONFIRM PASSWORD:");
            final String password2 = scanner.nextLine();
            if (password1.equals(password2)) {
                changePassword(user1.getUserid(), password1);
                logger.trace("PASSWORD CHANGE. LOGIN: {} ", user1.getLogin());
                System.out.println("PASSWORD CHANGE OK.");
                return 0;
            }
            else {
                System.out.println("CONFIRM PASSWORD INCORRECT!");
                return -1;
            }
        }
        else System.out.println("NO ACCESS FOR PASSWORD CHANGE! ERROR!.");
        return -1;
    }

    //Проверка на существование логина.
    public String isLoginExists() {
        System.out.println("PLEASE ENTER YOUR NEW LOGIN:");
        final String login = scanner.nextLine();
        if (login == null || login.isEmpty()) {
            System.out.println("LOGIN MAST NOT BE EMPTY!");
            System.out.println("FAIL");
            return null;
        }
        //Проверка на существующие идентичные логины.
        if (findByLogin(login) != null) {
            System.out.println("THIS LOGIN EXISTS!");
            System.out.println("FAIL");
            return null;
        }
        return login;
    }

    //Окончание сессии текущего пользователя.
    public int exitUser() {
        logger.trace("SESSION ENDED. LOGIN: {}", findByUserId(Application.userIdCurrent).getLogin());
        Application.userIdCurrent = null;
        Application.history.clear();
        System.out.println("YOUR SESSION ENDED.");
        return 0;
    }

    //Команда аутентификации пользователя.
    public int signUser() {
        Application.userIdCurrent = authentication();
        userProfile(Application.userIdCurrent);
        logger.trace("SESSION BEGIN. LOGIN: {}", findByUserId(Application.userIdCurrent).getLogin());
        return 0;
    }

}
