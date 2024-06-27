package com.example.delivery.Order;

import com.example.delivery.User.User;
import com.example.delivery.User.UserService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
class OrderServiceTest {


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderService(mongoTemplate, userService);
    }
    @Mock UserService userService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MongoTemplate mongoTemplate;

    @InjectMocks
    private OrderService orderService;


    @Test
    public void testUpdateOrder_Success() {
        // Mock data
        ObjectId orderId = new ObjectId();
        ObjectId courierId = new ObjectId();
        UpdateInformation information = new UpdateInformation(courierId,orderId);

        User mockCourier = new User(String.valueOf(courierId), "test@test.com", "password", "Test 123", "courier", new ArrayList<>());

        when(userService.getUser(information.getCourierId())).thenReturn(Optional.of(mockCourier));

        Query query = new Query(Criteria.where("id").is(orderId));
        Update update = new Update().set("courier", mockCourier);

        when(mongoTemplate.updateFirst(query, update, Order.class)).thenReturn(null);

        boolean result = orderService.updateOrder(information);


        verify(userService, times(1)).getUser(courierId);
        verify(mongoTemplate, times(1)).updateFirst(query, update, Order.class);

        assertTrue(result);
    }

    @Test
    public void testUpdateOrder_UserNotFound() {

        ObjectId orderId = new ObjectId();
        ObjectId courierId = new ObjectId();

        UpdateInformation information = new UpdateInformation(courierId,orderId);

        User mockCourier = new User(String.valueOf(courierId), "test@test.com", "password", "Test 123", "courier", new ArrayList<>());

        when(userService.getUser(information.getCourierId())).thenReturn(Optional.empty());

        boolean result = orderService.updateOrder(information);


        Query query = new Query(Criteria.where("id").is(information.getOrderId()));
        Update update = new Update().set("courier", mockCourier);

        verify(userService, times(1)).getUser(information.getCourierId());
        verify(mongoTemplate, never()).updateFirst(query,update,Order.class);

        assertFalse(result);
    }

    @Test
    public void testIncrementRating_Success() {
        ObjectId restaurantId = new ObjectId();
        ObjectId customerId = new ObjectId();
        RatingDTO rating = new RatingDTO(restaurantId, customerId, 1);

        Query queryRestaurant = new Query(Criteria.where("_id").is(rating.getRestaurantId()));
        Update updateRestaurant = new Update().inc("rating", rating.getRating());

        Query queryCustomer = new Query(Criteria.where("_id").is(rating.getCustomerId()));
        Update updateCustomer = new Update().addToSet("ratedRestaurants", rating.getRestaurantId());

        when(mongoTemplate.updateFirst(queryRestaurant, updateRestaurant, "restaurants")).thenReturn(null);
        when(mongoTemplate.updateFirst(queryCustomer, updateCustomer, User.class)).thenReturn(null);

        boolean result = orderService.incrementRating(rating);

        verify(mongoTemplate, times(1)).updateFirst(queryRestaurant, updateRestaurant, "restaurants");
        verify(mongoTemplate, times(1)).updateFirst(queryCustomer, updateCustomer, User.class);

        assertTrue(result);
    }
    @Test
    public void testIncrementRating_Failure() {
        ObjectId restaurantId = new ObjectId();
        ObjectId customerId = new ObjectId();
        RatingDTO rating = new RatingDTO(restaurantId, customerId, 6);

        Query queryRestaurant = new Query(Criteria.where("_id").is(restaurantId));
        Update updateRestaurant = new Update().inc("rating", rating.getRating());

        Query queryCustomer = new Query(Criteria.where("_id").is(customerId));
        Update updateCustomer = new Update().addToSet("ratedRestaurants", restaurantId);

        doThrow(new RuntimeException("MongoDB update failed")).when(mongoTemplate).updateFirst(queryRestaurant, updateRestaurant, "restaurants");

        boolean result = orderService.incrementRating(rating);

        verify(mongoTemplate, never()).updateFirst(queryRestaurant, updateRestaurant, "restaurants");
        verify(mongoTemplate, never()).updateFirst(queryCustomer, updateCustomer, User.class);

        assertFalse(result);
    }


}