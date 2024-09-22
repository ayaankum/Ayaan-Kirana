package com.assignment.kirana.service;

import com.assignment.kirana.model.Role;
import com.assignment.kirana.model.User;
import com.assignment.kirana.model.UserDTO;
import com.assignment.kirana.repository.RoleRepository;
import com.assignment.kirana.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class DefaultUserServiceImplTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private RoleRepository roleRepo;

    @InjectMocks
    private DefaultUserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_userExists() {
        User user = new User();
        user.setUserName("testUser");
        user.setPassword("password");
        Set<Role> roles = new HashSet<>();
        roles.add(new Role("ROLE_USER"));
        user.setRole(roles);

        when(userRepo.findByUserName("testUser")).thenReturn(user);

        UserDetails userDetails = userService.loadUserByUsername("testUser");

        assertEquals("testUser", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
    }



    @Test
    void save_userWithUserRole() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setUserName("testUser");
        userDTO.setPassword("password");
        userDTO.setRole("USER");

        Role role = new Role("ROLE_USER");
        when(roleRepo.findByRole("ROLE_USER")).thenReturn(role);

        User savedUser = new User();
        savedUser.setEmail("test@example.com");
        savedUser.setUserName("testUser");
        savedUser.setPassword(new BCryptPasswordEncoder().encode("password"));
        savedUser.setRole(new HashSet<>(Set.of(role)));

        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        User result = userService.save(userDTO);

        assertEquals("test@example.com", result.getEmail());
        assertEquals("testUser", result.getUserName());
        assertTrue(new BCryptPasswordEncoder().matches("password", result.getPassword()));
        assertEquals(1, result.getRole().size());
        assertTrue(result.getRole().contains(role));
    }

    @Test
    void save_userWithAdminRole() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("admin@example.com");
        userDTO.setUserName("adminUser");
        userDTO.setPassword("adminPassword");
        userDTO.setRole("ADMIN");

        Role role = new Role("ROLE_ADMIN");
        when(roleRepo.findByRole("ROLE_ADMIN")).thenReturn(role);

        User savedUser = new User();
        savedUser.setEmail("admin@example.com");
        savedUser.setUserName("adminUser");
        savedUser.setPassword(new BCryptPasswordEncoder().encode("adminPassword"));
        savedUser.setRole(new HashSet<>(Set.of(role)));

        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        User result = userService.save(userDTO);

        assertEquals("admin@example.com", result.getEmail());
        assertEquals("adminUser", result.getUserName());
        assertTrue(new BCryptPasswordEncoder().matches("adminPassword", result.getPassword()));
        assertEquals(1, result.getRole().size());
        assertTrue(result.getRole().contains(role));
    }
}