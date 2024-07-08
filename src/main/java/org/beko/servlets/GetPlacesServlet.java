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
import org.beko.exception.PlaceNotFoundException;
import org.beko.model.Place;
import org.beko.service.PlaceService;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/place")
public class GetPlacesServlet extends HttpServlet {
    private PlaceService placeService;
    private ObjectMapper objectMapper;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        placeService = (PlaceService) getServletContext().getAttribute("placeService");
        objectMapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String placeId = req.getParameter("id");
            String placeName = req.getParameter("name");

            isValidRequest(placeId, placeName);

            Optional<Place> place;

            if (placeId != null) {
                long id = Long.parseLong(placeId);
                place = placeService.getPlaceById(id);
            } else if (placeName != null) {
                place = placeService.getPlaceByName(placeName);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("No place name or ID provided"));
                return;
            }

            if (place.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Place not found"));
                return;
            }

            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), place.get());
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse("Incorrect place ID format."));
        } catch (PlaceNotFoundException | NotValidArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ExceptionResponse(e.getMessage()));
        }
    }

    private boolean isValidRequest(String placeId, String placeName) throws NotValidArgumentException{
        int paramCount = 0;
        if (placeId != null) paramCount++;
        if (placeName != null) paramCount++;

        if (paramCount > 1) throw new NotValidArgumentException("You can only pass one parameter: id or name.");
        return paramCount == 1;
    }
}

