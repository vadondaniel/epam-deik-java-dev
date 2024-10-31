package com.epam.training.webshop.core.product;

import com.epam.training.webshop.core.product.model.Product;
import com.epam.training.webshop.core.finance.money.Money;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

public class ProductServiceImpl implements ProductService {

  private static final Currency HUF = Currency.getInstance("HUF");

  private final List<Product> productDb = List.of(
      new Product("Xiaomi", new Money(100_000, HUF)),
      new Product("Iphone", new Money(400_000, HUF)),
      new Product("OnePlus", new Money(410_000, HUF)),
      new Product("Nokia", new Money(355_000, HUF))
  );

  @Override
  public List<Product> getAllProducts() {
    return List.copyOf(productDb);
  }

  @Override
  public Optional<Product> getProductByName(String name) {
    return productDb.stream()
        .filter(product -> product.getName().equals(name))
        .findFirst();
  }
}
