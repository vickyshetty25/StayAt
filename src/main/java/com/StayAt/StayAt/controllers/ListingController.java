package com.StayAt.StayAt.controllers;


import com.StayAt.StayAt.dto.ListingSearchRequest;
import com.StayAt.StayAt.models.Listing;
import com.StayAt.StayAt.security.services.ListingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class ListingController {

    @Autowired
    ListingService listingService;

    // Controller for listing endpoints
    @GetMapping("/listing")
    @ResponseStatus(HttpStatus.OK)
    public List<Listing> getAllListings(@RequestParam(required = false) String title){
        if(title == null)
            return listingService.findAll();
        else
            return listingService.findByTitleContaining(title);
    }


    @GetMapping("/listings/search")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Listing>> searchListings(@Valid @ModelAttribute ListingSearchRequest request) throws ParseException{
        List<Listing> listings = listingService.searchListings(request);
        if(listings.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(listings);
    }

    @GetMapping("/listing/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Listing getListingById(@PathVariable("id") String id){
        return listingService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, null));
    }

    @GetMapping("/listings/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<Listing>> getListingsByUserId(@PathVariable("id") String id){
        List<Listing> listings = listingService.findByUserId(id);
        if(listings.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(listings);
        }
    }

    @PostMapping("/listing")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Listing> createListing(@RequestBody Listing listing){
        listing.setCreatedAt(LocalDateTime.now());
        Listing createdListing = listingService.save(listing);
        if(createdListing != null){
            return ResponseEntity.status(HttpStatus.CREATED).body(createdListing);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/listings/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Listing> updateListing(@PathVariable("id") String id, @RequestBody Listing listing){
        Optional<Listing> optionalListing = Optional.ofNullable(listingService.update(id, listing));
        return optionalListing.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/listings/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteListing(@PathVariable("id") String id){
        boolean deleted = listingService.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/listings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllListings(){
        listingService.deleteAll();
    }

}
