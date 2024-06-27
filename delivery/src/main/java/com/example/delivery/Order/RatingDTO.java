package com.example.delivery.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import javax.persistence.Entity;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {


    @Getter
    @Setter
    ObjectId customerId;
    @Getter
    @Setter
    ObjectId restaurantId;
    @Getter
    @Setter
    int rating;



}
