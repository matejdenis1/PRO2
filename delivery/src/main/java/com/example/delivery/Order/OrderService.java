package com.example.delivery.Order;

import com.example.delivery.Restaurant.Menu;
import com.example.delivery.Restaurant.Restaurant;
import com.example.delivery.User.User;
import com.example.delivery.User.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    private UserService userService;

    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());

    private final MongoTemplate mongoTemplate;

    public OrderService(MongoTemplate mongoTemplate, UserService userService){
        this.mongoTemplate = mongoTemplate;
        this.userService = userService;
    }

    public List<Order> allOrders(){
        return orderRepository.findAll();
    }
    public List<Order> allDelivery() {
        List<Order> orders = allOrders();
        orders.removeIf(order -> order.getCourier() != null);
        return orders;
    }
    public Boolean deliver(ObjectId id){
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("delivered", true);

        try{
            mongoTemplate.updateFirst(query,update,Order.class);
            return true;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
    public List<Order> getCustomerOrders(ObjectId customerId){
        return orderRepository.findByCustomerId(customerId);
    }
    public List<Order> getCourierOrders(ObjectId courierId){
        return orderRepository.findByCourierId(courierId);
    }
    public Optional<Order> order(ObjectId id){
        return orderRepository.findById(id);
    }

    public String createOrder(Order order){
        User customer = order.getCustomer();
        User courier =  order.getCourier();
        Restaurant restaurant = order.getRestaurant();
        List<Menu> menu = order.getMenu();
        double price = order.getPrice();
        Order newOrder = new Order(customer,courier, restaurant, menu, price, false);
        try{
            return mongoTemplate.save(newOrder).getId();
        }
        catch (Exception e){
            System.out.println(e);
            return null;
        }
    }
    public Boolean updateOrder(UpdateInformation information){

        Optional<User> user = userService.getUser(information.getCourierId());

        if (user.isEmpty()) {
            return false;
        }

        User courier = user.get();

        Query query = new Query(Criteria.where("id").is(information.getOrderId()));
        Update update = new Update().set("courier", courier);

        try{
            mongoTemplate.updateFirst(query,update, Order.class);
            return true;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
    public Boolean incrementRating(RatingDTO rating){
        if(rating.getRating() < 1 || rating.getRating() > 5) return false;


        Query queryRestaurant = new Query(Criteria.where("_id").is(rating.getRestaurantId()));
        Update updateRestaurant = new Update().inc("rating", rating.getRating());

        Query queryCustomer = new Query(Criteria.where("_id").is(rating.getCustomerId()));
        Update updateCustomer = new Update().addToSet("ratedRestaurants", rating.getRestaurantId());

        try{
            mongoTemplate.updateFirst(queryRestaurant, updateRestaurant, "restaurants");
            mongoTemplate.updateFirst(queryCustomer,updateCustomer,User.class);
            return true;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }

}
