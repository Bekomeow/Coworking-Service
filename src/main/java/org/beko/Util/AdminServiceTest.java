//package org.beko.util;
//
//import org.beko.Service.AdminService;
//import org.junit.Test;
//
//import static org.junit.Assert.*;
//
//public class AdminServiceTest {
//    private AdminService adminService = new AdminService();
//
//    @Test
//    public void testLoginAdminSuccessfully() {
//        try {
//            adminService.login("Admin", "Admin");
//        } catch (Exception e) {
//            fail("Exception should not be thrown");
//        }
//    }
//
//    @Test
//    public void testLoginAdminInvalidCredentials() {
//        try {
//            adminService.login("Admin", "WrongPassword");
//            fail("Expected IllegalArgumentException to be thrown");
//        } catch (IllegalArgumentException e) {
//            // expected
//        }
//    }
//}
