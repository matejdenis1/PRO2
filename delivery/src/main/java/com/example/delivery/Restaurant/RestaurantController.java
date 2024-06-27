package com.example.delivery.Restaurant;


import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;
    @GetMapping
    public ResponseEntity<List<Restaurant>> getAllRestaurants(){
        return new ResponseEntity<List<Restaurant>>(restaurantService.allRestaurants(), HttpStatus.OK) ;
    }
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Restaurant>> getRestaurant(@PathVariable ObjectId id){
        return new ResponseEntity<Optional<Restaurant>>(restaurantService.restaurant(id), HttpStatus.OK);
    }
}
