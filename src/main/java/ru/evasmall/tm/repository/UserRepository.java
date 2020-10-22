package ru.evasmall.tm.repository;

import ru.evasmall.tm.entity.User;
import ru.evasmall.tm.enumerated.RoleEnum;
import ru.evasmall.tm.util.HashCode;

public class UserRepository extends AbstractRepository<User> {

    private static UserRepository instance = null;

    public static UserRepository getInstance() {
        if (instance == null) {
            synchronized(UserRepository.class) {
                if (instance == null)
                    instance = new UserRepository();
            }
        }
        return instance;
    }

    public String getObjectName(final User user) {
        if (user == null) return null;
        return user.getLogin();
    }

    public User findByLogin(final String login) {
        for (final User user: objects) {
            if(user.getLogin().equals(login)) return user;
        }
        return null;
    }

    public User findById(final Long userId) {
        for (final User user: objects) {
            if(user.getUserid().equals(userId)) return user;
        }
        return null;
    }

    public User create(final String login) {
        final User user = new User(login);
        objects.add(user);
        return user;
    }

    public User create(final Long userid, String login, String password, String firstname, String lastname,
                       String middlname, String email, RoleEnum role, boolean adminTrue) {
        final User user = new User(login);
        user.setUserid(userid);
        user.setLogin(login);
        user.setPassword(password);
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setMiddlname(middlname);
        user.setEmail(email);
        user.setRole(role);
        user.setAdmin(adminTrue);
        objects.add(user);
        return user;
    }

    public User removeByLogin (final String login) {
        final User user = findByLogin(login);
        if (user == null) return null;
        objects.remove(user);
        return user;
    }

    public User updateRole(final String login, String role) {
        final User user = findByLogin(login);
        if (user == null) return null;
        switch (role) {
            case "ADMIN":
                user.setRole(RoleEnum.ADMIN); break;
            case "USER":
                user.setRole(RoleEnum.USER); break;
            default: user.setRole(RoleEnum.USER); break;

        }
        return user;
    }

    public User updateProfile(final Long userId, String login, String firstname, String middlname, String lastname, String email) {
        final User user = findById(userId);
        if (user == null) return null;
        user.setLogin(login);
        user.setFirstname(firstname);
        user.setMiddlname(middlname);
        user.setLastname(lastname);
        user.setEmail(email);
        return user;
    }

    public User changePassword(final Long userId, String password) {
        final User user = findById(userId);
        if (user == null) return null;
        user.setPassword(HashCode.getHash(password));
        return user;
    }

}