package com.epam.training.webshop.core.checkout;

import com.epam.training.webshop.core.cart.Cart;
import com.epam.training.webshop.core.cart.grosspricecalculator.GrossPriceCalculator;
import com.epam.training.webshop.core.checkout.order.model.OrderDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CheckoutService {

  private final GrossPriceCalculator calculator;
  private final CheckoutObservable checkoutObservable;

  public OrderDto checkout(Cart cart) {
    OrderDto orderDto = new OrderDto(cart.getProducts(), cart.getAggregatedNetPrice(), calculator.getAggregatedGrossPrice(cart));
    checkoutObservable.notifyObservers(orderDto);
    return orderDto;
  }

}
