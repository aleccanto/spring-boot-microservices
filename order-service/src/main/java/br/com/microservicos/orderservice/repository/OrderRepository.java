package br.com.microservicos.orderservice.repository;

import br.com.microservicos.orderservice.model.Order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
