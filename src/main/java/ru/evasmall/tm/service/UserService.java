package ru.evasmall.tm.service;

import ru.evasmall.tm.entity.User;
import ru.evasmall.tm.enumerated.RoleEnum;
import ru.evasmall.tm.repository.UserRepository;

import java.util.Collections;
import java.util.List;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
                       String middlname, String email, RoleEnum role, boolean admin_true) {
        if (login == null || login.isEmpty()) return null;
        if (password == null || password.isEmpty()) return null;
        return userRepository.create(userid, login, password, firstname, lastname, middlname, email, role, admin_true);
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
    public List<User> UserSortByLogin(List<User> users) {
        Collections.sort(users, User.UserSortByLogin);
        return users;
    }

    //Сортировка пользователей по фамилии, имени, отчеству.
    public List<User> UserSortByFIO(List<User> users) {
        Collections.sort(users, User.UserSortByFIO);
        return users;
    }

}
