package com.epam.training.webshop.core.config;

import com.epam.training.webshop.core.product.persistence.Product;
import com.epam.training.webshop.core.product.persistence.ProductRepository;
import com.epam.training.webshop.core.user.persistence.User;
import com.epam.training.webshop.core.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("!prod")
public class InMemoryDatabaseInitializer {

  private final UserRepository userRepository;
  private final ProductRepository productRepository;

  public void init() {
    User admin = new User("admin", "admin", User.Role.ADMIN);
    userRepository.save(admin);

    Product liszt = new Product("Liszt", 100.0, "HUF");
    productRepository.save(liszt);
  }
}
