//package org.beko.util;
//
//import org.beko.Entity.User;
//import org.beko.Service.UserService;
//import org.junit.Before;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class UserServiceTest {
//    private UserService userService;
//
//    @Before
//    public void setUp() {
//        userService = new UserService();
//    }
//
//    @Test
//    public void testRegisterUser() {
//        User user = userService.register("testUser", "password");
//        assertNotNull(user);
//        assertEquals("testUser", user.getUsername());
//    }
//
//    @Test
//    public void testRegisterUserAlreadyExists() {
//        userService.register("testUser", "password");
//        try {
//            userService.register("testUser", "password");
//            fail("Expected IllegalArgumentException to be thrown");
//        } catch (IllegalArgumentException e) {
//            // expected
//        }
//    }
//
//    @Test
//    public void testLoginUserSuccessfully() {
//        userService.register("testUser", "password");
//        User user = userService.login("testUser", "password");
//        assertNotNull(user);
//        assertEquals("testUser", user.getUsername());
//    }
//
//    @Test
//    public void testLoginUserInvalidCredentials() {
//        userService.register("testUser", "password");
//        try {
//            userService.login("testUser", "wrongPassword");
//            fail("Expected IllegalArgumentException to be thrown");
//        } catch (IllegalArgumentException e) {
//            // expected
//        }
//    }
//
//    @Test
//    public void testHasUser() {
//        userService.register("testUser", "password");
//        assertTrue(userService.hasUser("testUser"));
//        assertFalse(userService.hasUser("nonExistentUser"));
//    }
//}
