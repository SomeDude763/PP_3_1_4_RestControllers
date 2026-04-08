package ru.kata.spring.boot_security.demo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

public class UserDTO {

    private int id;

    @NotBlank(message = "username не должен быть пустым, заправься борщом густым")
    @Size(min = 2, max = 50, message = "тут такое дело... " +
            "username должен состоять как минимум из 2 и как максимум из 50 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я0-9]+$", message = "выйди и зайди заново, используя только буквы")
    private String username;
    private String password;
    private Set<String> roles;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

}
