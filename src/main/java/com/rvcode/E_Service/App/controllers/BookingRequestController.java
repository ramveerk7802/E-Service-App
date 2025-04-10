package com.rvcode.E_Service.App.controllers;

import com.rvcode.E_Service.App.entities.BookingRequest;
import com.rvcode.E_Service.App.exception.MyCustomException;
import com.rvcode.E_Service.App.services.BookingRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking-request")
public class BookingRequestController {

    private final BookingRequestService bookingRequestService;

    public BookingRequestController(BookingRequestService bookingRequestService) {
        this.bookingRequestService = bookingRequestService;
    }




    @PostMapping("/create")
    public ResponseEntity<?> createNewBookingRequest(@RequestParam Long serviceTypeId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null && !authentication.isAuthenticated()){
            throw new MyCustomException(" User Not authorized");
        }
        String email = authentication.getName();
        BookingRequest saved = bookingRequestService.addNewServiceRequest(email,serviceTypeId);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);

    }



}

