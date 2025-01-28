package com.carlosoliveira.equipment_rental.modules.payment.application;

import com.carlosoliveira.equipment_rental.modules.rental.application.inputs.PaymentDetails;

public interface PaymentGatewayService {
    void processPayment(PaymentDetails paymentDetails);
}
