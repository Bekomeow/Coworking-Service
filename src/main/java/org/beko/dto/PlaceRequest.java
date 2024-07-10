package org.beko.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlaceRequest {
    /**
     * The name of place.
     */
    @NotNull(message = "Place cannot be null")
    @NotBlank(message = "Place cannot be blank")
    @Size(min = 3, max = 30, message = "Place length must be between 3 and 30 characters")
    private String name;

    /**
     * The type of place.
     */
    @NotNull(message = "Type cannot be null")
    @NotBlank(message = "Type cannot be blank")
    private String type;

    @JsonCreator
    public PlaceRequest(@JsonProperty("name") String name,
                        @JsonProperty("type") String type) {
        this.name = name;
        this.type = type;
    }
}
