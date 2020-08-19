package ru.evasmall.tm.controller;

import ru.evasmall.tm.entity.User;
import ru.evasmall.tm.enumerated.RoleEnum;
import ru.evasmall.tm.util.HashCode;
import ru.evasmall.tm.service.UserService;
import ru.evasmall.tm.Application;

import java.util.List;

public class UserController extends AbstractController {

    private final UserService userService;

    private final SystemController systemController = new SystemController();

    public UserController(UserService userService) {
        this.userService = userService;
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
                System.out.println("PASSWORD MAST NOT BE EMPTY!");
                System.out.println("FAIL");
                return -1;
            };
            //По умолчанию при регистрации присваивается роль USER. Изменить роль может только администратор.
            final RoleEnum role = RoleEnum.USER;
            Long userid = System.nanoTime();
            boolean admin_true = false;
            userService.create(userid, login, password, firstname, lastname, middlname, email, role, admin_true);
            System.out.println("OK");
            return 0;
        }
    }

    //Просмотр пользователей.
    public int listUser (int sort) {
        System.out.println("LIST USER");
        int index = 1;
        viewUsers(userService.findAll(), sort);
        System.out.println("OK");
        return 0;
    }

    //Просмотр списка пользователей.
    public void viewUsers (final List<User> users, int sort) {
        if (users == null || users.isEmpty()) return;
        int index = 1;
        //Параметр 1 - сортировка по логинам, 2 - сортировка по Фамилии, имени, отчеству.
        if (sort == 1) userService.UserSortByLogin(users);
        if (sort == 2) userService.UserSortByFIO(users);
        for (final User user: users) {
            System.out.println(index + ". ID: " + user.getUserid() +" LOGIN: " + user.getLogin() + "; LASTNAME: " + user.getLastname() +
            "; FIRSTNAME: " + user.getFirstname() + "; MIDDLNAME: " + user.getMiddlname() +
            "; EMAIL: " + user.getEmail() + "; ROLE: " + user.getRole().name() +
            "; PASSWORD: " + user.getPassword() + "; ADMIN: " + user.isAdmin_true());
            index++;
        }
    }

    //Удаление пользователя по логину (доступно только администраторам).
    public int removeUserByLogin(Long userId) {
        System.out.println("REMOVE USER BY LOGIN");
        if (userService.findByUserId(userId).isAdmin_true() == true) {
            System.out.println(userService.findByUserId(userId).isAdmin_true());
            System.out.println("PLEASE ENTER LOGIN OF REMOVE USER:");
            final String login = scanner.nextLine();
            final User user = userService.removeByLogin(login);
            if (login == null) System.out.println("FAIL");
            else System.out.println("OK");
            return 0;
        }
        else {
            systemController.displayForAdminOnly();
            return -1;
        }
    }

    //Изменение ролей пользователя по логину (доступно только администраторам).
    public int updateUserRole(Long userId) {
        System.out.println("[UPDATE USER DATA]");
        if (userService.findByUserId(userId).isAdmin_true() == true) {
            System.out.println("ENTER UPDATE USER LOGIN:");
            final String login = scanner.nextLine();
            final User user = userService.findByLogin(login);
            if (user == null) {
                System.out.println("FAIL");
                return 0;
            }
            System.out.println("PLEASE ENTER ROLE: ADMIN OR USER");
            final String role = scanner.nextLine();
            if (role.equals("ADMIN") || role.equals("USER")) {
                userService.updateRole(login, role);
                System.out.println("OK");
                return 0;
            }
            else {
                System.out.println("UNKNOWN ROLE!");
                return 0;
            }
        }
        else {
            systemController.displayForAdminOnly();
            return -1;
        }
    }

    //Аутентификация пользователя.
    public Long authentication() {
        //Проверка на существование логина
        System.out.println("PLEASE ENTER YOUR LOGIN:");
        final String login = scanner.nextLine();
        if (userService.findByLogin(login) == null) {
            System.out.println("THIS LOGIN NOT EXISTS!");
            System.out.println("FAIL");
            return null;
        }
        //Проверка пароля
        System.out.println("PLEASE ENTER YOUR PASSWORD:");
        final String password_admin = scanner.nextLine();
        if (HashCode.getHash(password_admin).equals(userService.findByLogin(login).getPassword())) {
            return userService.findByLogin(login).getUserid();
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
        System.out.println("LOGIN: " + userService.findByUserId(userId).getLogin());
        System.out.println("FIRSTNAME:" + userService.findByUserId(userId).getFirstname());
        System.out.println("MIDDLNAME:" + userService.findByUserId(userId).getMiddlname());
        System.out.println("LASTNAME:" + userService.findByUserId(userId).getLastname());
        System.out.println("EMAIL:" + userService.findByUserId(userId).getEmail());
        System.out.println("ROLE:" + userService.findByUserId(userId).getRole().name());
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
            userService.updateProfile(userId, login, firstname, middlname, lastname, email);
            System.out.println("PROFILE UPDATED:");
            userProfile(userId);
            return 0;
        }
    }

    //Изменение пароля текущего пользователя.
    public int changePassword(Long userIdCurrent) {
        System.out.println("PLEASE ENTER YOUR NEW PASSWORD:");
        System.out.println("PLEASE ENTER PASSWORD:");
        final String password1 = scanner.nextLine();
        //Проверка на пустой пароль
        if (password1 == null || password1.isEmpty()) {
            System.out.println("PASSWORD MAST NOT BE EMPTY!");
            System.out.println("FAIL]");
            return -1;
        };
        //Проверка на подтверждение пароля
        System.out.println("CONFIRM PASSWORD:");
        final String password2 = scanner.nextLine();
        if (password1.equals(password2)) {
            userService.changePassword(userIdCurrent, password1);
            System.out.println("PASSWORD CHANGE OK.");
            return 0;
        }
        else {
            System.out.println("PASSWORD INCORRECT!:");
            return -1;
        }
    }

    //Проверка на существование логина.
    public String isLoginExists() {
        System.out.println("PLEASE ENTER YOUR NEW LOGIN:");
        final String login = scanner.nextLine();
        if (login == null || login.isEmpty()) {
            System.out.println("LOGIN MAST NOT BE EMPTY!");
            System.out.println("FAIL");
            return null;
        };
        //Проверка на существующие идентичные логины.
        if (userService.findByLogin(login) != null) {
            System.out.println("THIS LOGIN EXISTS!");
            System.out.println("FAIL");
            return null;
        };
        return login;
    }

    //Окончание сессии текущего пользователя.
    public int exitUser() {
        Application.userIdCurrent = null;
        Application.history.clear();
        System.out.println("YOUR SESSION ENDED.");
        return 0;
    }

    //Команда аутентификации пользователя.
    public int signUser() {
        Application.userIdCurrent = authentication();
        userProfile(Application.userIdCurrent);
        return 0;
    }

}
