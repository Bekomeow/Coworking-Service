package org.beko.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.beko.dto.PlaceRequest;
import org.beko.model.Audit;
import org.beko.model.Place;
import org.beko.model.types.Role;
import org.beko.security.Authentication;
import org.beko.service.AuditService;
import org.beko.service.PlaceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Controller for managing admin operations.
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Api(value = "Application administration")
public class AdminController {
    private final AuditService auditService;
    private final PlaceService placeService;
    private final ServletContext servletContext;

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Create a new place", response = Place.class)
    @PostMapping("/places")
    public ResponseEntity<Place> createWorkspace(@RequestBody PlaceRequest request) throws AccessDeniedException {
        isAdmin();

        return ResponseEntity.ok(placeService.addPlace(request.getName(), request.getType()));
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Update place")
    @PutMapping("/places")
    public ResponseEntity<Void> updateWorkspace(@RequestParam String name, @RequestBody PlaceRequest request) throws AccessDeniedException {
        isAdmin();

        long placeId = placeService.getPlaceByName(name).get().getId();

        placeService.updatePlace(placeId, request.getName(), request.getType());
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Delete place")
    @DeleteMapping("/places")
    public ResponseEntity<Void> deleteWorkspace(@RequestParam Long id) throws AccessDeniedException {
        isAdmin();
        placeService.deletePlace(id);
        return ResponseEntity.ok().build();
    }

    @ApiImplicitParam(name = "Authorization", value = "Bearer token", required = true, dataTypeClass = String.class, paramType = "header")
    @ApiOperation(value = "Get list of all audits", response = Audit.class)
    @GetMapping("/audits")
    public ResponseEntity<List<Audit>> getListOfAllAudits() throws AccessDeniedException {
        isAdmin();
        return ResponseEntity.ok(auditService.getAllAudits());
    }

    private void isAdmin() throws AccessDeniedException {
        Authentication authentication = (Authentication) servletContext.getAttribute("authentication");
        if (authentication.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("You do not have permission to access this page.");
        }
    }

}