package com.StayAt.StayAt.security.services;

import com.StayAt.StayAt.dto.ReservationDTO;
import com.StayAt.StayAt.models.Listing;
import com.StayAt.StayAt.models.Reservation;
import com.StayAt.StayAt.repository.ListingRepository;
import com.StayAt.StayAt.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    @Autowired
    ReservationRepository reservationRepository;

    @Autowired
    ListingRepository listingRepository;

    public List<ReservationDTO> findAll(){
        List<Reservation> reservations = reservationRepository.findAll();

        return reservations.stream()
                .map(reservation -> {
                    ReservationDTO dto = new ReservationDTO();
                    dto.setId(reservation.getId());
                    dto.setUserId(reservation.getUserId());
                    dto.setListingId(reservation.getListingId());
                    dto.setStartDate(reservation.getStartDate());
                    dto.setEndDate(reservation.getEndDate());
                    dto.setTotalPrice(reservation.getTotalPrice());
                    dto.setCreatedAt(reservation.getCreatedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public Optional<Reservation> findById(String id){
        return reservationRepository.findById(id);
    }

    public List<Reservation> findByListingId(String listingId){
        List<Reservation> reservations = reservationRepository.findByListingId(listingId);
        reservations.forEach(reservation -> {
            listingRepository.findById(reservation.getListingId()).ifPresent(reservation::setListing);
        } );
        return reservations;
    }

    public List<Reservation> findByUserId(String userId){
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        reservations.forEach(reservation -> {
            listingRepository.findById(reservation.getListingId()).ifPresent(reservation::setListing);
        });
        return reservations;
    }

    public List<Reservation> findByListingOwner(String listingCreatorId){
        List<Listing> listings = listingRepository.findByUserId(listingCreatorId);

        List<String> listingIds = listings.stream()
                .map(Listing::getId)
                .collect(Collectors.toList());
        List<Reservation> reservations = listingIds.stream()
                .flatMap(listingId -> reservationRepository.findByListingId(listingId).stream())
                .collect(Collectors.toList());

        reservations.forEach(reservation -> {
            listingRepository.findById(reservation.getListingId()).ifPresent(reservation::setListing);
        });
        return reservations;
    }

    public Reservation save(Reservation reservation){
        return reservationRepository.save(reservation);
    }

    public Reservation update(String id, Reservation reservation){
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if(optionalReservation.isPresent()){
            reservation.setId(id);
            return reservationRepository.save(reservation);
        }else {
            return null;
        }
    }

    public void deleteById(String id){
        reservationRepository.deleteById(id);
    }

    public void deleteAll(){
        reservationRepository.deleteAll();
    }





}
