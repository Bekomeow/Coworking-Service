//package org.beko.servlets;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletConfig;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.beko.model.Place;
//import org.beko.service.BookingService;
//
//import java.io.IOException;
//import java.util.List;
//
//@WebServlet("/place/available")
//public class GetAvailablePlacesServlet extends HttpServlet {
//
//    private BookingService bookingService;
//    private ObjectMapper objectMapper;
//
//    @Override
//    public void init(ServletConfig config) throws ServletException {
//        super.init(config);
//        bookingService = (BookingService) getServletContext().getAttribute("bookingService");
//        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
//    }
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//        try {
//            List<Place> workspaces = bookingService.getAvailablePlacesAtNow();
//            resp.setStatus(HttpServletResponse.SC_OK);
//            objectMapper.writeValue(resp.getWriter(), workspaces);
//        } catch (RuntimeException e) {
//            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//        }
//    }
//}