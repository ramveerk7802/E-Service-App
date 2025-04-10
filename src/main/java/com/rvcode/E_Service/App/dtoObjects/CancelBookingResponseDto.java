package com.rvcode.E_Service.App.dtoObjects;

import com.rvcode.E_Service.App.entities.BookingRequest;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CancelBookingResponseDto {
    private Boolean status;
    private String message;
    private BookingRequest bookingRequest;
}
