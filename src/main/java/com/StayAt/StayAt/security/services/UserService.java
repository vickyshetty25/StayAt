package com.StayAt.StayAt.security.services;

import com.StayAt.StayAt.models.Listing;
import com.StayAt.StayAt.models.User;
import com.StayAt.StayAt.repository.ListingRepository;
import com.StayAt.StayAt.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ListingRepository listingRepository;

    public List<Listing> getFavoriteListings(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user != null){
            return Arrays.stream(user.getFavoriteListings())
                    .map(listingId -> listingRepository.findById(listingId))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }else {
            return new ArrayList<>();
        }
    }

    public String addFavoriteListing(String userId, String favoriteListing) throws JsonProcessingException {
        User user = userRepository.findById(userId).orElse(null);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(favoriteListing);
        String favoriteListingValue = jsonNode.get("favoriteListing").asText();
        boolean alreadyAdded = false;

        if (user != null) {
            String[] favorites = user.getFavoriteListings();
            if (favorites == null) {
                // If favorites is null, create a new array with a single value
                String[] updatedFavorites = new String[]{favoriteListingValue};
                user.setFavoriteListings(updatedFavorites);
                userRepository.save(user);
            } else {
                if (Arrays.asList(favorites).contains(favoriteListingValue)) {
                    // Already added
                    alreadyAdded = true;
                } else {
                    // Add the new favorite listing
                    String[] updatedFavorites = Arrays.copyOf(favorites, favorites.length + 1);
                    updatedFavorites[favorites.length] = favoriteListingValue;
                    user.setFavoriteListings(updatedFavorites);
                    userRepository.save(user);
                }
            }
        }

        if (alreadyAdded) {
            return null;
        } else {
            return favoriteListingValue;
        }
    }

    public String removeFavoriteListing(String userId, String favoriteListing) throws JsonProcessingException {
        User user = userRepository.findById(userId).orElse(null);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(favoriteListing);
        String favoriteListingValue = jsonNode.get("favoriteListing").asText();
        boolean isRemoved = false;

        if (user != null) {
            String[] favorites = user.getFavoriteListings();
            if (favorites != null) {
                List<String> updatedFavorites = new ArrayList<>(Arrays.asList(favorites));
                isRemoved = updatedFavorites.remove(favoriteListingValue);
                if (isRemoved) {
                    user.setFavoriteListings(updatedFavorites.toArray(new String[0]));
                    userRepository.save(user);
                }
            }
        }

        if (isRemoved) {
            return favoriteListingValue;
        } else {
            return null;
        }
    }
}
