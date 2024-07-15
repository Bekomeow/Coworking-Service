package org.beko.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.beko.dto.BookingRequest;
import org.beko.model.Booking;
import org.beko.model.Place;
import org.beko.model.User;
import org.beko.security.Authentication;
import org.beko.service.BookingService;
import org.beko.service.PlaceService;
import org.beko.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for managing workspace bookings.
 */
@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Api(value = "Booking controller")
public class BookingController {
    private final BookingService bookingService;
    private final UserService userService;
    private final PlaceService placeService;
    private final ServletContext servletContext;

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Booking place", response = Booking.class)
    @PostMapping("/book")
    public ResponseEntity<Booking> bookPlace(@RequestBody BookingRequest request) {
        Authentication authentication = (Authentication) servletContext.getAttribute("authentication");

        User user = userService.getUserByName(authentication.getUsername());
        Place place = placeService.getPlaceByName(request.getPlaceName()).get();

        return ResponseEntity.ok(bookingService.bookPlace(user, place, request.getStartTime(), request.getEndTime()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get list bookings for date", response = Booking.class)
    @GetMapping("/date")
    public ResponseEntity<List<Booking>> getFilteredBookingsByDate(@RequestParam String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate date = LocalDate.parse(dateStr, formatter);

        return ResponseEntity.ok(bookingService.listBookingsByDate(date));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get list bookings by username", response = Booking.class)
    @GetMapping("/username")
    public ResponseEntity<List<Booking>> getFilteredBookingsByUsername(@RequestParam String username) {
        return ResponseEntity.ok(bookingService.listBookingsByUser(username));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get list bookings by place name", response = Booking.class)
    @GetMapping("/place")
    public ResponseEntity<List<Booking>> getFilteredBookingsByWorkspace(@RequestParam String name) {
        return ResponseEntity.ok(bookingService.listBookingsByPlace(name));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Cancel booking")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable String id) throws AccessDeniedException {
        bookingService.cancelBooking(Long.parseLong(id));
        return ResponseEntity.ok().build();
    }
}
