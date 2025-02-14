package com.StayAt.StayAt.controllers;


import com.StayAt.StayAt.dto.ReservationDTO;
import com.StayAt.StayAt.models.Reservation;
import com.StayAt.StayAt.security.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class ReservationController {

    @Autowired
    ReservationService reservationService;

    @GetMapping("/reservations")
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationDTO> getAllReservations(){
        return reservationService.findAll();
    }

    @GetMapping("/reservations/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Reservation> getReservation(@PathVariable("id") String id){
        return reservationService.findById(id);
    }

    @GetMapping("/reservations/listing/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Reservation> getReservationByListingId(@PathVariable("id") String id){
        return reservationService.findByListingId(id);
    }

    @GetMapping("/reservations/user/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Reservation> getReservationsByUserId(@PathVariable("id") String id){
        return reservationService.findByUserId(id);
    }

    @GetMapping("/reservations/owner/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Reservation> getReservationsByListingOwner(@PathVariable("id") String id){
        return reservationService.findByListingOwner(id);
    }


    @PostMapping("/reservations")
    @ResponseStatus(HttpStatus.CREATED)
    public Optional<Reservation> createReservation(@RequestBody Reservation reservation){
        return Optional.ofNullable(reservationService
                .save(new Reservation(
                        reservation.getUserId(),
                        reservation.getListingId(),
                        reservation.getStartDate(),
                        reservation.getEndDate(),
                        reservation.getTotalPrice(),
                        LocalDateTime.now())));
    }

    @PutMapping("/reservations/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Reservation updateReservation(@PathVariable("id") String id, @RequestBody Reservation reservation){
        return reservationService.update(id, reservation);
    }

    @DeleteMapping("/reservations/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReservation(@PathVariable("id") String id){
        reservationService.deleteById(id);
    }

    @DeleteMapping("/reservations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllReservations(){
        reservationService.deleteAll();
    }
}
