package com.stockmanager.service;

import com.stockmanager.model.User;
import com.stockmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser =  new User();
        testUser.setId(1L);
        testUser.setUsername("Mathys");
        testUser.setPassword("001129SYHTAMfoot**");
    }

    @Test
    @DisplayName("username introuvable")
    void login_shouldReturnEmpty_whenUsernameNotFound() {
        when(userRepository.findByUsername("Mathys")).thenReturn(Optional.empty());

        Optional<User> result = authService.login("Mathys", "motDePasse");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("username trouvé mais mauvais password")
    void login_shouldReturnEmpty_whenPasswordIsWrong() {
        when(userRepository.findByUsername("Mathys")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("motDePasse", "001129SYHTAMfoot**")).thenReturn(false);
        Optional<User> result = authService.login("Mathys", "motDePasse");

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("username et password correct")
    void login_shouldReturnUser_whenCredentialAreValid() {
        when(userRepository.findByUsername("Mathys")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("001129SYHTAMfoot**", "001129SYHTAMfoot**")).thenReturn(true);
        Optional<User> result = authService.login("Mathys", "001129SYHTAMfoot**");
        assertFalse(result.isEmpty());
    }
}
