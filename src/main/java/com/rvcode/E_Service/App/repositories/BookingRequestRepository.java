package com.rvcode.E_Service.App.repositories;

import com.rvcode.E_Service.App.entities.BookingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRequestRepository extends JpaRepository<BookingRequest,Long> {
}
