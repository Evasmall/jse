package ru.evasmall.tm.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.evasmall.tm.Application;
import ru.evasmall.tm.constant.TerminalMassage;
import ru.evasmall.tm.entity.User;
import ru.evasmall.tm.enumerated.RoleEnum;
import ru.evasmall.tm.repository.UserRepository;
import ru.evasmall.tm.util.HashCode;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static ru.evasmall.tm.constant.FileNameConst.USER_JSON;
import static ru.evasmall.tm.constant.FileNameConst.USER_XML;
import static ru.evasmall.tm.constant.TerminalConst.RETURN_ERROR;
import static ru.evasmall.tm.constant.TerminalConst.RETURN_OK;
import static ru.evasmall.tm.constant.TerminalMassage.UNAUTHORIZED_USER;

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

    /**
     * Запись всех пользователей в файл формата JSON.
     */
    public int writeUserJson() {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        userRepository.writeJson(USER_JSON);
        System.out.println("USERS " + TerminalMassage.DATA_WRITTEN_FILES);
        return RETURN_OK;
    }

    /**
     * Запись всех пользователей в файл формата XML.
     */
    public int writeUserXML() {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        userRepository.writeXML(USER_XML);
        System.out.println("USERS " + TerminalMassage.DATA_WRITTEN_FILES);
        return RETURN_OK;
    }

    /**
     * Чтение и перезапись всех пользователей из файла формата JSON (только для администраторов).
     */
    public int readUserJson() {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        if (findByUserId(Application.userIdCurrent).get().isAdmin()) {
            userRepository.readJson(USER_JSON, User.class);
            System.out.println("USERS " + TerminalMassage.DATA_READ_FILES);
            return RETURN_OK;
        }
        systemService.displayForAdminOnly();
        return RETURN_ERROR;
    }

    /**
     * Чтение и перезапись всех пользователей из файла формата XML (только для администраторов).
     */
    public int readUserXML() {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        if (findByUserId(Application.userIdCurrent).get().isAdmin()) {
            userRepository.readXML(USER_XML, User.class);
            System.out.println("USERS " + TerminalMassage.DATA_READ_FILES);
            return RETURN_OK;
        }
        systemService.displayForAdminOnly();
        return RETURN_ERROR;
    }

    /**
     * Поиск всех пользователей.
     */
    public Optional<List<User>> findAll() {
        return userRepository.findAll();
    }

    /**
     * Поиск пользователя по логину.
     * @param login Логин
     * @return Пользователь
     */
    public Optional<User> findByLogin(String login) {
        if (login == null || login.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(userRepository.findByLogin(login));
    }

    /**
     * Поиск пользователя по идентификатору.
     * @param userId идентификатор пользователя
     * @return Пользователь
     */
    public Optional<User> findByUserId(Long userId) {
        if (userId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(userRepository.findById(userId));
    }

    /**
     * Создание пользователя.
     * @param login Логин
     * @return Пользователь
     */
    public Optional<User> create(String login) {
        if (login == null || login.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(userRepository.create(login));
    }

    /**
     * Создание пользователя.
     * @param userid Идентификатор пользователя
     * @param login Логин
     * @param password Пароль
     * @param firstname Имя
     * @param lastname Фамилия
     * @param middlname Отчество
     * @param email Электронная почта
     * @param role Роль
     * @param isAdmin Признак администратора
     * @return Пользователь
     */
    //
    public Optional<User> create(final Long userid, String login, String password, String firstname, String lastname,
                       String middlname, String email, RoleEnum role, boolean isAdmin) {
        try {
            userRepository.findById(userid).getUserid();
            logger.error("LOGIN WITH THIS ID EXISTS. ERROR. ");
            return Optional.empty();
        } catch (Exception e) {
            try {
                userRepository.findByLogin(login).getLogin();
                logger.error("LOGIN WITH THIS NAME EXISTS. ERROR. ");
                return null;
            } catch (Exception e1) {
                if (login == null || login.isEmpty()) {
                    return Optional.empty();
                }
                if (password == null || password.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.ofNullable(userRepository.create(userid, login, password, firstname, lastname, middlname, email, role, isAdmin));
            }
        }
    }

    /**
     * Создание пользователя по логину.
     * @param login Логин
     * @return Пользователь
     */
    public Optional<User> removeByLogin(String login) {
        if (login == null || login.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(userRepository.removeByLogin(login));
    }

    /**
     * Изменение роли пользователя.
     * @param login Логин
     * @param role Роль
     * @return Пользователь
     */
    public Optional<User> updateRole(String login, String role) {
        if (login == null || login.isEmpty()) {
            return Optional.empty();
        }
        if (role == null || role.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(userRepository.updateRole(login, role));
    }

    /**
     * Изменение профиля пользователя.
     * @param userId Идентификатор пользователя
     * @param login Логин
     * @param firstname Имя
     * @param middlname Отчество
     * @param lastname Фамилия
     * @param email Электронная почта
     * @return Пользователь
     */
    public Optional<User> updateProfile(Long userId, String login, String firstname, String middlname, String lastname, String email) {
        if (login == null || login.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(userRepository.updateProfile(userId, login, firstname, middlname, lastname, email));
    }

    /**
     * Изменение пароля пользователя.
     * @param userId Идентификатор пользователя
     * @param password Пароль
     * @return Пользователь
     */
    public Optional<User> changePassword(Long userId, String password) {
        if (userId == null) {
            return Optional.empty();
        }
        if (password == null || password.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(userRepository.changePassword(userId, password));
    }

    /**
     * Регистрация пользователя.
     */
    public int createUser() {
        System.out.println("USER REGISTRATION");
        final String login = isLoginExists();
        if (login == null) { return RETURN_ERROR; }
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
                return RETURN_ERROR;
            }
            //По умолчанию при регистрации присваивается роль USER. Изменить роль может только администратор.
            final RoleEnum role = RoleEnum.USER;
            Long userid = System.nanoTime();
            boolean isAdmin = false;
            create(userid, login, password, firstname, lastname, middlname, email, role, isAdmin);
            logger.trace("USER REGISTRATION: USERID = {} LOGIN = {} FIRSTNAME = {} LASTNAME = {} MIDDLNAME = {} EMAIL = {}", userid, login, firstname, lastname, middlname, email );
            System.out.println("REGISTRATION COMPLETED SUCCESSFULLY.");
            return RETURN_OK;
        }
    }

    /**
     * Просмотр пользователей.
     * @param sort 1 - сортировка по логинам, 2 - сортировка по Фамилии, имени, отчеству.
     */
    public int listUser (int sort) {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        else {
            System.out.println("LIST USER");
            viewUsers(findAll().get(), sort);
            System.out.println("OK");
            return RETURN_OK;
        }
    }

    /**
     * Просмотр списка пользователей.
     * @param users Пользователи
     * @param sort 1 - сортировка по логинам, 2 - сортировка по Фамилии, имени, отчеству.
     */
    public void viewUsers (final List<User> users, int sort) {
        if (users == null || users.isEmpty()) return;
        int index = 1;
        if (sort == 1) users.sort(Comparator.comparing(User::getLogin));
        if (sort == 2) users.sort(Comparator.comparing(User::getLastname).thenComparing(User::getFirstname).thenComparing(User::getMiddlname));
        for (final User user: users) {
            System.out.println(index + ". ID: " + user.getUserid() +" LOGIN: " + user.getLogin() + "; LASTNAME: " + user.getLastname() +
                    "; FIRSTNAME: " + user.getFirstname() + "; MIDDLNAME: " + user.getMiddlname() +
                    "; EMAIL: " + user.getEmail() + "; ROLE: " + user.getRole().name() +
                    "; PASSWORD: " + user.getPassword() + "; ADMIN: " + user.isAdmin());
            index++;
        }
    }

    /**
     * Удаление пользователя по логину (доступно только администраторам).
     * @param userId Идентификатор пользователя текущей сессии
     */
    public int removeUserByLogin(Long userId) {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        else {
            System.out.println("REMOVE USER BY LOGIN");
            if (findByUserId(userId).get().isAdmin()) {
                System.out.println(findByUserId(userId).get().isAdmin());
                System.out.println("PLEASE ENTER LOGIN OF REMOVE USER:");
                final String login = scanner.nextLine();
                removeByLogin(login);
                if (login == null) System.out.println("FAIL");
                logger.trace("USER DELETED: LOGIN = {}", login);
                System.out.println("USER DELETED.");
                return RETURN_OK;
            }
            else {
                systemService.displayForAdminOnly();
                return RETURN_ERROR;
            }
        }
    }

    /**
     * Изменение ролей пользователя по логину (доступно только администраторам).
     * @param userId идентификатор пользователя текущей сессии
     * @return
     */
    public int updateUserRole(Long userId) {
        if (Application.userIdCurrent == null) {
            System.out.println(UNAUTHORIZED_USER);
            return RETURN_ERROR;
        }
        else {
            System.out.println("UPDATE USER DATA");
            if (findByUserId(userId).get().isAdmin()) {
                System.out.println("ENTER UPDATE USER LOGIN:");
                final String login = scanner.nextLine();
                final User user = findByLogin(login).get();
                if (user == null) {
                    System.out.println("FAIL");
                    return RETURN_ERROR;
                }
                System.out.println("PLEASE ENTER ROLE: ADMIN OR USER");
                final String role = scanner.nextLine();
                if (role.equals("ADMIN") || role.equals("USER")) {
                    updateRole(login, role);
                    logger.trace("USER ROLE: LOGIN = {} ROLE = {}", login, role);
                    System.out.println("OK");
                    return RETURN_OK;
                }
                else {
                    System.out.println("UNKNOWN ROLE!");
                    return RETURN_ERROR;
                }
            }
            else {
                systemService.displayForAdminOnly();
                return RETURN_ERROR;
            }
        }
    }

    /**
     * Аутентификация пользователя.
     * @return идентификатор авторизированного пользователя
     */
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
        if (HashCode.getHash(password_admin).equals(findByLogin(login).get().getPassword())) {
            return findByLogin(login).get().getUserid();
        }
        else {
            System.out.println("INCORRECT PASSWORD!");
            return null;
        }
    }

    /**
     * Просмотр профиля текущего пользователя.
     * @param userId Идентификатор пользоваеля
     */
    public int userProfile(final Long userId) {
        if (userId == null) return RETURN_ERROR;
        System.out.println("CURRENT SESSION:");
        System.out.println("ID:" + userId.toString());
        System.out.println("LOGIN: " + findByUserId(userId).get().getLogin());
        System.out.println("FIRSTNAME:" + findByUserId(userId).get().getFirstname());
        System.out.println("MIDDLNAME:" + findByUserId(userId).get().getMiddlname());
        System.out.println("LASTNAME:" + findByUserId(userId).get().getLastname());
        System.out.println("EMAIL:" + findByUserId(userId).get().getEmail());
        System.out.println("ROLE:" + findByUserId(userId).get().getRole().name());
        return RETURN_OK;
    }

    /**
     * Изменение профиля текущего пользователя.
     * @param userId идентификатор пользователя
     */
    public int updateProfile(final Long userId) {
        final String login = isLoginExists();
        if (login == null) {
            return RETURN_ERROR;
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
            return RETURN_OK;
        }
    }

    /**
     * Изменение пароля пользователя.
     * @param userIdCurrent Идентификатор текущего пользователя
     */
    public int changePassword(Long userIdCurrent) {
        System.out.println("PLEASE ENTER LOGIN:");
        final String login1 = scanner.nextLine();
        final User user1 = findByLogin(login1).get();
        if (user1 == null) {
            System.out.println("LOGIN NOT EXIST!");
            return RETURN_ERROR;
        }
        //Проверка логина текущего пользователя или на права администратора
        if (user1.getUserid().equals(userIdCurrent) || findByUserId(userIdCurrent).get().isAdmin()) {
            System.out.println("PLEASE ENTER NEW PASSWORD:");
            final String password1 = scanner.nextLine();
            //Проверка на пустой пароль
            if (password1 == null || password1.isEmpty()) {
                System.out.println("PASSWORD NOT BE EMPTY!");
                System.out.println("FAIL");
                return RETURN_ERROR;
            }
            //Проверка на подтверждение пароля
            System.out.println("CONFIRM PASSWORD:");
            final String password2 = scanner.nextLine();
            if (password1.equals(password2)) {
                changePassword(user1.getUserid(), password1);
                logger.trace("PASSWORD CHANGE. LOGIN: {} ", user1.getLogin());
                System.out.println("PASSWORD CHANGE OK.");
                return RETURN_OK;
            }
            else {
                System.out.println("CONFIRM PASSWORD INCORRECT!");
                return RETURN_ERROR;
            }
        }
        else System.out.println("NO ACCESS FOR PASSWORD CHANGE! ERROR!.");
        return RETURN_ERROR;
    }

    /**
     * Проверка на существование логина.
     * @return существующий логин
     */
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

    /**
     * Окончание сессии текущего пользователя.
     */
    public int exitUser() {
        logger.trace("SESSION ENDED. LOGIN: {}", findByUserId(Application.userIdCurrent).get().getLogin());
        Application.userIdCurrent = null;
        Application.history.clear();
        System.out.println("YOUR SESSION ENDED.");
        return RETURN_OK;
    }

    /**
     * Команда аутентификации пользователя.
     */
    public int signUser() {
        Application.userIdCurrent = authentication();
        userProfile(Application.userIdCurrent);
        logger.trace("SESSION BEGIN. LOGIN: {}", findByUserId(Application.userIdCurrent).get().getLogin());
        return RETURN_OK;
    }

    /**
     * Создание начальной базы данных пользователей.
     */
    public void createBeginUsers() {
        UserService u = UserService.getInstance();
        u.create(1L,"ADMIN", HashCode.getHash("POBEDA"), "Василий", "Чапаев",
                "Иванович", "chapaev_vi@gmail.com", RoleEnum.ADMIN, true);
        u.create(2L,"TEST", HashCode.getHash("123"), "Пётр", "Исаев",
                "Семёнович", "isaev_ps@gmail.com", RoleEnum.USER, false);
        u.create(3L,"FF", HashCode.getHash("12345"), "Дмитрий", "Фурманов",
                "Андреевич", "furmanov_da@gmail.com", RoleEnum.USER, false);
    }

}
