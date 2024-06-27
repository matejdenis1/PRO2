package com.example.delivery.Payment;

import com.example.delivery.Order.Order;
import com.example.delivery.User.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Document(collection = "payments")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @Getter
    private ObjectId id;
    @Getter
    @Setter
    private Order order;
    @Getter
    @Setter
    private User customer;
    @Getter
    @Setter
    private String paymentMethod;
    @Getter
    @Setter
    private String paymentStatus;
    @Getter
    @Setter
    private double totalAmount;
    @Getter
    @Setter
    private Date paymentDate;

}
