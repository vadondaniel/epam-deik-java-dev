package com.epam.training.webshop.ui.config;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.checkout.CheckoutService;
import com.epam.training.webshop.core.product.ProductService;
import com.epam.training.webshop.ui.command.AbstractCommand;
import com.epam.training.webshop.ui.command.UserCartAddProductCommand;
import com.epam.training.webshop.ui.command.UserCartCheckoutCommand;
import com.epam.training.webshop.ui.command.UserCartClearCommand;
import com.epam.training.webshop.ui.command.UserCartListCommand;
import com.epam.training.webshop.ui.command.UserCartRemoveProductCommand;
import com.epam.training.webshop.ui.command.UserProductListCommand;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommandConfig {

  @Bean
  public AbstractCommand userCartAddProductCommand(
      ProductService productService, Cart cart) {
    return new UserCartAddProductCommand(productService, cart);
  }

  @Bean
  public AbstractCommand userCartCheckoutCommand(
      CheckoutService checkoutService, Cart cart) {
    return new UserCartCheckoutCommand(checkoutService, cart);
  }

  @Bean
  public AbstractCommand userCartClearCommand(Cart cart) {
    return new UserCartClearCommand(cart);
  }

  @Bean
  public AbstractCommand userCartListCommand(Cart cart) {
    return new UserCartListCommand(cart);
  }

  @Bean
  public AbstractCommand userCartRemoveProductCommand(
      ProductService productService, Cart cart) {
    return new UserCartRemoveProductCommand(productService, cart);
  }

  @Bean
  public AbstractCommand listProducts(ProductService productService) {
    return new UserProductListCommand(productService);
  }

}
