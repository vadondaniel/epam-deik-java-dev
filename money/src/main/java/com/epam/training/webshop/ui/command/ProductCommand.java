package com.epam.training.webshop.ui.command;

import com.epam.training.webshop.core.finance.money.Money;
import com.epam.training.webshop.core.product.ProductService;
import com.epam.training.webshop.core.product.model.ProductDto;
import com.epam.training.webshop.core.user.UserService;
import com.epam.training.webshop.core.user.model.UserDto;
import com.epam.training.webshop.core.user.persistence.User;
import java.util.Currency;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;

@ShellComponent
@RequiredArgsConstructor
public class ProductCommand {
  private final ProductService productService;
  private final UserService userService;

  @ShellMethod(key = "user product list", value = "Lists all products!")
  protected String listProducts() {
    return productService.getAllProducts().toString();
  }

  @ShellMethodAvailability("isAvailable")
  @ShellMethod(key = "admin product create", value = "Create a new product.")
  protected ProductDto createProduct(String productName, int amount) {
    Money money = new Money(amount, Currency.getInstance("HUF"));
    ProductDto product = new ProductDto(productName, money);
    productService.createProduct(product);
    return product;
  }

  private Availability isAvailable() {
    Optional<UserDto> user = userService.describe();
    return user.isPresent() && user.get().role() == User.Role.ADMIN
        ? Availability.available()
        : Availability.unavailable("You are not an admin!");
  }
}
