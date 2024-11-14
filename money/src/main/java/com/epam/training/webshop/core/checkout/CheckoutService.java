package com.epam.training.webshop.core.checkout;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.cart.grosspricecalculator.GrossPriceCalculator;
import com.epam.training.webshop.core.checkout.model.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CheckoutService {

  private final GrossPriceCalculator calculator;
  private final CheckoutObservable checkoutObservable;

  public Order checkout(Cart cart) {
    Order order = new Order(cart.getProducts(), cart.getAggregatedNetPrice(), calculator.getAggregatedGrossPrice(cart));
    checkoutObservable.notifyObservers(order);
    return order;
  }

}
