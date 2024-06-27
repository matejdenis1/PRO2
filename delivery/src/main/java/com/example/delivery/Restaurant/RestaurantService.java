package com.example.delivery.Restaurant;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepository;
    public List<Restaurant> allRestaurants(){
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> restaurant(ObjectId id){
        return restaurantRepository.findById(id);
    }
}
