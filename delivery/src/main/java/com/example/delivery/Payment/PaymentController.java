package com.example.delivery.Payment;

import com.example.delivery.Order.Order;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody Payment payment){
        Boolean newPayment = paymentService.createPayment(payment);
        return ResponseEntity.ok(newPayment);
    }
    @PostMapping("/updatePayments")
    public ResponseEntity<?> updatePayment(@RequestBody PaymentDTO payment){
        Boolean newPayment = paymentService.updatePayment(payment);
        return ResponseEntity.ok(newPayment);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable ObjectId id){
        return new ResponseEntity<Payment>(paymentService.getPaymentByOrderId(id), HttpStatus.OK);
    }
}
