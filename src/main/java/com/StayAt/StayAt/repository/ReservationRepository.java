package com.StayAt.StayAt.repository;


import com.StayAt.StayAt.models.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findByUserId(String userId);

    List<Reservation> findByListingId(String listingId);

    void deleteByIdIn(List<String> ids);
}
