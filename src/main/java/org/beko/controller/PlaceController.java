package org.beko.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.beko.model.Audit;
import org.beko.model.Place;
import org.beko.service.impl.BookingServiceImpl;
import org.beko.service.impl.PlaceServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controller for managing workspaces.
 */
@RestController
@RequestMapping("/places")
@RequiredArgsConstructor
@Api(value = "Place Controller")
public class PlaceController {
    private final PlaceServiceImpl placeService;
    private final BookingServiceImpl bookingService;

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get list of all places", response = Audit.class)
    @GetMapping("/list")
    public ResponseEntity<List<Place>> getListOfAllWorkspaces() throws AccessDeniedException {
        return ResponseEntity.ok(placeService.listPlaces());
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get place by name", response = Place.class)
    @GetMapping("/name/{name}")
    public ResponseEntity<Place> getWorkspaceByName(@PathVariable("name") String name) throws AccessDeniedException {
        Place place = placeService.getPlaceByName(name).get();
        return ResponseEntity.ok(place);
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get place by id", response = Place.class)
    @GetMapping("/id/{id}")
    public ResponseEntity<Place> getWorkspaceById(@PathVariable("id") String id) throws AccessDeniedException {
        Place place = placeService.getPlaceById(Long.parseLong(id)).get();
        return ResponseEntity.ok(place);
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get list of all available places at the current time", response = Audit.class)
    @GetMapping("/available")
    public ResponseEntity<List<Place>> getAvailableWorkspacesAtNow() throws AccessDeniedException {
        return ResponseEntity.ok(bookingService.getAvailablePlacesAtNow());
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get list of all available places for date", response = Audit.class)
    @GetMapping("/available-date")
    public ResponseEntity<List<Place>> getAvailableWorkspacesForTimePeriod(@RequestParam String dateStr) throws AccessDeniedException {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate date = LocalDate.parse(dateStr, formatter);
        return ResponseEntity.ok(bookingService.getAvailablePlacesForDate(date));
    }
}
