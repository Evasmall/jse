package ru.evasmall.tm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.evasmall.tm.enumerated.RoleEnum;

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

    private boolean isAdmin = false;

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

}
