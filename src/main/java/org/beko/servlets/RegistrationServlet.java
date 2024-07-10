//package org.beko.servlets;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import org.beko.dto.*;
//import org.beko.exception.NotValidArgumentException;
//import org.beko.exception.RegisterException;
//import org.beko.model.User;
//import org.beko.service.SecurityService;
//import java.io.IOException;
//import java.util.Set;
//
//@WebServlet("/auth/registration")
//public class RegistrationServlet extends HttpServlet {
//    private SecurityService securityService;
//    private ObjectMapper objectMapper;
//
//    @Override
//    public void init(ServletConfig config) throws ServletException {
//        super.init(config);
//        securityService = (SecurityService) getServletContext().getAttribute("securityService");
//        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//
//        try {
//            SecurityRequest request = objectMapper.readValue(req.getInputStream(), SecurityRequest.class);
//
//            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//            Set<ConstraintViolation<SecurityRequest>> violations = validator.validate(request);
//            for (ConstraintViolation<SecurityRequest> violation : violations) {
//                throw new NotValidArgumentException(violation.getMessage());
//            }
//
//            User registeredUser = securityService.register(request.username(), request.password());
//
//            resp.setStatus(HttpServletResponse.SC_CREATED);
//            objectMapper.writeValue(resp.getWriter(), new SuccessResponse("User with login " + registeredUser.getUsername() + " successfully created."));
//        } catch (NotValidArgumentException | JsonParseException | RegisterException e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
//        } catch (RuntimeException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
//        }
//
//        super.doPost(req, resp);
//    }
//}