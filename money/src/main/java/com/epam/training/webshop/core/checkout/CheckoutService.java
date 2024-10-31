package com.epam.training.webshop.core.checkout;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.cart.grosspricecalculator.GrossPriceCalculator;
import com.epam.training.webshop.core.checkout.model.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CheckoutService {

  private final GrossPriceCalculator grossPriceCalculator;
  private final Cart cart;

  public Order checkout() {
    return new Order(
        cart.getProducts(),
        cart.getAggregatedNetPrice(),
         grossPriceCalculator.getAggregatedGrossPrice(cart));
  }

}
