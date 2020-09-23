package ru.evasmall.tm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.evasmall.tm.enumerated.RoleEnum;

import java.util.Comparator;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class User {

    private Long userid = System.nanoTime();

    private String login = "";

    private String password = "";

    private String firstname = "";

    private String lastname = "";

    private String middlname = "";

    private String email = "";

    private RoleEnum role;

    private boolean adminTrue = false;

    public User() {}
    public User(String login) {
        this.login = login;
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

    public final static Comparator<User> UserSortByLogin = new Comparator<User>() {
        @Override
        public int compare(User u1, User u2) {
            return u1.getLogin().compareTo(u2.getLogin());
        }
    };

    public final static Comparator<User> UserSortByFIO = new Comparator<User>() {
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
