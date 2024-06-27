package com.example.delivery.Order;


import com.example.delivery.Restaurant.Menu;
import com.example.delivery.Restaurant.Restaurant;
import com.example.delivery.User.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Document(collection = "orders")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @Getter
    private String id;
    @Getter
    private User customer;
    @Getter
    private User courier;
    @Getter
    private Restaurant restaurant;
    @Getter
    private List<Menu> menu;
    @Getter
    private double price;
    @Getter
    private Boolean delivered;

    public Order(User customer, User courier, Restaurant restaurant, List<Menu> menu, double price, Boolean delivered) {
        this.customer = customer;
        this.courier = courier;
        this.restaurant = restaurant;
        this.menu = menu;
        this.price = price;
        this.delivered = delivered;
    }
}
