package com.carlosoliveira.equipment_rental.modules.payment.infra.paymentGateway;

import com.carlosoliveira.equipment_rental.modules.notification.application.ports.MailService;
import com.carlosoliveira.equipment_rental.modules.rental.application.inputs.PaymentDetails;
import com.carlosoliveira.equipment_rental.modules.payment.application.PaymentGatewayService;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripePaymentGateway implements PaymentGatewayService {
    private static final Logger logger = LoggerFactory.getLogger(StripePaymentGateway.class);
    private final StripeClient client;
    private final MailService mailService;

    public StripePaymentGateway(StripeClient stripeClient, MailService mailService) {
        this.client = stripeClient;
        this.mailService = mailService;
    }

    @Override
    public void processPayment(PaymentDetails paymentDetails) {
        long amountInCents = paymentDetails.amount().multiply(BigDecimal.valueOf(100)).longValue();
        PaymentIntentCreateParams params = PaymentIntentCreateParams
                .builder()
                .setAmount(amountInCents)
                .setConfirm(true)
                .setCurrency("usd")
                .setPaymentMethod("pm_card_visa")
                .setReturnUrl("https://test.com")
                .build();
        createPaymentIntent(params);
        sendEmail(paymentDetails);
    }

    private void createPaymentIntent(PaymentIntentCreateParams params) {
        try {
            this.client.paymentIntents().create(params);
        } catch (StripeException e) {
            logger.error("Stripe payment intent creation failed, error: {}", e.getMessage());
            throw new RuntimeException("Payment processing failed", e);
        }
    }

    private void sendEmail(PaymentDetails paymentDetails) {
        try {
            mailService.send(
                    paymentDetails.email(),
                    "Payment confirmation",
                    "Your payment of $" + paymentDetails.amount() + " has been processed successfully!"
            );
        } catch (Exception e) {
            logger.error("Failed to send payment confirmation email, error: {}", e.getMessage());
        }
    }
}
