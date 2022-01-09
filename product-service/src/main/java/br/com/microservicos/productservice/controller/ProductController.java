package br.com.microservicos.productservice.controller;

import br.com.microservicos.productservice.model.Product;
import br.com.microservicos.productservice.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

  private final ProductRepository repository;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Product> findAll() {
    return repository.findAll();
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createProduct(@RequestBody Product product) {
    repository.save(product);
  }
}
