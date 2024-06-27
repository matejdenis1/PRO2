package com.example.delivery.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void TestRegisterUser_Success() {

        RegisterForm registerForm = new RegisterForm();
        registerForm.setEmail("test@test.com");
        registerForm.setPassword("password");
        registerForm.setAddress("123 Test St");
        registerForm.setRole("customer");

        when(userRepository.findByEmail(registerForm.getEmail())).thenReturn(Optional.empty());

        assertEquals("", userService.registerUser(registerForm));

        verify(userRepository,times(1)).findByEmail(registerForm.getEmail());
        verify(mongoTemplate,times(1)).save(any(User.class));
    }
    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        // Given
        RegisterForm registerForm = new RegisterForm();
        registerForm.setEmail("test@test.com");
        registerForm.setPassword("password");
        registerForm.setAddress("123 Test St");
        registerForm.setRole("customer");

        when(userRepository.findByEmail(registerForm.getEmail())).thenReturn(Optional.of(new User()));


        assertEquals("User with this email already exists", userService.registerUser(registerForm));

        verify(userRepository, times(1)).findByEmail(registerForm.getEmail());

        verify(mongoTemplate, never()).save(any(User.class));
    }
    @Test
    void TestRegisterUser_AlreadyExists() {

        RegisterForm registerForm = new RegisterForm();
        registerForm.setEmail("test@test.cz");
        registerForm.setPassword("test");
        registerForm.setAddress("Test 123");
        registerForm.setRole("customer");

        when(userRepository.findByEmail(registerForm.getEmail())).thenReturn(Optional.empty());

        assertEquals("", userService.registerUser(registerForm));

        verify(userRepository,times(1)).findByEmail(registerForm.getEmail());
        verify(mongoTemplate,times(1)).save(any(User.class));
    }
    @Test
    void TestLoginUser_Success() {

        LoginForm loginForm = new LoginForm("test@test.com", "password123");
        User mockUser = new User("test@test.com", new BCryptPasswordEncoder().encode("password123"), "test","customer", new ArrayList<>());

        when(passwordEncoder.matches("password123", mockUser.getPassword())).thenReturn(true);
        when(userRepository.findByEmail(loginForm.getEmail())).thenReturn(Optional.of(mockUser));

        Optional<User> result = userService.loginUser(loginForm);

        assertTrue(result.isPresent());
        assertEquals(mockUser, result.get());

        verify(userRepository,times(1)).findByEmail(loginForm.getEmail());

    }
    @Test
    public void testLoginUser_InvalidCredentials() {
        // Mock data
        LoginForm loginForm = new LoginForm("test@test.com", "notrightpassword");
        User mockUser = new User("test@test.com", new BCryptPasswordEncoder().encode("password123"), "test","customer", new ArrayList<>());

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockUser));

        when(passwordEncoder.matches(loginForm.getPassword(), mockUser.getPassword())).thenReturn(false);

        Optional<User> result = userService.loginUser(loginForm);

        assertFalse(result.isPresent());

        verify(userRepository, times(1)).findByEmail(loginForm.getEmail());
    }
    @Test
    public void testLoginUser_UserNotFound() {
        LoginForm loginForm = new LoginForm("test@test.com", "password123");

        when(userRepository.findByEmail(loginForm.getEmail())).thenReturn(Optional.empty());

        Optional<User> result = userService.loginUser(loginForm);

        assertFalse(result.isPresent());

        verify(userRepository, times(1)).findByEmail(loginForm.getEmail());
    }
}