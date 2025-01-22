package com.example.REST.controller;

import com.example.REST.DTO.UserDTO;
import com.example.REST.model.User;
import com.example.REST.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("rest")
public class SecurityController {
    private final UserService userService;

    @Autowired
    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public UserDTO userPage(User user) {
        return userService.parseToUserDTO(userService.findUserById(user.getId()));
    }

    @GetMapping("/admin")
    public List<User> getAllUsers() {

        return userService.allUsers();
    }

    @GetMapping("user/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }


    @PutMapping("/user")
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid UserDTO userDTO) {
        userService.saveUser(userService.parseToUser(userDTO));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PatchMapping("/user/{id}")
    public void updateUser(@RequestBody @Valid User user,@PathVariable Long id) {
        userService.update(user,id);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}