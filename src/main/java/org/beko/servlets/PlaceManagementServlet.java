//package org.beko.servlets;
//
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
//import org.beko.dto.ExceptionResponse;
//import org.beko.dto.PlaceRequest;
//import org.beko.exception.NotValidArgumentException;
//import org.beko.exception.PlaceAlreadyExistException;
//import org.beko.exception.PlaceNotFoundException;
//import org.beko.mapper.PlaceMapper;
//import org.beko.model.Place;
//import org.beko.model.types.Role;
//import org.beko.security.Authentication;
//import org.beko.service.PlaceService;
//
//import java.io.IOException;
//import java.nio.file.AccessDeniedException;
//import java.util.Set;
//
//@WebServlet("/admin/places")
//public class PlaceManagementServlet extends HttpServlet {
//    private PlaceService placeService;
//    private ObjectMapper objectMapper;
//    private PlaceMapper placeMapper;
//
//    @Override
//    public void init(ServletConfig config) throws ServletException {
//        super.init(config);
//        placeService = (PlaceService) getServletContext().getAttribute("placeService");
//        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
//        placeMapper = (PlaceMapper) getServletContext().getAttribute("placeMapper");
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json");
//
//        try {
//            isAdmin(req);
//
//            PlaceRequest placeDto = objectMapper.readValue(req.getReader(), PlaceRequest.class);
//
//            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//            Set<ConstraintViolation<PlaceRequest>> violations = validator.validate(placeDto);
//            for (ConstraintViolation<PlaceRequest> violation : violations) {
//                throw new NotValidArgumentException(violation.getMessage());
//            }
//
//            Place place = placeMapper.toEntity(placeDto);
//
//            placeService.addPlace(place.getName(), place.getType());
//
//            resp.setStatus(HttpServletResponse.SC_CREATED);
//            objectMapper.writeValue(resp.getWriter(), place);
//        } catch (AccessDeniedException e) {
//            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
//        } catch (NotValidArgumentException | PlaceAlreadyExistException e){
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
//        } catch (RuntimeException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
//        }
//    }
//
//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        try {
//            isAdmin(req);
//
//            String placeName = req.getParameter("name");
//            if (placeName == null || placeName.isEmpty())
//                throw new NotValidArgumentException("Workspace name is not specified.");
//
//            Place updatedPlace = objectMapper.readValue(req.getReader(), Place.class);
//
//            long placeId = placeService.getPlaceByName(placeName).get().getId();
//
//            placeService.updatePlace(placeId, updatedPlace.getName(), updatedPlace.getType());
//
//            resp.setStatus(HttpServletResponse.SC_OK);
//        } catch (AccessDeniedException e) {
//            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
//        } catch (NotValidArgumentException | PlaceAlreadyExistException | PlaceNotFoundException e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
//        } catch (RuntimeException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
//        }
//    }
//
//    @Override
//    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        try {
//            isAdmin(req);
//
//            String id = req.getParameter("id");
//            if (id == null || id.isEmpty()) throw new NotValidArgumentException("Place id not specified.");
//
//            placeService.deletePlace(Long.valueOf(id));
//
//            resp.setStatus(HttpServletResponse.SC_OK);
//        } catch (AccessDeniedException e) {
//            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
//        } catch (NotValidArgumentException | PlaceNotFoundException e) {
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
//        } catch (RuntimeException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
//        }
//    }
//
//    private void isAdmin(HttpServletRequest req) throws AccessDeniedException {
//        Authentication authentication = (Authentication) getServletContext().getAttribute("authentication");
//        if (authentication.getRole() != Role.ADMIN) {
//            throw new AccessDeniedException("You do not have permission to access this page.");
//        }
//    }
//}
//
