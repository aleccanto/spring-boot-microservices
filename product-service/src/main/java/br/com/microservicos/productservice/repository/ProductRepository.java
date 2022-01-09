package br.com.microservicos.productservice.repository;

import br.com.microservicos.productservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

}

