package com.epam.training.webshop.ui.command;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.checkout.CheckoutService;
import com.epam.training.webshop.core.checkout.order.model.OrderDto;
import com.epam.training.webshop.core.product.ProductService;
import com.epam.training.webshop.core.product.model.ProductDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@RequiredArgsConstructor
public class CartCommand {

  private final CheckoutService checkoutService;
  private final ProductService productService;
  private final Cart cart;

  @ShellMethod(key = "user cart addProduct", value = "Add product to the cart!")
  protected String userAddProductToCart(String productName, int amount) {
    Optional<ProductDto> product = productService.getProductByName(productName);

    if (product.isEmpty()) {
      return productName + " is not found!";
    } else {
      cart.add(product.get(), amount);
      return productName + " added to cart!";
    }
  }

  @ShellMethod(key = "user cart checkout", value = "Cart checkout!")
  protected String cartCheckout() {
    if (cart.isEmpty()) {
      return "Cart is empty!";
    } else {
      OrderDto orderDto = checkoutService.checkout(cart);
      return "Your order: " + orderDto;
    }
  }

  @ShellMethod(key = "user cart clear", value = "Clear cart!")
  protected String clearCart() {
    cart.clear();
    return "Cart is cleared!";
  }

  @ShellMethod(key = "user cart list", value = "Get products from cart.")
  protected String listProducts() {
    if (cart.isEmpty()) {
      return "Cart is empty!";
    }
    return cart.getProducts().toString();
  }

  @ShellMethod(key = "user cart removeProduct", value = "Remove product from cart!")
  protected String removeProductFromCart(String productName) {
    Optional<ProductDto> product = productService.getProductByName(productName);
    if (product.isEmpty()) {
      return productName + " is not found!";
    }
    if (cart.containsProduct(product.get())) {
      cart.removeProduct(product.get());
      return "Product removed from the cart!";
    }
    return "Product is not found in the cart!";
  }
}
