package com.example.REST.service;

import com.example.REST.DTO.UserDTO;
import com.example.REST.model.Role;
import com.example.REST.model.User;
import com.example.REST.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class UserService implements UserDetailsService {
    private static final String ROLE_USER = "ROLE_USER";
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleService roleService;

    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElseThrow();
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void saveUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (user.getRoles() == null) {
            Set<Role> roles = new HashSet<>();
            roles.add(roleService.findByName(ROLE_USER));
            user.setRoles(roles);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void createUser(User user, Set<Role> roles) {
        if (roles.isEmpty()) {
            roles.add(roleService.findByName(ROLE_USER));
            user.setRoles(roles);
            saveUser(user);
        } else {
            saveUser(user);
        }
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.findById(userId);
        userRepository.deleteById(userId);
    }

    @Transactional
    public void update(User user,Long id) {
            User userToUpdate = findUserById(id);
            userToUpdate.setUsername(user.getUsername());
            userToUpdate.setAge(user.getAge());
            userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(userToUpdate);


    }
    public User parseToUser(UserDTO userDTO) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(userDTO, User.class);
    }
    public UserDTO parseToUserDTO(User user) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(user, UserDTO.class);
    }

    @Autowired
    public void setRoleRepository(RoleService roleService) {
        this.roleService = roleService;
    }
}
