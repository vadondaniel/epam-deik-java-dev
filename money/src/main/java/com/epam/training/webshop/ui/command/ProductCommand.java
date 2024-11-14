package com.epam.training.webshop.ui.command;

import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.ProductService;
import com.epam.training.webshop.core.product.model.Product;
import java.util.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class ProductCommand {
  private final ProductService productService;

  @ShellMethod(key = "user product list", value = "Lists all products!")
  protected String listProducts() {
    return productService.getAllProducts().toString();
  }

  @ShellMethod(key = "user product createProduct", value = "Add new product to store.")
  protected Product createProduct(String productName, int amount) {
    Money money = new Money(amount, Currency.getInstance("HUF"));
    Product product = new Product(productName, money);
    productService.createProduct(product);
    return product;
  }
}
