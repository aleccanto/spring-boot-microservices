package br.com.microservicos.orderservice.controller;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

import br.com.microservicos.orderservice.client.InventoryClient;
import br.com.microservicos.orderservice.dto.OrderDto;
import br.com.microservicos.orderservice.model.Order;
import br.com.microservicos.orderservice.repository.OrderRepository;


import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreaker;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final InventoryClient inventoryClient;
    private final OrderRepository repository;
    private final Resilience4JCircuitBreakerFactory circuitBreakerFactory;
    private final StreamBridge streamBridge;
    private final ExecutorService executorService;

    @PostMapping
    public String placeOrder(@RequestBody OrderDto orderDto) {

        circuitBreakerFactory.configureExecutorService(executorService);
        Resilience4JCircuitBreaker circuitBreaker = circuitBreakerFactory.create("inventory");
        Supplier<Boolean> booleanSupplier = () -> orderDto.getOrderLineItemsList().stream()
                .allMatch(lineItem -> inventoryClient.checkStock(lineItem.getSkuCode()));
        boolean productsInStock = circuitBreaker.run(booleanSupplier, throwable -> handleErroCase());

        if (productsInStock) {
            Order order = new Order();
            order.setOrderLineItens(orderDto.getOrderLineItemsList());
            order.setOrderNumber(UUID.randomUUID().toString());
            repository.save(order);

            log.info("Seding Order Details to Notification Service");
            streamBridge.send("notificationEventSupplier-out-0", MessageBuilder.withPayload(order.getId()).build());

            return "Order Place Sucessfully";
        } else {
            return "Order failed, one of the products in the order is not in stock";
        }
    }

    private Boolean handleErroCase() {
        return false;
    }
}
