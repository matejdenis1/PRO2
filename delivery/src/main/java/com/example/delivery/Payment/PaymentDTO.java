package com.example.delivery.Payment;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import javax.persistence.Entity;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {


    ObjectId orderId;
    String paymentMethod;
    String paymentStatus;
    Date paymentDate;
}
