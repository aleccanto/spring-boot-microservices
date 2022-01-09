package br.com.microservicos.notificationservice;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailSender {

    public void sendEmail(String message) {
        log.info("Order Placed Successfully - Order Number is {}", message);
        log.info("Sending Email to Customer");
    }
}
