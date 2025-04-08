package com.carlosoliveira.equipment_rental.modules.payment.application;

import com.carlosoliveira.equipment_rental.modules.rental.application.inputs.PaymentDetails;
import com.stripe.model.PaymentIntent;

public interface PaymentGatewayService {
    PaymentIntent processPayment(PaymentDetails paymentDetails);
}
