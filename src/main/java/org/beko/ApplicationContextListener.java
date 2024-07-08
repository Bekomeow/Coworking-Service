package org.beko;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.beko.dao.impl.BookingDAOImpl;
import org.beko.dao.impl.PlaceDAOImpl;
import org.beko.dao.impl.UserDAOImpl;
import org.beko.liquibase.LiquibaseDemo;
import org.beko.mapper.PlaceMapper;
import org.beko.security.JwtTokenUtils;
import org.beko.service.impl.*;
import org.beko.util.ConnectionManager;
import org.mapstruct.factory.Mappers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

/**
 * Application context for managing beans and dependencies.
 */
@WebListener
public class ApplicationContextListener implements ServletContextListener {

    private Properties properties;
    private ConnectionManager connectionManager;


    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext servletContext = sce.getServletContext();

        loadProperties(servletContext);
        databaseConfiguration(servletContext);
        serviceContextInit(servletContext);

        ObjectMapper objectMapper = new ObjectMapper();
        servletContext.setAttribute("objectMapper", objectMapper);
        servletContext.setAttribute("placeMapper", Mappers.getMapperClass(PlaceMapper.class));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

    private void loadProperties(ServletContext servletContext) {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(servletContext.getResourceAsStream("/WEB-INF/classes/application.properties"));
                servletContext.setAttribute("servletProperties", properties);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Property file not found!");
            } catch (IOException e) {
                throw new RuntimeException("Error reading configuration file: " + e.getMessage());
            }
        }
    }

    private void databaseConfiguration(ServletContext servletContext) {
        connectionManager = new ConnectionManager();
        servletContext.setAttribute("connectionManager", connectionManager);

        String changeLogFile = properties.getProperty("liquibase.change-log");
        String schemaName = properties.getProperty("liquibase.liquibase-schema");

        if (Boolean.parseBoolean(properties.getProperty("liquibase.enabled"))) {
            LiquibaseDemo liquibaseDemo = new LiquibaseDemo(connectionManager.getConnection(), changeLogFile, schemaName);
            liquibaseDemo.runMigrations();
            servletContext.setAttribute("liquibaseDemo", liquibaseDemo);
        }
    }

    private void serviceContextInit(ServletContext servletContext) {
//        UserDAO userDAO = new UserDAOImpl(connectionManager);
//        MeterTypeDAO meterTypeDAO = new MeterTypeDAOImpl(connectionManager);
//        MeterReadingDAO meterReadingDAO = new MeterReadingDAOImpl(connectionManager);
//        AuditDAOImpl auditDAO = new AuditDAOImpl(connectionManager);
//
//        AuditService auditService = new AuditServiceImpl(auditDAO);
//        UserService userService = new UserServiceImpl(userDAO);
//
//        AuditAspect auditAspect = new AuditAspect(auditService);
//
//        JwtTokenUtils jwtTokenUtils = new JwtTokenUtils(
//                properties.getProperty("jwt.secret"),
//                Duration.parse(properties.getProperty("jwt.lifetime")),
//                userService
//        );
//
//        SecurityService securityService = new SecurityServiceImpl(userDAO, jwtTokenUtils);
//        MeterTypeService meterTypeService = new MeterTypeServiceImpl(meterTypeDAO);
//        MeterReadingService meterReadingService = new MeterReadingServiceImpl(meterReadingDAO, userService, meterTypeService);

//        servletContext.setAttribute("jwtTokenUtils", jwtTokenUtils);
//        servletContext.setAttribute("auditService", auditService);
//        servletContext.setAttribute("userService", userService);
//        servletContext.setAttribute("securityService", securityService);
//        servletContext.setAttribute("meterTypeService", meterTypeService);
//        servletContext.setAttribute("meterReadingService", meterReadingService);

        BookingDAOImpl bookingDAO = new BookingDAOImpl(connectionManager);
        PlaceDAOImpl placeDAO = new PlaceDAOImpl(connectionManager);
        UserDAOImpl userDAO = new UserDAOImpl(connectionManager);

        BookingServiceImpl bookingService = new BookingServiceImpl(bookingDAO);
        PlaceServiceImpl placeService = new PlaceServiceImpl(placeDAO);
        UserServiceImpl userService = new UserServiceImpl(userDAO);

        JwtTokenUtils jwtTokenUtils = new JwtTokenUtils(
                properties.getProperty("jwt.secret"),
                Duration.parse(properties.getProperty("jwt.lifetime")),
                userService
        );

        SecurityServiceImpl securityService = new SecurityServiceImpl(userDAO, jwtTokenUtils);

        servletContext.setAttribute("bookingService", bookingService);
        servletContext.setAttribute("placeService", placeService);
        servletContext.setAttribute("userService", userService);
        servletContext.setAttribute("securityService", securityService);
    }
}