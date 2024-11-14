package com.epam.training.webshop.core.warehouse;

import com.epam.training.webshop.core.checkout.CheckoutObserver;
import com.epam.training.webshop.core.checkout.model.Order;

public class WarehouseService implements CheckoutObserver {

  @Override
  public void handleOrder(Order order) {
    System.out.println("WarehouseService handleOrder method is called!");
  }
}
