package com.example.demo.ControllerTests;
import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.assertj.core.api.Assertions;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController,"userRepository", userRepository);
        TestUtils.injectObjects(userController,"cartRepository", cartRepository);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder", bCryptPasswordEncoder);
    }
    public CreateUserRequest getCreateUserRequest() {
        CreateUserRequest UserRequest= new CreateUserRequest("User","Admin123","Admin123");
        return UserRequest;
    }

    @Test
    public void verifyCreateUser() {
        when(bCryptPasswordEncoder.encode("Admin123")).thenReturn("thisIsHashed");

        final ResponseEntity<User> response = userController.createUser(getCreateUserRequest());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User UserResponse = response.getBody();
        assertNotNull(UserResponse);
       // assertEquals(0, UserResponse.getId());
        //assertNotEquals("Admin123", UserResponse.getUsername());
        //assertEquals("thisIsHashed", UserResponse.getPassword());
    }

    @Test
    public void verifycreateUserfail() {
        // password not match
        CreateUserRequest reqPasswordNotMatch = getCreateUserRequest();
        reqPasswordNotMatch.setConfirmPassword("PasswordDoesNotMatch");
        final ResponseEntity<User> resPasswordNotMatch = userController.createUser(reqPasswordNotMatch);
        assertNotNull(resPasswordNotMatch);
        //assertNotEquals(400, resPasswordNotMatch.getStatusCodeValue());


        CreateUserRequest RequestPass = getCreateUserRequest();
        RequestPass.setPassword("Admin123");
        RequestPass.setConfirmPassword("Admin123");
        final ResponseEntity<User> resPassword = userController.createUser(RequestPass);
        //assertNotEquals(400, resPassword.getStatusCodeValue());
    }

    @Test
    public void verifyfindUserbyID() {
        User User = new User(1l,"User1","Admin");

        // find by id
        when(userRepository.findById(1L)).thenReturn(Optional.of(User));
        final ResponseEntity<User> resFindById = userController.findById(User.getId());
        User user = resFindById.getBody();
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("User1", user.getUsername());

        // find by name
        when(userRepository.findByUsername("User1")).thenReturn(user);
        final ResponseEntity<User> resFindByName = userController.findByUserName("User1");
        user = resFindByName.getBody();
        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("User1", user.getUsername());
    }

    @Test
    public void verifyfindUserbyIDwithfail() {
        final ResponseEntity<User> UserresponseByID = userController.findById(2L);
        assertNull(UserresponseByID.getBody());

        final ResponseEntity<User> responseByName = userController.findByUserName("Incorrect");
        assertNull(responseByName.getBody());
    }
}