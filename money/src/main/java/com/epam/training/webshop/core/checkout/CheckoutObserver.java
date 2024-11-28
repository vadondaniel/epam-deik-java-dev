package com.epam.training.webshop.core.checkout;

import com.epam.training.webshop.core.checkout.order.model.OrderDto;

public interface CheckoutObserver {
  void handleOrder(OrderDto orderDto);
}
