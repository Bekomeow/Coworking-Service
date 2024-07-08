package org.beko.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.beko.dto.ExceptionResponse;
import org.beko.exception.NotValidArgumentException;
import org.beko.exception.UserNotFoundException;
import org.beko.exception.PlaceNotFoundException;
import org.beko.model.Booking;
import org.beko.service.BookingService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/places/bookings")
public class GetAllBookingsServlet extends HttpServlet {
    private BookingService bookingService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        bookingService = (BookingService) getServletContext().getAttribute("bookingService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String dateStr = req.getParameter("date");
            String username = req.getParameter("username");
            String workspaceName = req.getParameter("name");

            isValidRequest(dateStr, username, workspaceName);

            List<Booking> bookings;
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

            if (dateStr != null) {
                LocalDate date = LocalDate.parse(dateStr, formatter);
                bookings = bookingService.listBookingsByDate(date);
            } else if (username != null) {
                bookings = bookingService.listBookingsByUser(username);
            } else if (workspaceName != null) {
                bookings = bookingService.listBookingsByPlace(workspaceName);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Не указаны параметры запроса"));
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), bookings);

        } catch (UserNotFoundException | PlaceNotFoundException | NotValidArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }

    private boolean isValidRequest(String dateStr, String username, String workspaceName) throws NotValidArgumentException {
        int paramCount = 0;
        if (dateStr != null) paramCount++;
        if (username != null) paramCount++;
        if (workspaceName != null) paramCount++;

        if (paramCount > 1) throw new NotValidArgumentException("Можно передавать только один параметр: dateStr, username, name");
        return paramCount == 1;
    }
}