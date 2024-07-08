package org.beko.mapper;

import org.beko.dto.PlaceRequest;
import org.beko.model.Place;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PlaceMapper {
    @Mapping(target = "id", ignore = true)
    Place toEntity(PlaceRequest placeRequest);

    PlaceRequest toDto(Place place);
}
