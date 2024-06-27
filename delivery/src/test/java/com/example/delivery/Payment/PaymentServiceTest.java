package com.example.delivery.Payment;

import com.example.delivery.Order.Order;
import com.example.delivery.Restaurant.Restaurant;
import com.example.delivery.User.User;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Or;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class PaymentServiceTest {

    @Mock
    private MongoTemplate mongoTemplate;
    @Mock
    private PaymentRepository paymentRepository;
    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void TestUpdatePayment_InvalidPaymentMethod() {

        PaymentDTO paymentDTO = new PaymentDTO(new ObjectId(), "invalid", "paid", Date.from(Instant.now()));

        Boolean result = paymentService.updatePayment(paymentDTO);

        assertFalse(result);
        verify(mongoTemplate, never()).save(any(Payment.class));
    }
    @Test
    void TestUpdatePayment_InvalidPaymentStatus() {
        PaymentDTO paymentDTO = new PaymentDTO(new ObjectId(), "paid", "invalid", Date.from(Instant.now()));

        Boolean result = paymentService.updatePayment(paymentDTO);

        assertFalse(result);
        verify(mongoTemplate, never()).save(any(Payment.class));
    }
    @Test
    void TestUpdatePayment_Success() {
        Payment mockPayment = new Payment();

        mockPayment.setOrder(new Order(String.valueOf(new ObjectId()), new User(),new User(),new Restaurant(),new ArrayList<>(),12,true));
        mockPayment.setPaymentMethod("pending");
        mockPayment.setPaymentStatus("pending");
        mockPayment.setPaymentDate(Date.from(Instant.now()));

        PaymentDTO paymentDTO = new PaymentDTO(new ObjectId(), "cash", "paid", Date.from(Instant.now()));

        when(paymentRepository.getByOrderId(paymentDTO.getOrderId())).thenReturn(mockPayment);

        Boolean result = paymentService.updatePayment(paymentDTO);

        assertTrue(result);
        verify(mongoTemplate, times(1)).save(mockPayment);
    }
}