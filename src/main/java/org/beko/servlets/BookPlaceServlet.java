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
//import org.beko.dto.BookingRequest;
//import org.beko.dto.ExceptionResponse;
//import org.beko.exception.NotValidArgumentException;
//import org.beko.exception.UserNotFoundException;
//import org.beko.exception.PlaceAlreadyBookedException;
//import org.beko.exception.PlaceNotFoundException;
//import org.beko.model.Booking;
//import org.beko.security.Authentication;
//import org.beko.service.BookingService;
//import org.beko.service.PlaceService;
//import org.beko.service.UserService;
//
//import java.io.IOException;
//import java.nio.file.AccessDeniedException;
//import java.util.Set;
//
//@WebServlet("/places/book")
//public class BookPlaceServlet extends HttpServlet {
//
//    private BookingService bookingService;
//    private PlaceService placeService;
//    private UserService userService;
//    private ObjectMapper objectMapper;
//
//
//    @Override
//    public void init(ServletConfig config) throws ServletException {
//        super.init(config);
//        bookingService = (BookingService) getServletContext().getAttribute("bookingService");
//        placeService = (PlaceService) getServletContext().getAttribute("placeService");
//        userService = (UserService) getServletContext().getAttribute("userService");
//        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        try {
//            Authentication authUserInfo = (Authentication) getServletContext().getAttribute("authentication");
//
//            BookingRequest bookingRequest = objectMapper.readValue(req.getReader(), BookingRequest.class);
//
//            Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//            Set<ConstraintViolation<BookingRequest>> violations = validator.validate(bookingRequest);
//            for (ConstraintViolation<BookingRequest> violation : violations) {
//                throw new NotValidArgumentException(violation.getMessage());
//            }
//
//            var place = placeService.getPlaceByName(bookingRequest.getPlaceName());
//            var user = userService.getUserByName(authUserInfo.getUsername());
//
//            Booking createdBooking = bookingService.bookPlace(user, place.get(),
//                    bookingRequest.getStartTime(), bookingRequest.getEndTime());
//
//            resp.setStatus(HttpServletResponse.SC_CREATED);
//            objectMapper.writeValue(resp.getWriter(), createdBooking);
//        } catch (AccessDeniedException e) {
//            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
//            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
//        } catch (NotValidArgumentException | PlaceNotFoundException | UserNotFoundException |
//                 PlaceAlreadyBookedException e){
//            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
//        } catch (RuntimeException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
//        }
//    }
//}
//
