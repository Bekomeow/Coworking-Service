package org.beko.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingRequest {

    @NotNull(message = "Place name cannot be null")
    @NotBlank(message = "Place name cannot be blank")
    private String placeName;

    /**
     * The start time of booking.
     */
    @NotNull(message = "Start time cannot be null")
    @Future(message = "Start time must be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * The end time of booking.
     */
    @NotNull(message = "End time cannot be null")
    @Future(message = "End time must be in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endTime;

    @JsonCreator
    public BookingRequest(@JsonProperty("placeName") String placeName,
                          @JsonProperty("startTime") LocalDateTime startTime,
                          @JsonProperty("endTime") LocalDateTime endTime) {
        this.placeName = placeName;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}