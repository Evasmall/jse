package ru.evasmall.tm.entity;

import ru.evasmall.tm.enumerated.RoleEnum;

import java.util.Comparator;
import java.util.Objects;

public class User {

    private Long userid = System.nanoTime();

    private String login = "";

    private String password = "";

    private String firstname = "";

    private String lastname = "";

    private String middlname = "";

    private String email = "";

    private RoleEnum role;

    private boolean admin_true = false;

    public User() {}

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public User(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getMiddlname() {
        return middlname;
    }

    public void setMiddlname(String middlname) {
        this.middlname = middlname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public boolean isAdmin_true() {
        return admin_true;
    }

    public void setAdmin_true(boolean admin_true) {
        this.admin_true = admin_true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(login, user.login) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

    public static Comparator<User> UserSortByLogin = new Comparator<User>() {
        @Override
        public int compare(User u1, User u2) {
            return u1.getLogin().compareTo(u2.getLogin());
        }
    };

    public static Comparator<User> UserSortByFIO = new Comparator<User>() {
        @Override
        public int compare(User u1, User u2) {
            if (u1 == null) {
                return -1;
            }
            int fio = u1.getLastname().compareTo(u2.getLastname());
            if (fio !=0) return fio;

            fio = u1.getFirstname().compareTo(u2.getFirstname());
            if (fio !=0) return fio;

            fio = u1.getMiddlname().compareTo(u2.getMiddlname());
            return fio;
        }
    };

}
