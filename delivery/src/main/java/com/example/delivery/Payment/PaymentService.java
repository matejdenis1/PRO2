package com.example.delivery.Payment;

import com.example.delivery.Order.RatingDTO;
import com.example.delivery.User.User;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.logging.Logger;

@Service
public class PaymentService {
    private PaymentRepository paymentRepository;

    private final MongoTemplate mongoTemplate;


    public PaymentService(MongoTemplate mongoTemplate, PaymentRepository paymentRepository) {
        this.mongoTemplate = mongoTemplate;
        this.paymentRepository = paymentRepository;
    }
    public Payment getPaymentByOrderId(ObjectId orderId){
        Payment payment = paymentRepository.getByOrderId(orderId);
        if(Objects.equals(payment.getPaymentStatus(),"pending")){
            return payment;
        }
        return null;
    }
    public Boolean updatePayment(PaymentDTO payment){

        String paymentStatus = payment.getPaymentStatus();
        String paymentMethod = payment.getPaymentMethod();

        if(!Objects.equals(paymentMethod, "canceled") && !Objects.equals(paymentMethod, "cash") && !Objects.equals(paymentMethod, "card")){
            return false;
        }
        if(!Objects.equals(paymentStatus, "paid") && !Objects.equals(paymentStatus, "returned")){
            return false;
        }

        Payment newPayment = getPaymentByOrderId(payment.getOrderId());
        newPayment.setPaymentMethod(payment.getPaymentMethod());
        newPayment.setPaymentDate(payment.getPaymentDate());
        newPayment.setPaymentStatus(payment.getPaymentStatus());

        try{

            mongoTemplate.save(newPayment);
            return true;
        }
        catch (Exception e){
            System.out.println(e);
            return false;
        }
    }
    public Boolean createPayment(Payment payment){
        try{
            mongoTemplate.save(payment);
            return true;
        }
        catch(Exception e){
            System.out.println(e);
            return false;
        }
    }

}
