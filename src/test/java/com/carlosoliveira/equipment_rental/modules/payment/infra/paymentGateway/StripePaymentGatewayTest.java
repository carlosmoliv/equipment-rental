package com.carlosoliveira.equipment_rental.modules.payment.infra.paymentGateway;

import com.carlosoliveira.equipment_rental.modules.notification.application.ports.MailService;
import com.carlosoliveira.equipment_rental.modules.rental.application.inputs.PaymentDetails;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.service.PaymentIntentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StripePaymentGatewayTest {

    @Mock
    private StripeClient stripeClient;

    @Mock
    private PaymentIntentService paymentIntentService;

    @Mock
    private MailService emailService;

    private StripePaymentGateway sut;

    @BeforeEach
    void setUp() {
        when(stripeClient.paymentIntents()).thenReturn(paymentIntentService);
        sut = new StripePaymentGateway(stripeClient, emailService);
    }

    @Test
    public void process_payment_creates_a_payment_intent() throws StripeException {
        // Arrange
        PaymentDetails paymentDetails = new PaymentDetails(BigDecimal.valueOf(50.0), "test@email.com");
        PaymentIntent mockPaymentIntent = new PaymentIntent();
        when(paymentIntentService.create(any(PaymentIntentCreateParams.class))).thenReturn(mockPaymentIntent);

        // Act
        sut.processPayment(paymentDetails);

        // Assert
        verify(paymentIntentService).create(any(PaymentIntentCreateParams.class));
    }
}
