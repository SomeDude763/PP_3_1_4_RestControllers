package ru.kata.spring.boot_security.demo.model;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    @NotBlank(message = "Имя не должно быть пустым, заправься борщом густым")
    @Size(min = 2, max = 50, message = "тут такое дело... " +
            "имя должно состоять как минимум из 2 и как максимум из 50 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "выйди и зайди заново, используя только буквы")
    private String name;

    @Column(name = "surname")
    @NotBlank(message = "Фамилия не должна быть пустой, сделай бутерброд с колбасой")
    @Size(min = 2, max = 50, message = "ну что ж такое то," +
            " фамилия должна состоять как минимум из 2 и как максимум из 50 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\-]+$", message = "зайди и выйди заново, используя только буквы")
    private String surname;

    @Min(value = 0, message = "Возраст не может быть меньше 0, за меня идиота не держи")
    @Max(value = 150, message = "Возраст слишком большой, сооберись")
    @Column(name = "age")
    private int age;

    @Column(name = "username")
    @NotBlank(message = "username не должен быть пустым, заправься борщом густым")
    @Size(min = 2, max = 50, message = "тут такое дело... " +
            "username должен состоять как минимум из 2 и как максимум из 50 символов")
    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$", message = "выйди и зайди заново, используя только буквы")
    private String username;
    private String password;
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roles;

    public User() {
    }

    public User(String name, String surname, int age, String username, String password, Set<Role> roles) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", age=" + age +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                '}';
    }


}



