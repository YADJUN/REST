package com.example.REST.DTO;

import com.example.REST.model.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
public class UserDTO {

    private String username;

    private int age;

    private String password;

    private Set<Role> roles;
}
