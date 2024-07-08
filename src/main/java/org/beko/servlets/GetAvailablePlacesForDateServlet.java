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
import org.beko.model.Place;
import org.beko.service.BookingService;
import org.beko.service.PlaceService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/workspaces/available-for-period")
public class GetAvailablePlacesForDateServlet extends HttpServlet {

    private BookingService bookingService;
    private PlaceService placeService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        bookingService = (BookingService) getServletContext().getAttribute("bookingService");
        placeService = (PlaceService) getServletContext().getAttribute("placeService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String dateStr = req.getParameter("date");

            if(dateStr.isBlank()) throw new NotValidArgumentException("Неверный формат запроса");

            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

            LocalDate date = LocalDate.parse(dateStr, formatter);

            List<Place> workspaces = bookingService.getAvailablePlacesForDate(date);

            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), workspaces);
        } catch (NotValidArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (DateTimeParseException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Неверный формат даты и времени"));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}