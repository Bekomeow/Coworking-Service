package org.beko.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.beko.dto.PlaceRequest;
import org.beko.model.Place;
import org.beko.service.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Admin Controller")
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final PlaceService placeService;

    @PostMapping("/admin/places")
    @ApiOperation(value = "Add a new place", response = Place.class)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Place> addPlace(@RequestBody PlaceRequest request) {
        Place place = placeService.addPlace(request.getName(), request.getType());
        return ResponseEntity.ok(place);
    }
}
