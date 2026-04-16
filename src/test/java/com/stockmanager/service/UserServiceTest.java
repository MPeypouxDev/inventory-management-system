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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setPassword("$2a$10$MySWp6cCIMRY1TSvWrk0sOS.TnEndLhZnW0Fm/lbKWVF.ZuO3KJJS");
        testUser.setRole(User.Role.ADMIN);
        testUser.setUsername("admin");
    }

    @Test
    @DisplayName("delete() doit lancer une exception si l'utilisateur est introuvable")
    void delete_shouldThrowException_whenUserNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.delete(99L));

        assertEquals("Utilisateur introuvable avec l'id : 99", exception.getMessage());
    }

    @Test
    @DisplayName("delete() doit échouer si l'utilisateur supprimer est le dernier admin")
    void delete_shouldFail_whenUserIsLastAdmin() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.delete(1L));

        assertEquals("Impossible de supprimer le dernier administrateur !", exception.getMessage());
    }

    @Test
    @DisplayName("delete() doit réussir si l'utilisateur a comme rôle USER")
    void delete_shouldBeSuccessful_whenUserRoleIsUser() {
        User user = new User();
        user.setRole(User.Role.USER);
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("save() doit sauvegarder l'utilisateur si il remplit toutes les conditions nécessaires")
    void save_shouldSaveUser_whenAllConditionsAreCorrect() {
        User user = new User();
        user.setId(1L);
        user.setUsername("Mathys");
        user.setPassword("001129SYHTAMfoot**");
        user.setRole(User.Role.USER);

        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        assertNotNull(result);
        assertEquals("Mathys", result.getUsername());
    }

    @Test
    @DisplayName("searchByName() retourne seulement l'utilisateur avec le nom exact saisi")
    void searchByName_shouldReturnOnlyUser_whenTheNameIsExactSame() {
        when(userRepository.findByUsernameContainingIgnoreCase("admin")).thenReturn(List.of(testUser));

        List<User> result = userService.searchByName("admin");

        assertEquals(1, result.size());
        assertEquals("admin", result.getFirst().getUsername());
    }
}
