package com.example.delivery.Order;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(){
        return new ResponseEntity<List<Order>>(
               orderService.allOrders(), HttpStatus.OK
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Order>> getOrder(@PathVariable ObjectId id){
        return new ResponseEntity<Optional<Order>>(orderService.order(id), HttpStatus.OK);
    }
    @GetMapping("/deliveres")
    public ResponseEntity<List<Order>> allDelivery(){
        return new ResponseEntity<List<Order>>(orderService.allDelivery(), HttpStatus.OK);
    }
    @PostMapping("/deliver/{id}")
    public ResponseEntity<Boolean> setDelivered(@PathVariable ObjectId id){
        Boolean update = orderService.deliver(id);
        return ResponseEntity.ok(update);
    }
    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody Order order){
        String newOrder = orderService.createOrder(order);
        return new ResponseEntity<String>(newOrder, HttpStatus.OK);
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateOrder(@RequestBody UpdateInformation information){
        Boolean newOrder = orderService.updateOrder(information);
        return ResponseEntity.ok(newOrder);
    }
    @GetMapping("/customerOrders/{customerId}")
    public ResponseEntity<List<Order>> getCustomerOrders(@PathVariable ObjectId customerId){
        return new ResponseEntity<List<Order>>(orderService.getCustomerOrders(customerId), HttpStatus.OK);
    }
    @GetMapping("/courierOrders/{courierId}")
    public ResponseEntity<List<Order>> getCourierOrders(@PathVariable ObjectId courierId){
        return new ResponseEntity<List<Order>>(orderService.getCourierOrders(courierId), HttpStatus.OK);
    }
    @PostMapping("/increment-rating")
    public ResponseEntity<?> rateRestaurant(@RequestBody RatingDTO rating){
        Boolean newRating = orderService.incrementRating(rating);
        return ResponseEntity.ok(newRating);
    }
}
