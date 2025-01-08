package com.StayAt.StayAt.repository;

import com.StayAt.StayAt.models.Listing;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListingRepository extends MongoRepository<Listing, String> {
    List<Listing> findByTitleContaining(String title);

    List<Listing> findByUserId(String userId);
}
