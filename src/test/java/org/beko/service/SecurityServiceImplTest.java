//package org.beko.service;
//
//import org.beko.containers.PostgresTestContainer;
//import org.beko.dao.UserDAO;
//import org.beko.dao.impl.UserDAOImpl;
//import org.beko.dto.TokenResponse;
//import org.beko.exception.AuthorizeException;
//import org.beko.exception.NotValidArgumentException;
//import org.beko.exception.RegisterException;
//import org.beko.liquibase.LiquibaseDemo;
//import org.beko.model.User;
//import org.beko.security.JwtTokenUtils;
//import org.beko.service.impl.SecurityServiceImpl;
//import org.beko.util.ConnectionManager;
//import org.beko.util.PropertiesUtil;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
//class SecurityServiceImplTest extends PostgresTestContainer {
//
//    private SecurityServiceImpl securityService;
//    private UserDAO userDAO;
//    private JwtTokenUtils jwtTokenUtils;
//    private ConnectionManager connectionManager;
//
//    @BeforeEach
//    public void setUp() {
//        connectionManager = new ConnectionManager(
//                container.getJdbcUrl(),
//                container.getUsername(),
//                container.getPassword()
//        );
//
//        String changeLogFile = PropertiesUtil.get("liquibase.change-log");
//        String schemaName = PropertiesUtil.get("liquibase.liquibase-schema");
//
//        LiquibaseDemo liquibaseDemo = new LiquibaseDemo(connectionManager.getConnection(), changeLogFile, schemaName);
//        liquibaseDemo.runMigrations();
//
//        userDAO = new UserDAOImpl(connectionManager);
//        jwtTokenUtils = mock(JwtTokenUtils.class);
//        securityService = new SecurityServiceImpl(userDAO, jwtTokenUtils);
//        clearUserTable();
//        resetSequence();
//    }
//
//    private void clearUserTable() {
//        String sql = "DELETE FROM coworking.\"user\"";
//        try (var connection = connectionManager.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.executeUpdate(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void resetSequence() {
//        String sql = "ALTER SEQUENCE coworking.\"user_id_seq\" RESTART WITH 1";
//        try (var connection = connectionManager.getConnection();
//             Statement statement = connection.createStatement()) {
//            statement.executeUpdate(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void testRegisterSuccess() {
//        String login = "newUser";
//        String password = "password123";
//
//        User newUser = securityService.register(login, password);
//
//        assertThat(newUser).isNotNull();
//        assertThat(newUser.getUsername()).isEqualTo(login);
//        assertThat(newUser.getPassword()).isEqualTo(password);
//    }
//
//    @Test
//    void testRegisterWithInvalidArgs() {
//        String invalidLogin = " ";
//        String invalidPassword = " ";
//
//        assertThatThrownBy(() -> securityService.register(invalidLogin, invalidPassword))
//                .isInstanceOf(NotValidArgumentException.class)
//                .hasMessage("Пароль или логин не могут быть пустыми или состоять только из пробелов.");
//    }
//
//    @Test
//    void testRegisterWithExistingUser() {
//        String login = "existingUser";
//        String password = "password123";
//        userDAO.save(User.builder().username(login).password(password).build());
//
//        assertThatThrownBy(() -> securityService.register(login, password))
//                .isInstanceOf(RegisterException.class)
//                .hasMessage("Пользователь с таким логином уже существует.");
//    }
//
//    @Test
//    void testAuthorizeSuccess() {
//        String login = "user";
//        String password = "password123";
//        userDAO.save(User.builder().username(login).password(password).build());
//        when(jwtTokenUtils.generateToken(login)).thenReturn("token123");
//
//        TokenResponse tokenResponse = securityService.authorize(login, password);
//
//        assertThat(tokenResponse).isNotNull();
//        assertThat(tokenResponse.token()).isEqualTo("token123");
//    }
//
//    @Test
//    void testAuthorizeWithInvalidLogin() {
//        String login = "invalidUser";
//        String password = "password123";
//
//        assertThatThrownBy(() -> securityService.authorize(login, password))
//                .isInstanceOf(AuthorizeException.class)
//                .hasMessage("Пользователь с данным логином отсутствует в базе данных.");
//    }
//
//    @Test
//    void testAuthorizeWithInvalidPassword() {
//        String login = "user";
//        String password = "password123";
//        userDAO.save(User.builder().username(login).password(password).build());
//
//        assertThatThrownBy(() -> securityService.authorize(login, "wrongPassword"))
//                .isInstanceOf(AuthorizeException.class)
//                .hasMessage("Неверный пароль.");
//    }
//}
//
