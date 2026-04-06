package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findOne(int id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.orElse(null);
    }

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void update(int id, User updatedUser) {
        User oldUser = userRepository.findById(id).orElseThrow();
        oldUser.setUsername(updatedUser.getUsername());
        oldUser.setRoles(updatedUser.getRoles());

        if (updatedUser.getPassword() == null || updatedUser.getPassword().isEmpty()) {
            oldUser.setPassword(oldUser.getPassword());
        } else {
            oldUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userRepository.save(oldUser);
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public UserDTO convertToDTO(User user) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet());
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setRoles(roleNames);
        return userDTO;
    }

    public List<UserDTO> convertToDTOList(List<User> userList) {
        return userList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public User convertToUser(UserDTO userDTO) {
        Set<Role> roleNames = userDTO.getRoles().stream()
                .map(roleService::findByRoleName)
                .collect(Collectors.toSet());
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setRoles(roleNames);
        return user;
    }

}

