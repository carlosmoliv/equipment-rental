package com.carlosoliveira.equipment_rental.modules.rental.application.ports;

import com.carlosoliveira.equipment_rental.modules.rental.application.inputs.PaymentDetails;

public interface PaymentGatewayService {
    void processPayment(PaymentDetails paymentDetails);
}
